package listener; /**
 * Created by deoxys on 17.05.16.
 */

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;

@WebListener()
public class MongoContextListener implements ServletContextListener {

    // Public constructor is required by servlet spec
    public MongoContextListener() {
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
        int hosts = Integer.parseInt(ctx.getInitParameter("MONGO_HOSTS"));
        int port = Integer.parseInt(ctx.getInitParameter("MONGO_PORT"));

        List<ServerAddress> seeds = new ArrayList<>();
        for (int i = 1; i <= hosts; i++)
            seeds.add(new ServerAddress(ctx.getInitParameter("MONGO_HOST_" + i), port));

        List<MongoCredential> auths = new ArrayList<>();
        String db = ctx.getInitParameter("MONGO_DB");
        String user = ctx.getInitParameter("MONGO_USER");
        String pswd = ctx.getInitParameter("MONGO_PASSWORD");
        auths.add(MongoCredential.createCredential(user, db, pswd.toCharArray()));

        MongoClient mongoClient = new MongoClient(seeds, auths);
        sce.getServletContext().setAttribute("MONGO_CLIENT", mongoClient);
        System.out.println("MongoClient: initialized successfully");
    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
        MongoClient mongoClient = (MongoClient) sce.getServletContext().
                getAttribute("MONGO_CLIENT");
        mongoClient.close();
        System.out.println("MongoClient: closed successfully");
    }
}