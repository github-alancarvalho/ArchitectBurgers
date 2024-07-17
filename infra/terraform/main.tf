provider "aws" {
  region = "us-east-1"
}

# Filter out local zones, which are not currently supported
# with managed node groups
# data "aws_availability_zones" "available" {
#   filter {
#     name   = "opt-in-status"
#     values = ["opt-in-not-required"]
#   }
# }

locals {
  cluster_name = "fiap-architect-burgers-eks"
}

module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "5.8.1"

  name = "education-vpc"

  cidr = "10.0.0.0/16"
  azs  = ["us-east-1a", "us-east-1b"]

  private_subnets = ["10.0.1.0/24", "10.0.2.0/24"]
  public_subnets  = ["10.0.3.0/24", "10.0.4.0/24"]

  enable_nat_gateway   = true
  single_nat_gateway   = true
  enable_dns_hostnames = true

  public_subnet_tags = {
    "kubernetes.io/role/elb" = 1
  }

  private_subnet_tags = {
    "kubernetes.io/role/internal-elb" = 1
  }
}

resource "aws_security_group" "rds" {
  name   = "education_rds"
  vpc_id = module.vpc.vpc_id

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "education_rds"
  }
}

resource "aws_db_parameter_group" "mydb" {
  name   = "education"
  family = "postgres16"

  parameter {
    name  = "log_connections"
    value = "1"
  }
}

# Set in deploy time
# export TF_VAR_db_password="....."
variable "db_password" {
  description = "RDS root user password"
  sensitive   = true
}

resource "aws_db_instance" "education" {
  identifier             = "education"
  instance_class         = "db.t3.micro"
  allocated_storage      = 2
  engine                 = "postgres"
  engine_version         = "16.3"
  username               = "burger"
  password               = var.db_password
#   db_subnet_group_name   = aws_db_subnet_group.education.name
  db_subnet_group_name   = module.vpc.name
  vpc_security_group_ids = [aws_security_group.rds.id]
  parameter_group_name   = aws_db_parameter_group.mydb.name
  publicly_accessible    = true
  skip_final_snapshot    = true

  availability_zone = module.vpc.azs
  multi_az = true
}


## # EKS sera criado manualmente por enquanto.
#    O modulo tenta fazer operações com Roles que não são permitidas no aws academy. Investigar depois...
# module "eks" {
#   source  = "terraform-aws-modules/eks/aws"
#   version = "20.8.5"
#
#   cluster_name    = local.cluster_name
#   cluster_version = "1.29"
#
#   cluster_endpoint_public_access           = true
#   enable_cluster_creator_admin_permissions = true
#
#   create_iam_role = false
#   iam_role_name = "LabRole"
#
#   cluster_addons = {
# #     aws-ebs-csi-driver = {
# #       service_account_role_arn = module.irsa-ebs-csi.iam_role_arn
# #     }
#     coredns                = {}
#     eks-pod-identity-agent = {}
#     kube-proxy             = {}
#     vpc-cni                = {}
#   }
#
#   vpc_id     = module.vpc.vpc_id
#   subnet_ids = module.vpc.private_subnets
#
#   eks_managed_node_group_defaults = {
#     ami_type = "AL2_x86_64"
#   }
#
#   eks_managed_node_groups = {
#     one = {
#       name = "node-group-1"
#
#       create_iam_role = false
#       iam_role_name = "LabRole"
#
#       instance_types = ["t3.micro"]
#
#       min_size     = 3
#       max_size     = 5
#       desired_size = 3
#     }
# #
# #     two = {
# #       name = "node-group-2"
# #
# #       instance_types = ["t3.micro"]
# #
# #       min_size     = 2
# #       max_size     = 3
# #       desired_size = 2
# #     }
#   }
# }
#
#
# # https://aws.amazon.com/blogs/containers/amazon-ebs-csi-driver-is-now-generally-available-in-amazon-eks-add-ons/
# data "aws_iam_policy" "ebs_csi_policy" {
#   arn = "arn:aws:iam::aws:policy/service-role/AmazonEBSCSIDriverPolicy"
# }
#
# module "irsa-ebs-csi" {
#   source  = "terraform-aws-modules/iam/aws//modules/iam-assumable-role-with-oidc"
#   version = "5.39.0"
#
#   create_role                   = true
#   role_name                     = "AmazonEKSTFEBSCSIRole-${module.eks.cluster_name}"
#   provider_url                  = module.eks.oidc_provider
#   role_policy_arns              = [data.aws_iam_policy.ebs_csi_policy.arn]
#   oidc_fully_qualified_subjects = ["system:serviceaccount:kube-system:ebs-csi-controller-sa"]
# }