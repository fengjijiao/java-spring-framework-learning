package us.fjj.spring.learning.enableaspectjautoproxyusage.test1;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class Aspect1 {
    @Pointcut("execution(* us.fjj.spring.learning.enableaspectjautoproxyusage.test1.*.*(..))")
    public void pc1() {}

    @Before("pc1()")
    public void before(JoinPoint joinPoint) {
        System.out.println("将要执行"+joinPoint.getSignature().getName());
    }
}
