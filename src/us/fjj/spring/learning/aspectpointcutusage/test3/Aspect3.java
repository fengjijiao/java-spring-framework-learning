package us.fjj.spring.learning.aspectpointcutusage.test3;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Aspect3 {
    @Pointcut(value = "within(C1)")//匹配的类型是C1,也就是说被代理的对象的类型必须是C1类型的才行，需要和C1完全匹配，若要匹配到C2则需要改成within(C1+)\within(C2)
    public void pc(){}
    @Before(value = "pc()")
    public void before(JoinPoint joinPoint) {
        System.out.println(String.format("将要执行%s方法", joinPoint.getSignature().getName()));
    }
}
