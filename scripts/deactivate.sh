#!/bin/bash

echo "Killing softly..."
kill `cat /home/gradient2go/var/pid` &
sleep 10
echo "Killing not so softly..."
kill -9 `cat /home/gradient2go/var/pid` || echo "Already killed!"
