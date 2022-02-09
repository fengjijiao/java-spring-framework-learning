package us.fjj.spring.learning.proxyclass;

public class ServiceA  implements IService {
    @Override
    public void m1() {
        System.out.println("我是ServiceA的m1方法！");
    }

    @Override
    public void m2() {
        System.out.println("我是ServiceA的m2方法！");
    }

    @Override
    public void m3() {
        System.out.println("我是ServiceA的m3方法！");
    }
}
