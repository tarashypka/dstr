#! /bin/zsh

# Drawbacks:
#   performance issue: 
#       if --deploy --names=all then mvn will build project 3 times
#   command order issue: 
#       --names=all --deploy leads to unintuitive behavior
#       --deploy --names=all leads to correct behavior
#   error messages issue: 
#       wrong error messages on --[start,stop] 
#       when tomcat/mongod instance is already started/stopped

USR_NAME="deoxys"
PRJ_NAME="dstr"
PRJ_HOME="/home/$USR_NAME/Workspace/IdeaProjects/Java/$PRJ_NAME"
WAR_FILE="$PRJ_HOME/target/$PRJ_NAME.war"

TOMCAT1_LOCAL_CATALINA_BASE="/opt/tomcat1"
TOMCAT1_LOCAL_PORT=8081
TOMCAT2_LOCAL_CATALINA_BASE="/opt/tomcat2"
TOMCAT2_LOCAL_PORT=8082
TOMCAT3_LOCAL_CATALINA_BASE="/opt/tomcat3"
TOMCAT3_LOCAL_PORT=8083
TOMCAT_LOCAL_CATALINA_HOME="/opt/tomcat"

TOMCAT1_REMOTE_HOST=192.168.56.121
TOMCAT2_REMOTE_HOST=192.168.56.122
TOMCAT3_REMOTE_HOST=192.168.56.123
TOMCAT_REMOTE_CATALINA_HOME="/opt/tomcat"
TOMCAT_REMOTE_PORT=8080

MONGO1_LOCAL_PORT=27017
MONGO1_LOCAL_CONFIG="/home/$USR_NAME/Workspace/mongodb/dstr/mongod_1.conf"
MONGO2_LOCAL_PORT=27018
MONGO2_LOCAL_CONFIG="/home/$USR_NAME/Workspace/mongodb/dstr/mongod_2.conf"
MONGO3_LOCAL_PORT=27019
MONGO3_LOCAL_CONFIG="/home/$USR_NAME/Workspace/mongodb/dstr/mongod_3.conf"

MONGO1_REMOTE_HOST=192.168.56.111
MONGO2_REMOTE_HOST=192.168.56.112
MONGO3_REMOTE_HOST=192.168.56.113
MONGO_REMOTE_DB_PATH="/home/$USR_NAME/tmp/mongodb"
MONGO_REMOTE_PORT=27017

USAGE_ERR_MSG="Usage:\n\
\tdstr --[start,stop] --[local,remote,vm] --names=[tomcat[1,2,3],mongo[1,2,3],all]\n\
\tdstr --deploy --[local,remote] --names=[tomcat[1,2,3],all]"

usage_err() {
    echo $USAGE_ERR_MSG
    exit 1
}

start=false;
stop=false;
deploy=false;
local=false;
remote=false;
vm=false;
tomcats=();
mongos=();

for arg in $@; do
    case $arg in
        --start)
            if [[ $stop == true || $deploy == true ]]; then
                usage_err
            fi
            start=true
            ;;
        --stop)
            if [[ $start == true || $deploy == true ]]; then
                usage_err
            fi
            stop=true
            ;;
        --deploy)
            if [[ $start == true || $stop == true || $vm == true ]]; then
                usage_err
            fi
            deploy=true
            ;;
        --local)
            if [[ $remote == true || $vm == true ]]; then
                usage_err
            fi
            local=true
            ;;
        --remote)
            if [[ $local == true || $vm == true ]]; then
                usage_err
            fi
            remote=true
            ;;
        --vm)
            if [[ $local == true || $remote == true || $deploy == true ]]; then
                usage_err
            fi
            vm=true
            ;;
        --names=*)
			for name in "${(s/,/)arg: +8}"; do
				if [[ $name == tomcat[1-3] ]]; then
					tomcats+=($name)
				elif [[ $name == mongo[1-3] ]]; then
					mongos+=($name)
                elif [[ $name == all ]]; then
                    tomcats+=(tomcat1 tomcat2 tomcat3)
                    if [[ $deploy == false ]]; then
                        mongos+=(mongo1 mongo2 mongo3)
                    fi
				fi
			done
            ;;
        *)
            usage_err
            ;;
    esac
done

if [[ ${#tomcats[@]} == 0 && ${#mongos[@]} == 0 ]]; then
    usage_err
elif [[ $local == false && $remote == false && $vm == false ]]; then
    usage_err
elif [[ $start == false && $stop == false ]]; then
    if [[ $deploy == false || ${#mongos[@]} != 0 ]]; then
        usage_err
	fi
fi

# in global scope are [local,remote] [start,stop,deploy]

manage_vm() {
    if [[ $start == true ]]; then
        if vboxmanage startvm $1 --type headless &> /dev/null; then
            echo "Success: virtual machine $1 was started"
        else
            echo "Error: virtual machine $1 was not started"
        fi
    else
        if vboxmanage controlvm $1 poweroff &> /dev/null; then
		    echo "Success: virtual machine $1 was stopped"
        else
		    echo "Error: virtual machine $1 was not stopped"
        fi
    fi
}

manage_tomcat() {
    i=${1: -1}
    if [[ $local == true ]]; then
        CATALINA_HOME=$TOMCAT_LOCAL_CATALINA_HOME
        case $i in
            1)
                export CATALINA_BASE=$TOMCAT1_LOCAL_CATALINA_BASE
                port=$TOMCAT1_LOCAL_PORT
                ;;
            2)
                export CATALINA_BASE=$TOMCAT2_LOCAL_CATALINA_BASE
                port=$TOMCAT2_LOCAL_PORT
                ;;
            3)
                export CATALINA_BASE=$TOMCAT3_LOCAL_CATALINA_BASE
                port=$TOMCAT3_LOCAL_PORT
                ;;
        esac
        if [[ $deploy == true ]]; then
            rm $WAR_FILE
            cd $PRJ_HOME
            # Bad performance on --deploy if tomcats length > 1
            # Project will be builded several times
            if mvn package &> /dev/null; then
                echo "Success: mvn generated $WAR_FILE"
                cp $WAR_FILE $CATALINA_BASE/webapps
                echo "Success: war-file was deployed to $1 (localhost:$port)"
            else
                echo "Error: mvn didn't generate $WAR_FILE"
                echo "Error: war-file was not deployed to $1 (localhost:$port)"
            fi
        elif [[ $start == true ]]; then
            if $CATALINA_HOME/bin/startup.sh $1 &> /dev/null; then
                echo "Success: tomcat on localhost:$port was started ($1)"
            else
                echo "Error: tomcat on localhost:$port was not started ($1)"
            fi
        else
            if $CATALINA_HOME/bin/shutdown.sh $1 &> /dev/null; then
                echo "Success: tomcat on localhost:$port was stopped ($1)"
            else
                echo "Error: tomcat on localhost:$port was not stopped ($1)"
            fi
        fi
    else
        port=$TOMCAT_REMOTE_PORT
        case $i in
            1)
                host=$TOMCAT1_REMOTE_HOST
                ;;
            2)
                host=$TOMCAT2_REMOTE_HOST
                ;;
            3)
                host=$TOMCAT3_REMOTE_HOST
                ;;
        esac
        CATALINA_HOME=$TOMCAT_REMOTE_CATALINA_HOME
        if ! ping -c 1 -W 1 $host > /dev/null; then
            echo "Error: you should start $1 ($host) first"
        elif [[ $deploy == true ]]; then
            rm $WAR_FILE
            cd $PRJ_HOME
            if mvn package &> /dev/null; then
                echo "Success: mvn generated $WAR_FILE"
            else
                echo "Error: mvn didn't generate $WAR_FILE"
            fi
            if scp $WAR_FILE "$host:$CATALINA_HOME/webapps"; then
                echo "Success: war-file was deployed to $1 ($host:$port)"
            else
                echo "Error: war-file was not deployed to $1 ($host:$port)"
            fi
        elif [[ $start == true ]]; then
            if ssh $host $CATALINA_HOME/bin/startup.sh; then
                echo "Success: tomcat on $host:$port was started ($1)"
            else
                echo "Error: tomcat on $host:$port was not started ($1)"
            fi
        else
            if ssh $host $CATALINA_HOME/bin/shutdown.sh; then
                echo "Success: tomcat on $host:$port was stopped ($1)"
            else
                echo "Error: tomcat on $host:$port was not stopped ($1)"
            fi
        fi
    fi
}

manage_mongo() {
	i=${1: -1}
	if [[ $local == true ]]; then
		case $i in
			1)
                port=$MONGO1_LOCAL_PORT
                config=$MONGO1_LOCAL_CONFIG
				;;
			2)
                port=$MONGO2_LOCAL_PORT
                config=$MONGO2_LOCAL_CONFIG
				;;
			3)
                port=$MONGO3_LOCAL_PORT
                config=$MONGO3_LOCAL_CONFIG
				;;
		esac
		if [[ $start == true ]]; then
            started="mongod --config $config --smallfiles &> /dev/null &"
            export LC_ALL=C
			if eval $started; then
                echo "Success: mongod on localhost:$port was started ($1)"
            else
                echo "Error: mongod on localhost:$port was not started ($1)"
            fi
		else
            stopped="mongod --config $config --shutdown &> /dev/null &"
			if eval $stopped; then
                echo "Success: mongod on localhost:$port was stopped ($1)"
            else
                echo "Error: mongod on localhost:$port was not stopped ($1)"
            fi
		fi
	else
        port=$MONGO_REMOTE_PORT
        dbpath=$MONGO_REMOTE_DB_PATH
		case $i in
			1)
				host=$MONGO1_REMOTE_HOST
				;;
			2)
				host=$MONGO2_REMOTE_HOST
				;;
			3)
				host=$MONGO3_REMOTE_HOST
				;;
		esac
        if ! ping -c 1 -W 1 $host > /dev/null; then
            echo "Error: you should start $1 ($host) first"
        elif [[ $start == true ]]; then
            started="mongod \
                --port $port \
                --dbpath $dbpath --replSet \"rs0\" --smallfiles &> /dev/null &"
            export LC_ALL=C
            if eval ssh $host $started; then
                echo "Success: mongod on $host:$port was started ($1)"
            else
                echo "Error: mongod on $host:$port was not started ($1)"
            fi
        elif [[ $stop == true ]]; then
            stopped="mongod \
                --port $port \
                --dbpath $dbpath --replSet \"rs0\" --shutdown &> /dev/null &"
            if eval ssh $host $stopped; then
                echo "Success: mongod on $host:$port was stopped ($1)"
            else
                echo "Error: mongod on $host:$port was not stopped ($1)"
            fi
		fi
	fi
}

if [[ $vm == true ]]; then
	for i in $tomcats $mongos; do
        manage_vm $i
    done
else
	for t in $tomcats; do
        manage_tomcat $t
    done
	for m in $mongos; do
        manage_mongo $m
    done
fi
