package us.fjj.spring.learning.aspectpointcutusage.test15;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Aspect15 {
    @Pointcut("AspectPcDefine15.pc1()")
    public void pc3() {}
    @Pointcut("AspectPcDefine15.pc1() || AspectPcDefine15.pc2()")
    public void pc4() {}
    @Before("pc3()")
    public void before1(JoinPoint joinPoint) {
        System.out.println("111将要执行"+joinPoint.getSignature().getName());
    }
    @Before("pc4()")
    public void before2(JoinPoint joinPoint) {
        System.out.println("222将要执行"+joinPoint.getSignature().getName());
    }
}
