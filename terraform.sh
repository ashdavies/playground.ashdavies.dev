bash << EOF > terraform.log
./gradlew terraformPlan \
  --console=plain
EOF
