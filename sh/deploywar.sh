#! /bin/zsh

# Generate project war file with Maven
# and deploy it to running Tomcat instances
# on the servers tomcatsvr[1,2,3]

# Two solutions were presented:
#   using unix ssh-based scp command    pretty fast (< 30sec)
#   using tomcat7-maven-plugin          enough slow (> 1min)

PRJ_NAME="dstr"
PRJ_HOME=/home/deoxys/Workspace/IdeaProjects/Java/$PRJ_NAME
WAR_FILE=$PRJ_HOME/target/$PRJ_NAME.war
TAG_FILE=$PRJ_HOME/web/WEB-INF/tags/genericpage.tag
POM_FILE=$PRJ_HOME/pom.xml

cd $PRJ_HOME
for i (1 2 3); do
    # Change footer info for one of Tomcat servers
    find $TAG_FILE -type f -exec sed -i "s/localhost/tomcat$i (192.168.56.12$i)/g" {} \;
    
    # Change mvn deployment destination
    # sed -i "s/localhost/tomcatsvr$i/g" $POM_FILE

    # Deploy to one of Tomcat servers
    # mvn tomcat7:redeploy

    # Revert to initial destination
    # sed -i "s/tomcatsvr$i/localhost/g" $POM_FILE

    # Generate war file
    mvn package

    # To write to tomcatsvr$i:/opt/tomcat/webapps
    # write permissions should be changed first:
    # sudo chmod a+w /opt/tomcat/webapps

    # Secure copy generated war to one of Tomcat servers
    scp $WAR_FILE tomcatsvr$i:/opt/tomcat/webapps

    # Revert to initial footer info
    find $TAG_FILE -type f -exec sed -i "s/tomcat$i (192.168.56.12$i)/localhost/g" {} \;
done
