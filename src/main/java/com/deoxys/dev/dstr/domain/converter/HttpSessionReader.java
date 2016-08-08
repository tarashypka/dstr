package com.deoxys.dev.dstr.domain.converter;

import javax.servlet.http.HttpSession;

public interface HttpSessionReader<T> {

    T read(HttpSession ses);
}