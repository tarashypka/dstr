package com.deoxys.dev.dstr.persistence;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import javax.naming.*;
import javax.naming.spi.ObjectFactory;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by deoxys on 08.07.16.
 */

public class CustomMongoJndiFactory implements ObjectFactory {

    @Override
    public Object getObjectInstance(Object obj,
                                    Name name,
                                    Context nameCtx,
                                    Hashtable<?, ?> environment) throws Exception {

        validateProperty(obj, "Invalid JNDI object reference");

        String dbName = null;
        String host = null;
        String username = null;
        String password = null;
        int port = 27017;

        Reference ref = (Reference) obj;
        Enumeration<RefAddr> props = ref.getAll();
        while (props.hasMoreElements()) {
            RefAddr addr = props.nextElement();
            String propName = addr.getType();
            String propValue = (String) addr.getContent();
            if (propName.equals("dbName")) {
                dbName = propValue;
                validateProperty(dbName, "Invalid or empty mongo database name");
            } else if (propName.equals("host")) {
                host = propValue;
                validateProperty(host, "Invalid or empty mongo host");
            } else if (propName.equals("username")) {
                username = propValue;
                validateProperty(username, "Invalid or empty mongo username");
            } else if (propName.equals("password")) {
                password = propValue;
                validateProperty(password, "Invalid or empty mongo password");
            } else if (name.equals("port")) {
                try {
                    port = Integer.parseInt(propValue);
                } catch (NumberFormatException e) {
                    throw new NamingException("Invalid port value " + propValue);
                }
            }
        }

        List<ServerAddress> seeds = new ArrayList<>();
        seeds.add(new ServerAddress(host, port));

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
