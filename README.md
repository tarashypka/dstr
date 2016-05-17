# dstr
Study project  

### Create required users
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

### Start replicas
```
$ mongod --keyFile /home/deoxys/mongodb/mongodb-keyfile --replSet "rs0"  
```

### Connect to the remotely running mongod instance
```
$ mongo dstr --host 192.168.56.111 --port 27017 -u dstrDbAdmin -p 1234  
```
