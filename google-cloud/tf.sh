#!/bin/bash

DIR="$(dirname -- "$0")"
$DIR/build/terraform/terraform* -chdir=$DIR/build/terraform/main/runtimeExecution $@
