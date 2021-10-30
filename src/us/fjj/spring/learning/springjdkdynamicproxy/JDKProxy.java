package us.fjj.spring.learning.springjdkdynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//JDK动态代理实现InvocationHandler接口
public class JDKProxy implements InvocationHandler {
    private Object target;//需要代理的目标对象
    final MyAspect myAspect = new MyAspect();
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        myAspect.myBefore();
        Object result = method.invoke(target, args);
        myAspect.myAfter();
        return result;
    }

    //定义获取代理对象方法
    private Object getJDKProxy(Object targetObject) {
        //为目标对象target赋值
        this.target = targetObject;
        //JDK动态代理只能代理实现了接口的类，从newProxyInstance函数所需的参数就可以看出来
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces(), this);
    }

    public static void main(String[] args) {
        JDKProxy jdkProxy = new JDKProxy();
        UserManager userManager = (UserManager) jdkProxy.getJDKProxy(new UserManagerImpl());//获取代理对象
        userManager.addUser("ok", "111111");//执行添加方法
        userManager.delUser("ok");//执行删除方法
        /**
         * 方法执行前
         * 正在执行添加用户方法
         * 用户名称：ok，密码：111111
         * 方法执行后
         * 方法执行前
         * 正在执行删除用户方法
         * 用户名称：ok
         * 方法执行后
         */
    }
}
