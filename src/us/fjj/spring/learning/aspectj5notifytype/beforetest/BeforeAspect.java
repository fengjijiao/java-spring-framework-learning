package us.fjj.spring.learning.aspectj5notifytype.beforetest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class BeforeAspect {
    @Before("execution(* us.fjj.spring.learning.aspectj5notifytype.beforetest.Service1.*(..))")
    public void before(JoinPoint joinPoint) {
        System.out.println("我是前置通知");
    }
}
/**
 * 1.类上需要使用@Aspect标注
 * 2.任意方法上使用@Before标注，将这个方法作为前置通知，目标方法被调用之前，会自动回调这个方法
 * 3.被@Before标注的方法参数可以为空，或者为JoinPoint类型，当为JoinPoint类型时，必须为第一个参数
 * 4.被@Before标注的方法名称可以随意命名
 */
