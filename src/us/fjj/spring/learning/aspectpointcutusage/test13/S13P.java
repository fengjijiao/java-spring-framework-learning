package us.fjj.spring.learning.aspectpointcutusage.test13;

public class S13P {
    @Ann13
    public void m1() {
        System.out.println("我是m1方法");
    }
    @Ann13
    public void m2() {
        System.out.println("我是m2方法");
    }
}
