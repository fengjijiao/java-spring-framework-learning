package us.fjj.spring.learning.aspectj5notifytype.aftertest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AfterAspect {
    @Pointcut("execution(* us.fjj.spring.learning.aspectj5notifytype.aftertest.Service3.*(..))")
    public void pc1(){}

    @After("pc1()")
    public void after(JoinPoint joinPoint) {
        System.out.println(joinPoint.getSignature().getName()+"执行完毕！");
    }
}
