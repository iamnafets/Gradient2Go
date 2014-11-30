#!/bin/bash

set -e

(
cd /home/gradient2go
./activator clean compile stage &> var/compile
target/universal/stage/bin/gradient2go
PID=$!
echo $PID > var/pid
)
