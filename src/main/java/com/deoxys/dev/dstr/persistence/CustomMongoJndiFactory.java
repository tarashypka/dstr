package com.deoxys.dev.dstr.persistence;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import javax.naming.*;
import javax.naming.spi.ObjectFactory;
import java.util.*;

public class CustomMongoJndiFactory implements ObjectFactory {

    @Override
    public Object getObjectInstance(Object obj,
                                    Name name,
                                    Context nameCtx,
                                    Hashtable<?, ?> environment) throws Exception {

        validateProperty(obj, "Invalid JNDI object reference");

        Reference ref = (Reference) obj;
        Enumeration<RefAddr> addresses = ref.getAll();

        String dbName = null;
        String host = "localhost";      // default host
        String username = null;
        String password = null;
        int port = 27017;               // default port

        Map<String, String> props = new HashMap<>();
        while (addresses.hasMoreElements()) {
            RefAddr addr = addresses.nextElement();
            String propName = addr.getType();
            String propValue =  (String) addr.getContent();

            if (propName.equals("dbName")) {
                dbName = propValue;
                validateProperty(dbName, "Invalid or empty mongo database name");
            } else if (propName.equals("username")) {
                username = propValue;
                validateProperty(username, "Invalid or empty mongo username");
            } else if (propName.equals("password")) {
                password = propValue;
                validateProperty(password, "Invalid or empty mongo password");
            } else if (propName.equals("host")) {
                host = propValue;
                validateProperty(host, "Invalid or empty mongo host");
            } else if (propName.equals("port"))
                port = Integer.parseInt(propValue);
            else props.put(propName, propValue);
        }

        if (username == null) throw new NameNotFoundException("username");
        else if (password == null) throw new NameNotFoundException("password");
        else if (dbName == null) throw new NameNotFoundException("dbName");

        List<ServerAddress> seeds = new ArrayList<>();

        // Host, port values if set, than with names "host_1", "port_1", ...
        String host_, port_;
        for (int i = 1; ; i++) {
            if ((host_ = props.get("host_" + i)) != null && ! host_.trim().equals("")) {
                if ((port_ = props.get("port_" + i)) != null)
                    seeds.add(new ServerAddress(host_, Integer.parseInt(port_)));
                else throw new NameNotFoundException("port_" + i);
            } else break;
        }

        // Add default host and port if there are no
        if (seeds.isEmpty()) seeds.add(new ServerAddress(host , port));

        List<MongoCredential> auths = new ArrayList<>();
        auths.add(MongoCredential.createCredential(username, dbName, password.toCharArray()));

        return new MongoClient(seeds, auths);
    }

    private void validateProperty(String property, String errorMessage)
            throws NamingException {

        if (property == null || property.trim().equals("")) {
            throw new NamingException(errorMessage);
        }
    }

    private void validateProperty(Object property, String errorMessage)
            throws NamingException {

        if (property == null) {
            throw new NamingException(errorMessage);
        }
    }
}
