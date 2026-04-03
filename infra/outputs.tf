output "apprunner_service_url" {
  description = "Public URL of the App Runner service"
  value       = aws_apprunner_service.franchise.service_url
}

output "rds_endpoint" {
  description = "RDS endpoint hostname"
  value       = aws_db_instance.franchise.address
}

output "db_password_secret_arn" {
  description = "Secrets Manager ARN for the database password"
  value       = aws_secretsmanager_secret.db_password.arn
}

output "jwt_secret_arn" {
  description = "Secrets Manager ARN for the JWT secret"
  value       = aws_secretsmanager_secret.jwt_secret.arn
}
