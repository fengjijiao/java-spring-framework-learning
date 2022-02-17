package us.fjj.spring.learning.fathersoncontainer.test2.module2;

import us.fjj.spring.learning.fathersoncontainer.test2.module1.Service2;

public class Service3 {
    private Service1 service1;

    private Service2 service2;

    public Service1 getService1() {
        return service1;
    }

    public void setService1(Service1 service1) {
        this.service1 = service1;
    }

    public Service2 getService2() {
        return service2;
    }

    public void setService2(Service2 service2) {
        this.service2 = service2;
    }

    public void m1() {
        System.out.println("执行了Service3的m1方法(module2)");
        service1.m1();
    }

    public void m2() {
        System.out.println("执行了Service3的m2方法(module2)");
        service2.m1();
    }
}
