# dstr
###### *Study project*


### Description

Online store.

Main entities: User (Customer, Admin), Order, Item.

Customer can make, reject Order.
By making new Order, Customer reserves Items, that are grouped by Categories.

Admin can add new Items, change Order status (Rejected, In process, Processed), ban Customer.

System is supposed to manage large amount of data (to be scalable).

Polyglot persistence:  
Customers (relational data) are stored in PostgreSQL.  
Orders and Items (non-relational data) are stored in MongoDB.

MongoDB is pre-configured with replica of 3 shards.  
SlaveOk is configured, so Customer can read (but not write) data only with 1 shard alive.  

Tomcat is pre-configured with Load Balancer redirecting to 1 of 3 instances.  
Customer session is saved in distributed in-memory cache (Hazelcast).  
Sticky session and session replication against loss of Customer's sensitive data.


### Software and Tools used

```
Java v.1.8.0_101
PostgreSQL v9.5.4
MongoDB v3.2.9
Hazelcast Open Source v3.6.2
Apache HTTP Server (httpd) v2.4.20
Apache Tomcat v8.0.33
Apache Tomcat Connector (mod_jk) v1.2.41
Apache Maven v3.3.9
Intellij IDEA Ultimate 2016.1.4
Ubuntu v16.04
```


### Tomcat configuration

Configure server, users, JNDI resources in `../dstrconf/tomcat/` files.

Replace local tomcat configuration files with those in `../dstr/conf/tomcat/`.

To load Mongo and Postgres connection pools into Tomcat Container via JNDI,
Tomcat should be pre-configured with `-XX:MaxPermSize=???M` property,
where `??? >= 128`.


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
local   dstr            dstradmin                           md5
local   testdstr        testdstradmin                       md5

host    dstr            dstradmin           mongosvr1/24    md5
host    dstr            dstradmin           mongosvr2/24    md5
host    dstr            dstradmin           mongosvr3/24    md5
```

#### Create required schemas, users and tables
```
$ sudo -u postgres psql -d template1
template1=# CREATE DATABASE dstr;
template1=# CREATE DATABASE testdstr;
template1=# CREATE USER dstradmin WITH PASSWORD '1234';
template1=# CREATE USER testdstradmin WITH PASSWORD '1234';
template1=# GRANT ALL PRIVILEGES ON DATABASE dstr to dstradmin;
template1=# GRANT ALL PRIVILEGES ON DATABASE testdstr to testdstradmin;
template1=# \q
$ psql -d dstr -U dstradmin -a -f ../dstr/conf/postgres/users-create.sql
$ psql -d dstr -U dstradmin -a -f ../dstr/conf/postgres/customers-create.sql
$ psql -d dstr -U dstradmin -a -f ../dstr/conf/postgres/admin-insert.sql
$ psql -d testdstr -U testdstradmin -a -f ../dstr/conf/postgres/users-create.sql
$ psql -d testdstr -U testdstradmin -a -f ../dstr/conf/postgres/customers-create.sql
```
To use schemas manually user can connect with
```
$ psql -d dstr -U dstradmin -h thinkpad -p 5432 -W
```


### MongoDB configuration

#### Configure mongodb connection settings in web.xml

`/etc/hosts` could be used to configure DNS
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
    <param-name>MONGO_PORT_1</param-name>
    <param-value>27017</param-value>
</context-param>
<context-param>
    <param-name>MONGO_HOST_2</param-name>
    <param-value>mongosvr2</param-value>
</context-param>
<context-param>
    <param-name>MONGO_PORT_2</param-name>
    <param-value>27017</param-value>
</context-param>
<context-param>
    <param-name>MONGO_HOST_3</param-name>
    <param-value>mongosvr3</param-value>
</context-param>
<context-param>
    <param-name>MONGO_PORT_3</param-name>
    <param-value>27017</param-value>
</context-param>
```

#### Configure replica

Refer to `conf/mongodb/mongod.conf` for custom mongod configuration.

##### Start all replica members

```
$ mongod --config /path/to/mongod_1.conf
$ mongod --config /path/to/mongod_2.conf
$ mongod --config /path/to/mongod_3.conf
```

##### Connect and configure replica set

```
$ mongo --host <MONGO_HOST_1>
> rs.initiate();
> rs.add("<MONGO_HOST_2>:<MONGO_PORT_2>");
> rs.add("<MONGO_HOST_3>:<MONGO_PORT_3>");
> exit;
```

##### Create required users

```
$ mongo admin --host <MONGO_HOST_1>
> db.createUser( { customer: "siteUserAdmin", pwd: "1234", roles: [ { role: "userAdminAnyDatabase", db: "admin" } ] } );
> exit;  
$ mongo admin --host <MONGO_HOST_1> -u siteUserAdmin -p 1234
> use dstr;
> db.createUser( { customer: "dstrAdmin", pwd: "1234", roles: [ { role: "dbOwner", db: "dstr" } ] } );
> db.createUser( { customer: "testdstrAdmin", pwd: "1234", roles: [ { role: "dbOwner", db: "testdstr" } ] } );
> exit;
```

Connect to configured replica set with

```
$ mongo --host dstrs/<MONGO_HOST_1>:<MONGO_PORT_1>,<MONGO_HOST_1>:<MONGO_PORT_1>,<MONGO_HOST_1>:<MONGO_PORT_1> dstr
```

##### Add specific objects for orders collection
```
$ mongo dstr --host <MONGO_HOST_1> -u dstrAdmin -p 1234 < ../dstr/conf/orders-number-seq.js
$ mongo dstr --host <MONGO_HOST_1> -u dstrAdmin -p 1234 < ../dstr/conf/orders-autoremove.js
$ mongo testdstr --host <MONGO_HOST_1> -u testdstrAdmin -p 1234 < ../dstr/conf/orders-number-seq.js
$ mongo testdstr --host <MONGO_HOST_1> -u testdstrAdmin -p 1234 < ../dstr/conf/orders-autoremove.js
```

To configure replica set running on different machines refer to
[Enforce Keyfile Access Controll In Existing Replica Set](https://docs.mongodb.com/manual/tutorial/enforce-keyfile-access-control-in-existing-replica-set/).

### Configure Load Balancer for replication in Tomcat

#### Install and configure httpd & mod_jk

`mod_jk` plugin is connector used for httpd-to-workers communication via AJP (The Apache Tomcat Connectors) protocol.  

3 workers (tomcat1, tomcat2, tomcat3) were configured in `../dstr/conf/workers.properties`.

Copy configurations files `../dstr/conf/{workers.properties,httpd.conf}` to `/path/to/httpd/conf/`.

#### Enable affinity/sticky sessions

Set `jvmRoute` for all Tomcat instances in `/path/to/tomcat/conf/server.xml`:  
```
<Engine name="Catalina" defaultHost="localhost" jvmRoute="tomcat#">
```

All sessionIDs will be appended with worker names and load balancer will know where to redirect each session:
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

##### Hazelcast on tomcatsvr[1,2,3]:5701
```
<context-param>
    <param-name>HZ_HOST_1</param-name>
    <param-value>tomcatsvr1</param-value>
</context-param>
<context-param>
    <param-name>HZ_PORT_2</param-name>
    <param-value>5701</param-value>
</context-param>
<context-param>
    <param-name>HZ_HOST_2</param-name>
    <param-value>tomcatsvr2</param-value>
</context-param>
<context-param>
    <param-name>HZ_PORT_2</param-name>
    <param-value>5701</param-value>
</context-param>
<context-param>
    <param-name>HZ_HOST_3</param-name>
    <param-value>tomcatsvr3</param-value>
</context-param>
<context-param>
    <param-name>HZ_PORT</param-name>
    <param-value>5701</param-value>
</context-param>
```

#### Allow Hz communication between application servers

All applications are configured with Hazelcast, that uses TCP-IP network.
In Ubuntu, UFW (Uncomplicated Firewall) could be used to open the communication ports
```
sudo ufw enable
sudo ufw allow 5701
sudo ufw allow 8009
```


### References
