package us.fjj.spring.learning.beanlifecycle.test7;

public class TestObj {
    private Service1 service1;
    private Service2 service2;

    @Override
    public String toString() {
        return "TestObj{" +
                "service1=" + service1 +
                ", service2=" + service2 +
                '}';
    }

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
}
