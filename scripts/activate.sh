#!/bin/bash

activator start &
PID=$!
echo $PID > /home/gradient2go/var/pid
