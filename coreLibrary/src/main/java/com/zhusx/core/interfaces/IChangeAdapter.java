package com.zhusx.core.interfaces;

import java.util.List;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/10/17 9:09
 */

public interface IChangeAdapter<T> {
    void _addItemToUpdate(T t);

    void _addItemToUpdate(List<T> list);

    void _addItemToUpdate(int position, T t);

    void _setItemToUpdate(T t);

    void _setItemToUpdate(List<T> list);

    void _clearItemToUpdate();

    boolean _removeItemToUpdate(T t);

    boolean _removeItemToUpdate(int position);
}
