package com.zhusx.core.interfaces;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/12 9:24
 */

public class Lib_AdapterItems<K, V> {
    public Lib_AdapterItems(K itemA, V itemB) {
        this.itemA = itemA;
        this.itemB = itemB;
    }

    public Lib_AdapterItems() {
    }

    public K itemA;
    public V itemB;
}
