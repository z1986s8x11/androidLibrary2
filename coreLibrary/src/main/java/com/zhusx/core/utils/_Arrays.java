package com.zhusx.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/2/1 17:19
 */
public final class _Arrays {
    private _Arrays() {
    }

    public static <E> Set<E> newHashMap(E... item) {
        Set<E> set = new HashSet<>();
        Collections.addAll(set, item);
        return set;
    }

    public static <T> List<T> asList(T... t) {
        List<T> list = new ArrayList<>();
        Collections.addAll(list, t);
        return list;
    }

}

