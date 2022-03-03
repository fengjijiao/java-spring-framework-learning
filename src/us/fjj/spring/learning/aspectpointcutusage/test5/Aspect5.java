package us.fjj.spring.learning.aspectpointcutusage.test5;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Aspect5 {
    @Pointcut("target(ServiceE)")
    public void pc0() {}
    @Before("pc0()")
    public void before(JoinPoint joinPoint) {
        System.out.println("将要执行"+joinPoint.getSignature().getName());
    }
}
