package com.deoxys.dev.dstr.presentation.listener;

/**
 * Created by deoxys on 17.05.16.
 */

import com.mongodb.*;
import org.apache.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;

@WebListener()
public class MongoContextListener implements ServletContextListener {
    final static Logger logger = Logger.getLogger(MongoContextListener.class);

    public MongoContextListener() { }

    public void contextInitialized(ServletContextEvent sce) {

        ServletContext ctx = sce.getServletContext();
        int hosts = Integer.parseInt(ctx.getInitParameter("MONGO_HOSTS"));

        List<ServerAddress> seeds = new ArrayList<>();
        for (int i = 1; i <= hosts; i++) {
            String host = ctx.getInitParameter("MONGO_HOST_" + i);
            int port = Integer.parseInt(ctx.getInitParameter("MONGO_PORT_" + i));
            seeds.add(new ServerAddress(host, port));
        }

        List<MongoCredential> auths = new ArrayList<>();
        String db = ctx.getInitParameter("MONGO_DB");
        String user = ctx.getInitParameter("MONGO_USER");
        String password = ctx.getInitParameter("MONGO_PASSWORD");
        auths.add(MongoCredential.createCredential(user, db, password.toCharArray()));

        MongoClient mongo = new MongoClient(seeds, auths);
        ctx.setAttribute("MONGO_CLIENT", mongo);
        logger.info("MongoClient: initialized successfully");
    }

    public void contextDestroyed(ServletContextEvent sce) {

        MongoClient mongo = (MongoClient) sce.getServletContext().
                getAttribute("MONGO_CLIENT");
        mongo.close();
        logger.info("MongoClient: closed successfully");
    }
}