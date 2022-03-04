package us.fjj.spring.learning.aspectpointcutusage.test14;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Aspect14 {
    @Pointcut(value = "bean(beanService2)")
    public void pc1() {}
    @Before(value = "pc1()")
    public void before(JoinPoint joinPoint) {
        System.out.println("将要运行："+joinPoint.getSignature().getName());
    }
}
