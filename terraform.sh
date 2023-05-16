# bash << EOF | tee terraform.log
bash << EOF > terraform.log
./gradlew terraformPlan \
  --no-configuration-cache \
  --console=plain \
  --dry-run \
  --quiet
EOF