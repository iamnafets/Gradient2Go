#!/bin/bash

yum -y install java-1.8.0-openjdk-headless.x86_64
yum -y install java-1.8.0-openjdk-devel.x86_64
useradd gradient2go
mkdir /home/gradient2go
chown gradient2go:users /home/gradient2go
