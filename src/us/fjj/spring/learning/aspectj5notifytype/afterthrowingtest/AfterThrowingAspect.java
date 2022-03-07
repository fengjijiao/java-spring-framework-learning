package us.fjj.spring.learning.aspectj5notifytype.afterthrowingtest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

@Aspect
public class AfterThrowingAspect {
    @Pointcut("execution(* us.fjj.spring.learning.aspectj5notifytype.afterthrowingtest.Service5.*(..))")
    public void pc1() {}

    @AfterThrowing(value = "pc1()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, IllegalArgumentException e) {
        System.out.println("当输入"+ Arrays.asList(joinPoint.getArgs())+"时发生错误："+e.getMessage());
    }

}
