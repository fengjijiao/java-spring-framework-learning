package us.fjj.spring.learning.aspectpointcutusage.test4;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Aspect4 {
    @Pointcut(value = "this(ServiceD)")
    public void pc1(){}
    @Before(value = "pc1()")
    public void before(JoinPoint joinPoint) {
        System.out.println(String.format("将要执行%s方法", joinPoint.getSignature().getName()));
    }
}
