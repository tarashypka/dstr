package com.deoxys.dev.dstr.domain.service;

import com.deoxys.dev.dstr.domain.converter.HttpRequestReader;
import com.deoxys.dev.dstr.domain.converter.HttpSessionReader;
import com.mongodb.MongoClient;

import javax.naming.InitialContext;

/**
 * Service that uses Mongo Connection Pool
 */

abstract class MongoService<T> {

    public MongoClient mongo;
    HttpRequestReader<T> requestReader;
    HttpSessionReader<T> sessionReader;

    <T extends HttpRequestReader & HttpSessionReader> MongoService(T reader) {
        try {
            InitialContext ctx = new InitialContext();
            mongo = (MongoClient) ctx.lookup("java:/comp/env/mongo");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        requestReader = reader;
        sessionReader = reader;
    }
}
