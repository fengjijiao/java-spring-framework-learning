package us.fjj.spring.learning.aspectpointcutusage.test7;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Aspect7 {
    @Pointcut("@within(Ann7)")
    public void pc0() {

    }

    @Before("pc0()")
    public void before(JoinPoint joinPoint) {
        System.out.println("将要执行"+joinPoint.getSignature().getName());
    }
}
