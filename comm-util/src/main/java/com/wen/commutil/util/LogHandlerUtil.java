package com.wen.commutil.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * LogHandlerUtil类
 * 动态代理模式
 * @author calwen
 */
public class LogHandlerUtil implements InvocationHandler {
    private Object target;

    public Object newProxyInstance(Object targetObject) {
        this.target = targetObject;
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),
                targetObject.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {

        String runner = target.getClass().getName() + "." + method.getName();
        System.out.println("======================");
        System.out.println(TimeUtil.getNow());
        System.out.println("start --> " + runner);
        Object rs;
        try {
            System.out.println(method);
            rs = method.invoke(target, args);
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error");
            throw e;
        }
        return rs;
    }
}
