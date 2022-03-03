package us.fjj.spring.learning.aspectpointcutusage.test8;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Aspect8 {
    @Pointcut("@within(us.fjj.spring.learning.aspectpointcutusage.test8.Ann8)")
    public void pc1(){}
    @Before("pc1()")
    public void before(JoinPoint joinPoint) {
        System.out.println("将要执行"+joinPoint.getSignature().getName());
    }
}
