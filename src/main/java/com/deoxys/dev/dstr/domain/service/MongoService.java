package com.deoxys.dev.dstr.domain.service;

import com.mongodb.MongoClient;

import javax.naming.InitialContext;

/**
 * Created by deoxys on 08.07.16.
 *
 * Service that uses Mongo Connection Pool
 */

public abstract class MongoService  {

    public MongoClient mongo;

    public MongoService() {
        try {
            InitialContext ctx = new InitialContext();
            if (ctx == null) {
                throw new Exception("Context not found");
            }
            mongo = (MongoClient) ctx.lookup("java:/comp/env/mongo");
            if (mongo == null) {
                throw new Exception("MongoClient not found in Context");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
