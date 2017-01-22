package com.zhusx.core.entity;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/12 9:24
 */

public class AdapterItem<K, V> {
    public K a;
    public V b;

    public AdapterItem(K a, V b) {
        this.a = a;
        this.b = b;
    }

    public AdapterItem() {
    }

    @Override
    public String toString() {
        return String.valueOf(a);
    }
}
