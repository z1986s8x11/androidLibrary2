package com.zhusx.core.entity;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/12 9:24
 */

public class Lib_AdapterItem<K, V> {
    public Lib_AdapterItem(K itemA, V itemB) {
        this.itemA = itemA;
        this.itemB = itemB;
    }

    public Lib_AdapterItem() {
    }

    public K itemA;
    public V itemB;
}
