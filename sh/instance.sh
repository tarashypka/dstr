#! /bin/zsh

START_REPLICA_SH=/home/deoxys/mongodb/startreplica.sh

if [[ $1 == "start" ]]; then
    # Start mongod/tomcat instance[s]
    for arg in ${@:2}; do
        if [[ $arg =~ "mongosvr[1-3]" || $arg =~ "tomcatsvr[1-3]" ]]; then
            if ! ping -c 1 -W 1 $arg > /dev/null; then
                echo "You should start $arg first"
            elif [[ $arg =~ "mongosvr[1-3]" ]]; then
                export LC_ALL=C
                if ssh $arg $START_REPLICA_SH; then
                    echo "Successfully started mongod on $arg"
                fi
            elif [[ $arg =~ "tomcatsvr[1-3]" ]]; then
                if ssh $arg sudo /bin/systemctl start tomcat; then
                    echo "Successfully started tomcat on $arg"
                fi
            fi
        else
            echo "Unknown arg: $arg"
        fi
    done
elif [[ $1 == "stop" ]]; then
    # Stop mongod/tomcat instance[s]
    for arg in ${@:2}; do
        if [[ $arg =~ "mongosvr[1-3]" || $arg =~ "tomcatsvr[1-3]" ]]; then
            if ! ping -c 1 $arg > /dev/null; then
                echo "You should start $arg first"
            elif [[ $arg =~ "mongosvr[1-3]" ]]; then
                if ssh $arg pkill mongod; then
                    echo "Successfully stopped mongod on $arg"
                fi
            elif [[ $arg =~ "tomcatsvr[1-3]" ]]; then
                if ssh $arg sudo /bin/systemctl stop tomcat; then
                    echo "Successfully stopped tomcat on $arg"
                fi
            fi
        else
            echo "Unknown arg: $arg"
        fi
    done
else
    echo "Unknown arg: $1: should be 'start' or 'stop'"
fi
