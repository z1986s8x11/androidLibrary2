package com.zhusx.core.interfaces;

/**
* Author        zhusx
* Email         327270607@qq.com
* Created       2016/12/12 9:24
*/
public abstract class Lib_Runnable implements Runnable {
    private boolean isCancel;

    public boolean _isCancel() {
        return isCancel;
    }

    public void _setCancel() {
        this.isCancel = true;
    }
}
