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

if [[ $1 == "start" ]]; then
    # Start mongosvr/tomcatsvr vbox machine
    if [[ $2 == "all" ]]; then
        for i in {1..3}; do
            vboxmanage startvm mongosvr$i --type headless
            vboxmanage startvm tomcatsvr$i --type headless
            sleep 10
        done
    else
        for arg in ${@:2}; do
            if [[ $arg =~ "mongosvr[1-3]" || $arg =~ "tomcatsvr[1-3]" ]]; then
                vboxmanage startvm $arg --type headless
            else
                echo "Unknown arg: $arg"
            fi
        done
    fi
elif [[ $1 == "stop" ]]; then
    # Stop mongosvr/tomcatsvr vbox machine
    if [[ $2 == "all" ]]; then
        for i in {1..3}; do
            if ! ping -c 1 -W 1 mongosvr$i > /dev/null; then
                echo "mongosvr$i is already stopped"
            else 
                if ssh mongosvr$i pkill mongod; then
                    echo "Successfully stopped mongod on mongosvr$i"
                fi
                sleep 3
                ssh mongosvr$i sudo /sbin/poweroff
            fi
            if ! ping -c 1 -W 1 tomcatsvr$i > /dev/null; then
                echo "tomcatsvr$i is already stopped"
            else 
                ssh tomcatsvr$i sudo /sbin/poweroff
            fi
        done
    else
        for arg in ${@:2}; do
            if [[ $arg =~ "mongosvr[1-3]" || $arg =~ "tomcatsvr[1-3]" ]]; then
                if ! ping -c 1 -W 1 $arg > /dev/null; then
                    echo "$arg is already stopped"
                elif [[ $arg =~ "mongosvr[1-3]" ]]; then
                    if ssh $arg pkill mongod; then
                        echo "Successfully stopped mongod on $arg"
                    fi
                    sleep 3
                    ssh $arg sudo /sbin/poweroff
                elif [[ $arg =~ "tomcatsvr[1-3]" ]]; then
                    if ssh $arg sudo /bin/systemctl stop tomcat; then
                        echo "Successfully stopped tomcat on $arg"
                    fi
                    sleep 3
                    ssh $arg sudo /sbin/poweroff
                fi
            else
                echo "Unknown arg: $arg"
            fi
        done
    fi
else
    echo "Unknown arg: $1: should be 'start' or 'stop'"
fi
