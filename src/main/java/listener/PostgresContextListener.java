package listener; /**
 * Created by deoxys on 28.05.16.
 */

import org.postgresql.jdbc3.Jdbc3PoolingDataSource;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener()
public class PostgresContextListener implements ServletContextListener {

    // Public constructor is required by servlet spec
    public PostgresContextListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
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
            ex.printStackTrace();
        }
        System.out.println("Postgres connection pool: initialized successfully");
    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
    }
}