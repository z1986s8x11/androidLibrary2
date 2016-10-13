package com.zhusx.core.utils;

import com.zhusx.core.interfaces.Filter;

import java.util.HashSet;
import java.util.Set;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/7/13 11:08
 */
public class _Sets {
    public static boolean isEmpty(Set set) {
        if (set == null) {
            return true;
        }
        return set.isEmpty();
    }

    public static <T> Set<T> filter(Set<T> set, Filter<T> filter) {
        Set<T> temp = new HashSet<>();
        for (T t : set) {
            if (!filter.isFilter(t)) {
                temp.add(t);
            }
        }
        return temp;
    }
}
