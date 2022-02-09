package us.fjj.spring.learning.replacemethod;

public class ServiceB {
    public void say() {
        ServiceA serviceA = this.getServiceA();
        System.out.println("serviceB:"+this+",serviceA:"+serviceA);
    }
    public ServiceA getServiceA() {
        return null;
    }
}
