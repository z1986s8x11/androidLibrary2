package com.zhusx.core.interfaces;

import java.util.Set;

/**
* Author        zhusx
* Email         327270607@qq.com
* Created       2016/12/12 9:24
*/
public interface Lib_LifeCycleListener {
    void _addOnCycleListener(Lib_OnCycleListener listener);

    void _removeOnCycleListener(Lib_OnCycleListener listener);

    Set<Lib_OnCycleListener> getCycleListeners();
}
