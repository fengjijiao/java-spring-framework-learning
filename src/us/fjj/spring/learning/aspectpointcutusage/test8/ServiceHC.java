package us.fjj.spring.learning.aspectpointcutusage.test8;

public class ServiceHC extends ServiceH {
    @Override
    public void m2() {
//        super.m2();
        System.out.println("我是m2");
    }
    public void m3() {
        System.out.println("我是m3");
    }
}
