package us.fjj.spring.learning.commonproxyclass;

public class Service2 {
    public void m1() {
        System.out.println("这里是Service2的m1方法！");
        m2();
    }
    public void m2() {
        System.out.println("这里是Service2的m2方法！");
    }
}
