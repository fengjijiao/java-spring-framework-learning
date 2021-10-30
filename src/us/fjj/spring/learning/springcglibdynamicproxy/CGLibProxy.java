package us.fjj.spring.learning.springcglibdynamicproxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLibProxy implements MethodInterceptor {
    private Object target;//需要代理的目标对象
    final MyAspect myAspect = new MyAspect();
    //重写拦截方法
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        myAspect.myBefore();
        Object invoke = method.invoke(target, objects);//方法执行，参数：target目标对象，arr参数数组
        myAspect.myAfter();
        return invoke;
    }
    //定义获取代理对象方法
    public Object getCGLibProxy(Object targetObject) {
        //为目标对象target赋值
        this.target = targetObject;
        Enhancer enhancer = new Enhancer();
        //设置父类，因为CGlib是针对指定的类生成一个子类，所有需要指定父类
        enhancer.setSuperclass(targetObject.getClass());
        enhancer.setCallback(this);//设置回调
        Object result = enhancer.create();//创建并返回代理对象
        return result;
    }

    public static void main(String[] args) {
        CGLibProxy cgLib = new CGLibProxy();//实例化CGLibProxy对象
        UserManager userManager = (UserManager) cgLib.getCGLibProxy(new UserManagerImpl());//获取代理对象
        userManager.addUser("ok", "123456");//执行新增方法
        userManager.delUser("ok");//执行删除方法
        /**
         * 方法执行前
         * 正在执行添加用户方法
         * 用户名称：ok，密码：123456
         * 方法执行后
         * 方法执行前
         * 正在执行删除用户方法
         * 用户名称：ok
         * 方法执行后
         */
    }
}
