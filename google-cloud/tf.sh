#!/bin/bash

DIR="$(dirname -- "$0")"
$DIR/build/terraform/terraform_1.3.1 -chdir=$DIR/build/terraform/main/runtimeExecution $@
