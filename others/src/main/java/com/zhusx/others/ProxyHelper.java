package com.zhusx.others;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * 演示java动态代理
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/10/18 14:57
 */

public class ProxyHelper {
    public static void main(String[] args) throws IOException,
            InstantiationException, IllegalAccessException {
        // Observable<String> o = create(A.class).test("name", "pwd");
        // System.out.println(o);
        create(A.class).test("11", "").toString();
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> clazz) {
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException(
                    "API declarations must be interfaces.");
        }
        if (clazz.getInterfaces().length > 0) {
            throw new IllegalArgumentException(
                    "API interfaces must not extend other interfaces.");
        }
        // for (Method method : clazz.getDeclaredMethods()) {
        // System.out.println(method.getName());
        // }
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method,
                                         Object[] args) throws Throwable {
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }
                        POST postWay = method.getAnnotation(POST.class);
                        if (postWay != null) {
                            System.out.println(postWay.value());
                        }
//                        Parameter[] param = method.getParameters();
//                        for (int i = 0; i < method.getParameterCount(); i++) {
//                            Field field = param[i].getAnnotation(Field.class);
//                            System.out.println(field.value() + ":" + args[i]);
//                        }
                        System.out.println(method);
                        System.out.println(Arrays.toString(args));
                        Observable<String> ob = new Observable<String>();
                        return ob;
                    }
                });
    }

    public interface A {
        @POST("/test-signature")
        Observable<String> test(@Field("password") String pwd,
                                @Field("username") String name);

        @POST("/test-signature")
        Observable<String> name(@Field("my") String myName);
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface POST {
        String value();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface GET {
        String value();
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Field {
        String value();
    }

    public static class Observable<T> {
        public String url;
        public String method;
        public String fields;

        @Override
        public String toString() {
            return String.format("url:%s \nmethod:%s \nfields:%s", url, method,
                    fields);
        }
    }
}
