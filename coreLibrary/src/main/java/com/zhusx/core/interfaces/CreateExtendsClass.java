package com.zhusx.core.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/1/13 12:31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface CreateExtendsClass {
    String packageName();

    String ClassName();
}
