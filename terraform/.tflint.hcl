plugin "google" {
  enabled = true
  version = "0.35.0"
  source  = "github.com/terraform-linters/tflint-ruleset-google"
}

plugin "terraform" {
  enabled = true
  version = "0.13.0"
  source  = "github.com/terraform-linters/tflint-ruleset-terraform"
}
