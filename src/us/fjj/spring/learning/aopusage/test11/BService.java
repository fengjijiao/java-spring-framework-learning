package us.fjj.spring.learning.aopusage.test11;

public class BService {
    public void m1() {
        System.out.println("m1");
        this.m2();
    }
    public void m2() {
        System.out.println("m2");
    }
}
