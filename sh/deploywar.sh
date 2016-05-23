#! /bin/zsh

# Generate war file from web directory
# and deploy it to running Tomcat instances
# on the servers tomcatsvr[1,2,3]

PRJ_NAME="dstr"
PRJ_HOME=/home/deoxys/Workspace/IdeaProjects/Java/$PRJ_NAME
WAR_FILE=$PRJ_HOME/target/$PRJ_NAME.war

# Generate war file
cd $PRJ_HOME
mvn package

# To write to tomcatsvr[1,2,3]:/opt/tomcat/webapps
# write permissions should be changed first:
# sudo chmod a+w /opt/tomcat/webapps

scp $WAR_FILE tomcatsvr1:/opt/tomcat/webapps/
scp $WAR_FILE tomcatsvr2:/opt/tomcat/webapps/
scp $WAR_FILE tomcatsvr3:/opt/tomcat/webapps/
