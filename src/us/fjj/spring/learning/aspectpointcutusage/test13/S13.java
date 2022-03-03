package us.fjj.spring.learning.aspectpointcutusage.test13;

public class S13 extends S13P {
    @Override
    public void m2() {
        System.out.println("m2");
    }
    @Ann13
    public void m3() {
        System.out.println("m3");
    }
    public void m4() {
        System.out.println("m4");
    }
}
