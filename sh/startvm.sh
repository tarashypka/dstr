#!/bin/zsh

# Start mongo slaves, that are virtual machines
# in private network with private IP addresses:
#
# All instances are 256Mb RAM, 4Gb HDD
# 
# 192.168.56.111  mongoshard1 (with mongodb v3.2.6 installed)
# 192.168.56.112  mongoshard2 (with mongodb v3.2.6 installed)
# 192.168.56.113  mongoshard3 (with mongodb v3.2.6 installed)
# 192.168.56.114  mongoshard3 (with mongodb v3.0.4 installed)
# 192.168.56.115  mongoshard3 (with mongodb v3.0.4 installed)
#
# 192.168.56.121  mongosvr1
# 192.168.56.122  mongosvr2
#
# JVMs on mongosvr[1,2] run with xms=64M, xmx=128M
#
# Note: mapping for SSH is configured in /etc/hosts

vboxmanage startvm mongoshard1 --type headless
vboxmanage startvm mongoshard2 --type headless
vboxmanage startvm mongoshard3 --type headless
# vboxmanage startvm mongoshard4 --type headless
# vboxmanage startvm mongosvr1   --type headless
# vboxmanage startvm mongosvr2   --type headless
