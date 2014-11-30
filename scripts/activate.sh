#!/bin/bash

set -e

(
cd /home/gradient2go
./activator clean compile stage 2>&1 var/compile > var/compile
target/universal/stage/bin/gradient2go
PID=$!
echo $PID > var/pid
)
