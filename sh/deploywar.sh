#! /bin/zsh

# Generate war file from web directory
# and deploy it to running Tomcat instances
# on the servers tomcatsvr[1,2,3]

PRJ_NAME="dstr"
PRJ_HOME=/home/deoxys/Workspace/IdeaProjects/Java/$PRJ_NAME
WAR_FILE=$PRJ_HOME/target/$PRJ_NAME.war
TAG_FILE=$PRJ_HOME/web/WEB-INF/tags/genericpage.tag

cd $PRJ_HOME
for i (1 2 3); do
    find $TAG_FILE -type f -exec sed -i "s/localhost/tomcat$i (192.168.56.12$i)/g" {} \;

    # Generate war file
    mvn package

    # To write to tomcatsvr[1,2,3]:/opt/tomcat/webapps
    # write permissions should be changed first:
    # sudo chmod a+w /opt/tomcat/webapps

    scp $WAR_FILE tomcatsvr$i:/opt/tomcat/webapps/
    find $TAG_FILE -type f -exec sed -i "s/tomcat$i (192.168.56.12$i)/localhost/g" {} \;
done
