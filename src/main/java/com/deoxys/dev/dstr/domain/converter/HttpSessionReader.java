package com.deoxys.dev.dstr.domain.converter;

import javax.servlet.http.HttpSession;

/**
 * Created by deoxys on 11.07.16.
 */

public interface HttpSessionReader<T> {

    T read(HttpSession ses);
}