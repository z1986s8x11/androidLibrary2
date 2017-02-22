package com.zhusx.core.interfaces;

import com.zhusx.core.manager.Lib_SQLManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface CreateSQL {
    Lib_SQLManager.SQLDefaultEnum _defaultValue() default Lib_SQLManager.SQLDefaultEnum.DEFAULT;

    Lib_SQLManager.SQLKeyEnum _key() default Lib_SQLManager.SQLKeyEnum.DEFAULT;

    boolean _isInsert() default true;

    boolean _isCreate() default true;

    boolean _isSelect() default true;
}
