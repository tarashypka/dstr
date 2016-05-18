# dstr
#### *Study project*

#### Software used
```
MongoDB v3.2.6  
Tomcat v8.0.33  
IntelliJ IDEA ULTIMATE v14.0.2  
```

#### Configure mongodb connection settings in web.xml

###### *1 instance on localhost:27017*
```
<context-param>
	<param-name>MONGO_HOSTS</param-name>
	<param-value>1</param-value>
</context-param>
<context-param>
	<param-name>MONGO_HOST_1</param-name>
	<param-value>localhost</param-value>
</context-param>
```
###### *replica set on localhost:[27017,27018,27018]*
```
<context-param>
	<param-name>MONGO_HOST_1</param-name>
	<param-value>locahost</param-value>
</context-param>
<context-param>
	<param-name>MONGO_HOST_2</param-name>
	<param-value>localhost</param-value>
</context-param>
<context-param>
	<param-name>MONGO_HOST_3</param-name>
	<param-value>localhost</param-value>
</context-param>
<context-param>
	<param-name>MONGO_PORT_1</param-name>
	<param-value>27017</param-value>
</context-param>
<context-param>
	<param-name>MONGO_PORT_2</param-name>
	<param-value>27018</param-value>
</context-param>
<context-param>
	<param-name>MONGO_PORT_3</param-name>
	<param-value>27019</param-value>
</context-param>
```

#### Create required users
```
$ mongo admin  
> db.createUser( { user: "siteUserAdmin", pwd: "1234", roles: [ { role: "userAdminAnyDatabase", db: "admin" } ] } );  
> exit;  
$ mongo admin -u siteUserAdmin -p 1234  
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

#### Test distributed in-memory cache
To refresh session without changing its attributes the path `ContextRoot/users.jsp` could be used.  
