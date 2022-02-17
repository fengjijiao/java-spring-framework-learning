package us.fjj.spring.learning.fathersoncontainer.test2.module1;

public class Service2 {
    private Service1 service1;

    public void m1() {
        System.out.println("执行了Service2的m1方法(module1)");
        this.service1.m1();
    }

    public Service1 getService1() {
        return service1;
    }

    public void setService1(Service1 service1) {
        this.service1 = service1;
    }
}
