package com.zhusx.core.utils;

import com.zhusx.core.interfaces.IFilter;
import com.zhusx.core.interfaces.IFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/10/13 9:58
 */

public class _Lists {
    public static boolean isEmpty(List list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    public static int size(List list) {
        if (isEmpty(list)) {
            return 0;
        }
        return list.size();
    }

    public static <T> List<T> filter(List<T> list, IFilter<T> filter) {
        List<T> temp = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            if (!filter.isFilter(t)) {
                temp.add(t);
            }
        }
        return temp;
    }

    public static <T, S> List<T> map(List<S> sourceList, IFunction<S, T> f) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            list.add(f.apply(sourceList.get(i)));
        }
        return list;
    }

    public static <T> List<T> range(int count, IFunction<Integer, T> f) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(f.apply(i));
        }
        return list;
    }
}
