package com.deoxys.dev.dstr.domain.converter;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by deoxys on 11.07.16.
 */

public interface HttpRequestReader<T> {

    T read(HttpServletRequest req);
}
