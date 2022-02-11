package us.fjj.spring.learning.configurationannotationandbeanannotation;

/**
 * ServiceB依赖于ServiceA，ServiceB通过构造器注入ServiceA。
 */
public class ServiceB {
    private ServiceA serviceA;

    public ServiceB(ServiceA serviceA) {
        this.serviceA = serviceA;
    }

    @Override
    public String toString() {
        return "ServiceB{" +
                "serviceA=" + serviceA +
                '}';
    }
}
