package com.deoxys.dev.dstr.presentation.listener;

/**
 * Created by deoxys on 28.05.16.
 */

import org.apache.log4j.Logger;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener()
public class PostgresContextListener implements ServletContextListener {
    final static Logger logger = Logger.getLogger(PostgresContextListener.class);

    public PostgresContextListener() { }

    public void contextInitialized(ServletContextEvent sce) {

        ServletContext ctx = sce.getServletContext();
        int port = Integer.parseInt(ctx.getInitParameter("POSTGRES_PORT"));

        Jdbc3PoolingDataSource source = new Jdbc3PoolingDataSource();
        source.setServerName(ctx.getInitParameter("POSTGRES_HOST"));
        source.setPortNumber(port);
        source.setDatabaseName(ctx.getInitParameter("POSTGRES_DB"));
        source.setUser(ctx.getInitParameter("POSTGRES_USER"));
        source.setPassword(ctx.getInitParameter("POSTGRES_PASSWORD"));
        source.setMaxConnections(10);

        ctx.setAttribute("POSTGRES_CONNECTION_POOL", source);

        // Expand Postgres connection pool beyond Servlets via JNDI
        try {
            new InitialContext().rebind("POSTGRES_CONNECTION_POOL", source);
        } catch (NamingException ex) {
            logger.error("Error initializing POSTGRES_CONNECTION_POOL via Jndi");
            ex.printStackTrace();
        }
        logger.info("Postgres connection pool: initialized successfully");
    }

    public void contextDestroyed(ServletContextEvent sce) {

        Jdbc3PoolingDataSource source = (Jdbc3PoolingDataSource)
                sce.getServletContext().getAttribute("POSTGRES_CONNECTION_POOL");
        source.close();
        logger.info("Postgres connection pool: closed successfully");
    }
}