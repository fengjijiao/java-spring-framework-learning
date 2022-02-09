package us.fjj.spring.learning.proxyclass;

public class ServiceB implements IService {
    @Override
    public void m1() {
        System.out.println("我是ServiceB的m1方法！");
    }

    @Override
    public void m2() {
        System.out.println("我是ServiceB的m2方法！");
    }

    @Override
    public void m3() {
        System.out.println("我是ServiceB的m3方法！");
    }
}
