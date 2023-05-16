bash << EOF > terraform.log
./gradlew terraformPlan \
  --no-configuration-cache \
  --console=plain
EOF