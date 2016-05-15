#! /bin/zsh

# Generate war file from web directory
# and deploy it to running Tomcat instances
# on the servers mongosvr1 and mongosvr2

PRJ_NAME="dstr"
PRJ_HOME=/home/deoxys/Workspace/IdeaProjects/Java/$PRJ_NAME

# Generate war file
cd $PRJ_HOME/web
jar -cvf ../$PRJ_NAME.war *

# To write to mongosvr[1,2]:/opt/tomcat/webapps
# their permissions should be changed first:
# sudo chmod a+w /opt/tomcat/webapps

scp $PRJ_HOME/$PRJ_NAME.war mongosvr1:/opt/tomcat/webapps/
scp $PRJ_HOME/$PRJ_NAME.war mongosvr2:/opt/tomcat/webapps/
