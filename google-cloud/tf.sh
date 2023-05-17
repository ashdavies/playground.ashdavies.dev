#!/bin/bash
TF_VAR_gh_token=$(gh auth token)
DIR=$(dirname -- "$0")
PATH=$(realpath $DIR)

/bin/ln -sf ${PATH}/build/terraform/main/data ${PATH}/build/terraform/main/runtimeExecution/.terraform
$DIR/build/terraform/terraform* -chdir=$DIR/build/terraform/main/runtimeExecution $@
 