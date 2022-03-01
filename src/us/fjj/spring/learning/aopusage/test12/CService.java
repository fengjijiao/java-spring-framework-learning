package us.fjj.spring.learning.aopusage.test12;

import org.springframework.aop.framework.AopContext;

public class CService {
    public void m1() {
        System.out.println("m1");
        ((CService) AopContext.currentProxy()).m2();//2
    }

    public void m2() {
        System.out.println("m2");
    }
}
