# dstr
###### *Study project*

### Requirements

```
psql (PostgreSQL) v9.5.3
MongoDB v3.2.6  
Tomcat v8.0.33
httpd v2.4.20
JK v1.2.41
Maven v3.3.9
```
### PostgreSQL configuration

#### Configure postgres connection settings in web.xml

`/etc/hosts` could be used to configure DNS:
```
192.168.56.1	thinkpad
```

##### *default instance on localhost*
```
<context-param>
  	<param-name>POSTGRES_HOST</param-name>
  	<param-value>thinkpad</param-value>
</context-param>
<context-param>
   	<param-name>POSTGRES_PORT</param-name>
  	<param-value>5432</param-value>
</context-param>
```

#### Add required access in `pg_hba.conf`
```
local   all             dstrdbadmin                        md5
host    all             all             mongosvr1/24       trust
host    all             all             mongosvr2/24       trust
host    all             all             mongosvr3/24       trust
```

#### Create required schema, user and table
```
$ sudo -u postgres psql -d template1
template1=# CREATE DATABASE dstr;
template1=# CREATE USER dstrDbAdmin WITH PASSWORD '1234';
template1=# GRANT ALL PRIVILEGES ON DATABASE dstr to dstrdbadmin;
template1=# \q
$ psql -d dstr -U dstrdbadmin -W
template1=# INSERT INTO customers (name, surname, email, password, role)
			VALUES ('admin', 'admin', 'dstrdbadmin', '1234', 'admin');
```

### MongoDB configuration

#### Configure mongodb connection settings in web.xml

`/etc/hosts` could be used to configure DNS:
```
192.168.56.111  mongosvr1
192.168.56.112  mongosvr2
192.168.56.113  mongosvr3
```

##### *replica set on mongosvr[1,2,3]:27017*
```
<context-param>
	<param-name>MONGO_HOST_1</param-name>
	<param-value>mongosvr1</param-value>
</context-param>
<context-param>
	<param-name>MONGO_HOST_2</param-name>
	<param-value>mongosvr2</param-value>
</context-param>
<context-param>
	<param-name>MONGO_HOST_3</param-name>
	<param-value>mongosvr3</param-value>
</context-param>
<context-param>
	<param-name>MONGO_PORT</param-name>
	<param-value>27017</param-value>
</context-param>
```

#### Create required users

```
$ mongo admin  
> db.createUser( { user: "siteUserAdmin", pwd: "1234", roles: [ { role: "userAdminAnyDatabase", db: "admin" } ] } );  
> exit;  
$ mongo admin -u siteUserAdmin -p 1234  
> use dstr;
> db.createUser( { user: "dstrDbAdmin", pwd: "1234", roles: [ { role: "dbOwner", db: "dstr" } ] } );  
> exit;  
$ mongo dstr -u dstrDbAdmin -p 1234  
> db.users.insert( { "name" : "Taras", "email" : "tarashypka@gmail.com", "password" : "1234" } );  
```

#### Start replica set

```
$ mongod --keyFile /path/to/mongodb-keyfile --replSet "rs0"  
```

#### Connect to the remotely running mongod instance

```
$ mongo dstr --host <HOST> --port <PORT> -u dstrDbAdmin -p 1234  
```

### Configure Load Balancer for replication in Tomcat

#### Install and configure httpd & mod_jk

`mod_jk` plugin is connector used for httpd-to-workers communication via AJP (The Apache Tomcat Connectors) protocol.  
So 3 workers (tomcat1, tomcat2, tomcat3) were configured in `conf/workers.properties`.  
Copy configurations files `conf/{workers.properties,httpd.conf}` to `/path/to/httpd/conf/`.  

#### Enable affinity/sticky sessions

Set `jvmRoute` for all Tomcat instances in `/path/to/tomcat/conf/server.xml`:  
```
<Engine name="Catalina" defaultHost="localhost" jvmRoute="tomcat#">
```

All sessionIDs will be appended with worker names, so load balancer will know to where redirect each session:  
```
2F61B573F11635E01B17349776F825C4 --> 2F61B573F11635E01B17349776F825C4.tomcat1
```

#### Start Apache Http Server with configured Load Balancer

```
$ sudo /path/to/apache/bin/apachectl start
```

### Distributed cache with Hazelcast

#### Configure Hz connection settings in web.xml

`/etc/hosts` could be used to configure DNS:
```
192.168.56.121  tomcatsvr1
192.168.56.122  tomcatsvr2
192.168.56.123  tomcatsvr3
```

Similarly to as for mongodb:  

##### *Hz configs on tomcatsvr[1,2,3]:5701*
```
<context-param>
	<param-name>HZ_HOST_1</param-name>
	<param-value>tomcatsvr1</param-value>
</context-param>
<context-param>
	<param-name>HZ_HOST_2</param-name>
	<param-value>tomcatsvr2</param-value>
</context-param>
<context-param>
	<param-name>HZ_HOST_3</param-name>
	<param-value>tomcatsvr3</param-value>
</context-param>
<context-param>
	<param-name>HZ_PORT</param-name>
	<param-value>5701</param-value>
</context-param>
<context-param>
```

#### Allow Hz communication between application servers

All applications are configured with Hz, that uses TCP-IP network.  
In Ubuntu, UFW (Uncomplicated Firewall) could be used to open the required port:
```
sudo ufw enable
sudo ufw allow 5701
sudo ufw allow 8009
```

### References