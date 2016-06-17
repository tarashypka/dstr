package com.dstr.listener;

/**
 * Created by deoxys on 17.06.16.
 */

import com.dstr.dao.MongoItemDAO;
import com.dstr.model.Item;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.ListConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.mongodb.MongoClient;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

@WebListener()
public class HzContextListener implements ServletContextListener {
    final static Logger logger = Logger.getLogger(HzContextListener.class);

    // Public constructor is required by servlet spec
    public HzContextListener() { }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
        ListConfig listConfig = new ListConfig();
        listConfig.setName("ITEMS");

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

        MongoClient mongo = (MongoClient) sce.getServletContext()
                .getAttribute("MONGO_CLIENT");
        MongoItemDAO itemDAO = new MongoItemDAO(mongo);

        cfg.setInstanceName("ITEMS");
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(cfg);

        List<Item> items = hz.getList("ITEMS");
        items.addAll(itemDAO.findAllItems());

        logger.info("Hazelcast: initialized successfully");
    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
        Hazelcast.shutdownAll();
        logger.info("Hazelcast: closed successfully");
    }
}
