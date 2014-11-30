#!/bin/bash

/home/gradient2go/activator start &
PID=$!
echo $PID > /home/gradient2go/var/pid
