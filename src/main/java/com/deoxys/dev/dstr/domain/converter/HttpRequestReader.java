package com.deoxys.dev.dstr.domain.converter;

import javax.servlet.http.HttpServletRequest;

public interface HttpRequestReader<T> {

    T read(HttpServletRequest req);
}
