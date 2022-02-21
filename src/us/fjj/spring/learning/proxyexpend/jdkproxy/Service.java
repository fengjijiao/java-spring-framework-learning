package us.fjj.spring.learning.proxyexpend.jdkproxy;

/**
 * @author jijiao
 */
public class Service implements IService1, IService2 {
    @Override
    public void m1() {
        System.out.println("Service的m1方法");
    }

    @Override
    public void m2() {
        System.out.println("Service的m2方法");
    }
}
