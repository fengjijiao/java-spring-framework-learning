package us.fjj.spring.learning.proxyexpend.cglib.test6;

import java.util.List;

public interface IMethodInfo {
    //获取方法数量
    int methodCount();
    //获取被代理的对象中方法名称列表
    List<String> methodNames();
}
