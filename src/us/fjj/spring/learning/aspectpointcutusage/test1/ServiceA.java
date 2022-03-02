package us.fjj.spring.learning.aspectpointcutusage.test1;

public class ServiceA {
    public void m1() {
        System.out.println("我是m1方法");
    }
    public void m2() {
        System.out.println(10/0);
        System.out.println("我是m2方法");
    }
}
