package us.fjj.spring.learning.aspectj5notifytype.afterreturningtest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AfterReturningAspect {
    @Pointcut("execution(* us.fjj.spring.learning.aspectj5notifytype.afterreturningtest.Service4.*(..))")
    public void pc1() {}
    @AfterReturning(value = "pc1()", returning = "returnVal")
    public void afterReturning(JoinPoint joinPoint, Object returnVal) {
        System.out.println(joinPoint.getSignature().getName()+"执行完成，返回值："+returnVal);
    }
}
