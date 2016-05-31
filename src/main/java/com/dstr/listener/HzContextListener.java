package com.dstr.listener;

/**
 * Created by deoxys on 22.05.16.
 */

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener()
public class HzContextListener implements ServletContextListener {
    final static Logger logger = Logger.getLogger(HzContextListener.class);

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
        // Initialize Hazelcast with USERS instance
        ListConfig customersLC = new ListConfig();
        ListConfig itemsLC = new ListConfig();
        ListConfig ordersLC = new ListConfig();
        customersLC.setName("CUSTOMERS");
        itemsLC.setName("ITEMS");
        ordersLC.setName("ORDERS");

        ServletContext ctx = sce.getServletContext();
        int port = Integer.parseInt(ctx.getInitParameter("HZ_PORT"));
        int hosts = Integer.parseInt(ctx.getInitParameter("HZ_HOSTS"));

        Config cfg = new Config();
        cfg.addListConfig(customersLC)
                .addListConfig(itemsLC)
                .addListConfig(ordersLC);
        cfg.getNetworkConfig().setPort(port);
        cfg.getNetworkConfig().setPortAutoIncrement(true);

        NetworkConfig network = cfg.getNetworkConfig();
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(true);
        for (int i = 1; i <= hosts; i++)
            join.getTcpIpConfig().addMember(ctx.getInitParameter("HZ_HOST_" + i));
        join.getTcpIpConfig().setEnabled(false);

        cfg.setInstanceName("HZ_CONFIG");
        // Hazelcast.newHazelcastInstance(cfg);
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
