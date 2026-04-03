variable "aws_region" {
  description = "AWS region for the deployment"
  type        = string
  default     = "us-east-1"
}

variable "project_name" {
  description = "Project name prefix used in resource naming"
  type        = string
  default     = "franchiseapi"
}

variable "environment" {
  description = "Deployment environment name"
  type        = string
  default     = "dev"
}

variable "ecr_image_identifier" {
  description = "Full ECR image identifier, for example 123456789012.dkr.ecr.us-east-1.amazonaws.com/franchiseapi:latest"
  type        = string
}

variable "db_name" {
  description = "MySQL database name"
  type        = string
  default     = "franchise_db"
}

variable "db_username" {
  description = "MySQL master username"
  type        = string
  default     = "franchise_user"
}

variable "db_instance_class" {
  description = "RDS instance class"
  type        = string
  default     = "db.t4g.micro"
}

variable "db_engine_version" {
  description = "MySQL engine version"
  type        = string
  default     = "8.0"
}

variable "db_allocated_storage" {
  description = "Initial RDS storage in GiB"
  type        = number
  default     = 20
}

variable "db_max_allocated_storage" {
  description = "Maximum autoscaled RDS storage in GiB"
  type        = number
  default     = 100
}

variable "apprunner_cpu" {
  description = "App Runner CPU setting"
  type        = string
  default     = "1 vCPU"
}

variable "apprunner_memory" {
  description = "App Runner memory setting"
  type        = string
  default     = "2 GB"
}

variable "tags" {
  description = "Additional tags to apply to resources"
  type        = map(string)
  default     = {}
}
