#!/bin/bash

(
cd /home/gradient2go
./activator start 2> var/error > var/log &
PID=$!
echo $PID > var/pid
)
