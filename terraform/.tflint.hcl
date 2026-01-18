plugin "google" {
  enabled = true
  version = "0.38.0"
  source  = "github.com/terraform-linters/tflint-ruleset-google"
}

plugin "terraform" {
  enabled = true
  version = "0.14.0"
  source  = "github.com/terraform-linters/tflint-ruleset-terraform"
}
