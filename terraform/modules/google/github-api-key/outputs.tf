output "uid" {
  description = "Unique id in UUID4 format."
  value       = google_apikeys_key.main.uid
}

output "key_string" {
  description = "An encrypted and signed value held by this key."
  value       = google_apikeys_key.main.key_string
  sensitive   = true
}
