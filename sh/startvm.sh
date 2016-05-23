#! /bin/zsh

# Start mongo slaves, that are virtual machines
# in private network with private IP addresses:
#
# Instances with 256Mb RAM, 4Gb HDD
# and mongodb v3.2.6 installed on em:
# 
# 192.168.56.111  mongosvr1
# 192.168.56.112  mongosvr2
# 192.168.56.113  mongosvr3
#
# Instances with 256Mb RAM, 4Gb HDD
# and Tomcat v8.0.33 installed on em:
#
# 192.168.56.121  tomcatsvr1
# 192.168.56.122  tomcatsvr2
# 192.168.56.123  tomcatsvr3
#
# Notes: 
# 1. Mappings for SSH is configured in /etc/hosts
# 2. JVMs on tomcatsvr[1,2,3] run with xms=64M, xmx=128M

vboxmanage startvm mongosvr1 --type headless
vboxmanage startvm mongosvr2 --type headless
vboxmanage startvm mongosvr3 --type headless

# Due to the CPU overload
sleep 30

vboxmanage startvm tomcatsvr1   --type headless
vboxmanage startvm tomcatsvr2   --type headless
vboxmanage startvm tomcatsvr3   --type headless
