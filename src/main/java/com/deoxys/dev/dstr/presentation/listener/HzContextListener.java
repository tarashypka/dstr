package com.deoxys.dev.dstr.presentation.listener;

/**
 * Created by deoxys on 17.06.16.
 */

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.ListConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener()
public class HzContextListener implements ServletContextListener {
    final static Logger logger = Logger.getLogger(HzContextListener.class);

    public HzContextListener() { }

    public void contextInitialized(ServletContextEvent sce) {

        /**
         *  Any type of user uses all items list.
         *  That's why it's better to save all items in shared Hz cache.
         */
        ListConfig itemsListConf = new ListConfig();
        itemsListConf.setName("ITEMS");

        ServletContext ctx = sce.getServletContext();
        String host = ctx.getInitParameter("HZ_HOST");
        int port = Integer.parseInt(ctx.getInitParameter("HZ_PORT"));

        Config cfg = new Config();
        cfg.addListConfig(itemsListConf);
        cfg.getNetworkConfig().setPort(port);

        /**
         *  First Hz initialization will take place on port HZ_PORT.
         *  To exclude 'Port in use' runtime exception,
         *  further Hz initializations will be on auto-incremented ports.
         */
        cfg.getNetworkConfig().setPortAutoIncrement(true);

        NetworkConfig network = cfg.getNetworkConfig();
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig().setEnabled(true);
        join.getTcpIpConfig().addMember(host);

        cfg.setInstanceName("HZ_CACHE");
        Hazelcast.newHazelcastInstance(cfg);

        logger.info("Hazelcast: initialized successfully");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        Hazelcast.shutdownAll();
        logger.info("Hazelcast: closed successfully");
    }
}
