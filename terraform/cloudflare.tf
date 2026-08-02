resource "cloudflare_dns_record" "playground_ashdavies_dev" {
  zone_id = "9027dc91233d4a1438d08b50760f8856"
  name    = "playground.ashdavies.dev"
  ttl     = 3600
  type    = "CNAME"
}
