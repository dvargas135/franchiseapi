terraform {
  required_version = ">= 1.6.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.6"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

locals {
  name_prefix = var.project_name

  common_tags = merge(
    {
      Project     = var.project_name
      Environment = var.environment
      ManagedBy   = "Terraform"
    },
    var.tags
  )

  r2dbc_url = "r2dbc:mysql://${aws_db_instance.franchise.address}:3306/${var.db_name}"
}

data "aws_caller_identity" "current" {}

data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
}

resource "random_password" "db_password" {
  length           = 24
  special          = true
  override_special = "!#$%&*()-_=+[]{}<>:?"
}

resource "random_password" "jwt_secret" {
  length  = 48
  special = false
}

resource "aws_security_group" "apprunner" {
  name_prefix = "${local.name_prefix}-apprunner-"
  description = "Security group for App Runner VPC connector"
  vpc_id      = data.aws_vpc.default.id

  egress {
    description = "Allow outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = local.common_tags
}

resource "aws_security_group" "db" {
  name_prefix = "${local.name_prefix}-db-"
  description = "Security group for MySQL"
  vpc_id      = data.aws_vpc.default.id

  ingress {
    description     = "MySQL from App Runner VPC connector"
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.apprunner.id]
  }

  egress {
    description = "Allow outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = local.common_tags
}

resource "aws_db_subnet_group" "franchise" {
  name       = "${local.name_prefix}-db-subnet-group"
  subnet_ids = data.aws_subnets.default.ids

  tags = local.common_tags
}

resource "aws_db_instance" "franchise" {
  identifier             = "${local.name_prefix}-mysql"
  allocated_storage      = var.db_allocated_storage
  max_allocated_storage  = var.db_max_allocated_storage
  engine                 = "mysql"
  engine_version         = var.db_engine_version
  instance_class         = var.db_instance_class
  db_name                = var.db_name
  username               = var.db_username
  password               = random_password.db_password.result
  db_subnet_group_name   = aws_db_subnet_group.franchise.name
  vpc_security_group_ids = [aws_security_group.db.id]
  port                   = 3306

  publicly_accessible    = false
  skip_final_snapshot    = true
  deletion_protection    = false
  storage_encrypted      = true
  backup_retention_period = 7

  tags = local.common_tags
}

resource "aws_secretsmanager_secret" "db_password" {
  name        = "${local.name_prefix}/db/password"
  description = "Database password for Franchise API"

  tags = local.common_tags
}

resource "aws_secretsmanager_secret_version" "db_password" {
  secret_id     = aws_secretsmanager_secret.db_password.id
  secret_string = random_password.db_password.result
}

resource "aws_secretsmanager_secret" "jwt_secret" {
  name        = "${local.name_prefix}/jwt/secret"
  description = "JWT secret for Franchise API"

  tags = local.common_tags
}

resource "aws_secretsmanager_secret_version" "jwt_secret" {
  secret_id     = aws_secretsmanager_secret.jwt_secret.id
  secret_string = random_password.jwt_secret.result
}

resource "aws_iam_role" "apprunner_access" {
  name = "${local.name_prefix}-apprunner-access-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Service = "build.apprunner.amazonaws.com"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })

  tags = local.common_tags
}

resource "aws_iam_role_policy_attachment" "apprunner_ecr_access" {
  role       = aws_iam_role.apprunner_access.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSAppRunnerServicePolicyForECRAccess"
}

resource "aws_iam_role" "apprunner_instance" {
  name = "${local.name_prefix}-apprunner-instance-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Service = "tasks.apprunner.amazonaws.com"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })

  tags = local.common_tags
}

resource "aws_iam_role_policy" "apprunner_secrets" {
  name = "${local.name_prefix}-apprunner-secrets-policy"
  role = aws_iam_role.apprunner_instance.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "secretsmanager:GetSecretValue",
          "kms:Decrypt"
        ]
        Resource = [
          aws_secretsmanager_secret.db_password.arn,
          aws_secretsmanager_secret.jwt_secret.arn
        ]
      }
    ]
  })
}

resource "aws_apprunner_vpc_connector" "franchise" {
  vpc_connector_name = "${local.name_prefix}-vpc-connector"
  subnets            = data.aws_subnets.default.ids
  security_groups    = [aws_security_group.apprunner.id]

  tags = local.common_tags
}

resource "aws_apprunner_service" "franchise" {
  service_name = "${local.name_prefix}-service"

  source_configuration {
    authentication_configuration {
      access_role_arn = aws_iam_role.apprunner_access.arn
    }

    auto_deployments_enabled = false

    image_repository {
      image_identifier      = var.ecr_image_identifier
      image_repository_type = "ECR"

      image_configuration {
        port = "8080"

        runtime_environment_variables = {
          SPRING_R2DBC_URL      = local.r2dbc_url
          SPRING_R2DBC_USERNAME = var.db_username
          SPRINGDOC_SWAGGER_UI_ENABLED = "false"
          SPRINGDOC_API_DOCS_ENABLED   = "false"
        }

        runtime_environment_secrets = {
          SPRING_R2DBC_PASSWORD = aws_secretsmanager_secret.db_password.arn
          SECURITY_JWT_SECRET   = aws_secretsmanager_secret.jwt_secret.arn
        }
      }
    }
  }

  instance_configuration {
    cpu               = var.apprunner_cpu
    memory            = var.apprunner_memory
    instance_role_arn = aws_iam_role.apprunner_instance.arn
  }

  network_configuration {
    egress_configuration {
      egress_type       = "VPC"
      vpc_connector_arn = aws_apprunner_vpc_connector.franchise.arn
    }
  }

  health_check_configuration {
    protocol            = "HTTP"
    path                = "/swagger-ui/index.html"
    interval            = 10
    timeout             = 5
    healthy_threshold   = 1
    unhealthy_threshold = 5
  }

  tags = local.common_tags

  depends_on = [
    aws_db_instance.franchise,
    aws_iam_role_policy_attachment.apprunner_ecr_access,
    aws_iam_role_policy.apprunner_secrets
  ]
}
