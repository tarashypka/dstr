package com.deoxys.dev.dstr.domain.converter;

import javax.servlet.http.HttpSession;

@FunctionalInterface
public interface HttpSessionReader<T> {

    T read(HttpSession ses);
}