package us.fjj.spring.learning.aspectpointcutusage.test12;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Aspect12 {
    @Pointcut("@args(*, Ann12)")
    public void pc1() {}
    @Before("pc1()")
    public void before(JoinPoint joinPoint) {
        System.out.println("将要执行"+joinPoint.getSignature().getName());
    }
}
