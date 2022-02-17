package us.fjj.spring.learning.beanlifecycle.test23;

public class ServiceA {
    public ServiceA() {
        System.out.println("create " + this.getClass());
    }
}
