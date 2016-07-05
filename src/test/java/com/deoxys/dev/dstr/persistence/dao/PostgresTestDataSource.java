package com.deoxys.dev.dstr.persistence.dao;

import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PostgresTestDataSource {

    private static DataSource dataSource;

    public static DataSource getDataSource() {

        if (dataSource != null) {
            return dataSource;
        }

        String prjPath = System.getProperty("user.dir");
        String confPath =  prjPath + "/src/test/resources/postgres.xml";
        File conf = new File(confPath);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document doc = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(conf);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace();
        }

        doc.getDocumentElement().normalize();
        Element e = (Element) doc.getElementsByTagName("schema").item(0);

        Map<String, String> schema = new HashMap<>();
        schema.put("server", e.getElementsByTagName("server").item(0).getTextContent());
        schema.put("port", e.getElementsByTagName("port").item(0).getTextContent());
        schema.put("name", e.getElementsByTagName("name").item(0).getTextContent());
        schema.put("user", e.getElementsByTagName("user").item(0).getTextContent());
        schema.put("password", e.getElementsByTagName("password").item(0).getTextContent());
        schema.put("maxConn", e.getElementsByTagName("maxConn").item(0).getTextContent());

        Jdbc3PoolingDataSource source = new Jdbc3PoolingDataSource();
        source.setDataSourceName("Postgres DataSource for Tests");
        source.setServerName(schema.get("server"));
        source.setPortNumber(Integer.parseInt(schema.get("port")));
        source.setDatabaseName(schema.get("name"));
        source.setUser(schema.get("user"));
        source.setPassword(schema.get("password"));
        source.setMaxConnections(Integer.parseInt(schema.get("maxConn")));

        return dataSource = source;
    }
}