package com.deoxys.dev.dstr.persistence.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deoxys on 06.07.16.
 */

public class MongoTestClient {

    private static MongoClient mongo;
    private static final String CONFIG_RESOURCE;

    static {
        CONFIG_RESOURCE = "mongo.xml";
    }

    public static MongoClient getMongo() {
        if (mongo != null) {
            return mongo;
        }
        URL confUrl = MongoTestClient.class.getClassLoader().getResource(CONFIG_RESOURCE);
        File conf = new File(confUrl.toString());
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(conf);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace();
        }
        doc.getDocumentElement().normalize();

        List<ServerAddress> seeds = new ArrayList<>();

        NodeList shards = doc.getElementsByTagName("shards");
        for (int i = 0; i < shards.getLength(); i++) {
            Element e = (Element) shards.item(i);
            String host = e.getElementsByTagName("host").item(i).getTextContent();
            String port = e.getElementsByTagName("port").item(i).getTextContent();
            seeds.add(new ServerAddress(host, Integer.parseInt(port)));
        }

        List<MongoCredential> auths = new ArrayList<>();
        String db = doc.getElementsByTagName("dbName").item(0).getTextContent();
        String user = doc.getElementsByTagName("user").item(0).getTextContent();
        String password = doc.getElementsByTagName("password").item(0).getTextContent();
        auths.add(MongoCredential.createCredential(user, db, password.toCharArray()));

        mongo = new MongoClient(seeds, auths);
        return mongo;
    }
}
