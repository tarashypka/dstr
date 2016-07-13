package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.HttpRequestReader;
import com.deoxys.dev.dstr.domain.converter.HttpSessionReader;
import com.mongodb.MongoClient;

import javax.naming.InitialContext;

/**
 * Created by deoxys on 08.07.16.
 *
 * Service that uses Mongo Connection Pool
 */

public abstract class MongoService<T> {

    public MongoClient mongo;
    protected HttpRequestReader<T> requestReader;
    protected HttpSessionReader<T> sessionReader;

    public <T extends HttpRequestReader & HttpSessionReader> MongoService(T reader) {
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
        requestReader = reader;
        sessionReader = reader;
    }
}
