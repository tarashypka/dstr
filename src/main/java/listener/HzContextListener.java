package listener; /**
 * Created by deoxys on 22.05.16.
 */

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.mongodb.MongoClient;
import dao.MongoUserDAO;
import model.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

@WebListener()
public class HzContextListener implements ServletContextListener {

    // Public constructor is required by servlet spec
    public HzContextListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
        ListConfig listConfig = new ListConfig();
        listConfig.setName("USERS");

        ServletContext ctx = sce.getServletContext();
        int port = Integer.parseInt(ctx.getInitParameter("HZ_PORT"));
        int hosts = Integer.parseInt(ctx.getInitParameter("HZ_HOSTS"));

        Config cfg = new Config();
        cfg.addListConfig(listConfig);
        cfg.getNetworkConfig().setPort(port);
        cfg.getNetworkConfig().setPortAutoIncrement(false);

        NetworkConfig network = cfg.getNetworkConfig();
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false);
        for (int i = 1; i <= hosts; i++)
            join.getTcpIpConfig().addMember(ctx.getInitParameter("HZ_HOST_" + i));
        join.getTcpIpConfig().setEnabled(true);

        MongoClient mongo = (MongoClient) sce.getServletContext().getAttribute("MONGO_CLIENT");
        MongoUserDAO userDAO = new MongoUserDAO(mongo);
        List<User> users = userDAO.findAllUsers();

        cfg.setInstanceName("USERS");
        Hazelcast.newHazelcastInstance(cfg);
        System.out.println("Hazelcast: initialized successfully");
    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
        Hazelcast.shutdownAll();
        System.out.println("Hazelcast: closed successfully");
    }
}
