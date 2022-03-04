package us.fjj.spring.learning.aspectpointcutusage.test15;

public class Service15 {
    private String name;

    public Service15(String name) {
        this.name = name;
    }

    public void m1() {
        System.out.println(name+"我是m1方法");
    }
    public void m2() {
        System.out.println(name+"我是m2方法");
    }
    public void m3() {
        System.out.println(name+"我是m3方法");
    }
}
