package us.fjj.spring.learning.aspectpointcutusage.test6;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Aspect6 {
    @Pointcut("args(String)")
    public void pc1() {}
    @Before("pc1()")
    public void before(JoinPoint joinPoint) {
        System.out.println("参数为："+joinPoint.getArgs()[0]);
    }
}
