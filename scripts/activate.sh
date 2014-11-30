#!/bin/bash

/home/gradient2go/activator start 2> /home/gradient2go/var/error > /home/gradient2go/var/log &
PID=$!
echo $PID > /home/gradient2go/var/pid
