#!/bin/bash

set -e

(
cd /home/gradient2go
echo "Killing softly..." >> var/deactivate
kill `cat /home/gradient2go/var/pid` &
sleep 10
echo "Killing not so softly..." >> var/deactivate
kill -9 `cat /home/gradient2go/var/pid` || echo "Already killed!" >> var/deactivate
)
