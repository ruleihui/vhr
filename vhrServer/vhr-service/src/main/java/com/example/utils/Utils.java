package com.example.utils;

import com.example.entity.Hr;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {
    /**
     * 判断字符串是否为空
     *
     * @param value
     * @return
     */
    public static boolean isNull(String value) {
        return value == null || "".equals(value);
    }


    public static boolean isNull(Integer value) {
        return value == null;
    }

    public static boolean isNull(Object[] value) {
        return value == null || value.length == 0;
    }

    public static Hr getCurrentHr() {
        Hr hr = (Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return hr;
    }
}
