package us.fjj.spring.learning.proxyexpend.cglib.test2;

public class Service implements IService1, IService2 {
    @Override
    public void m1() {
        System.out.println("m1方法");
    }

    @Override
    public void m2() {
        System.out.println("m2方法");
    }
}
