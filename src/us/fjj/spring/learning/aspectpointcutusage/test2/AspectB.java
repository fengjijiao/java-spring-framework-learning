package us.fjj.spring.learning.aspectpointcutusage.test2;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

@Aspect
public class AspectB {
    @Pointcut(value = "execution(* us.fjj.spring.learning.aspectpointcutusage.test2.ServiceB.*(..))")
    public void pointcut1() {
    }

    @After(value = "pointcut1()")
    public void after(JoinPoint joinPoint) {
        System.out.println("After: " + joinPoint);
    }

    @Before(value = "pointcut1()")
    public void before(JoinPoint joinPoint) {
        System.out.println("Before: " + joinPoint);
    }

    @AfterReturning(value = "pointcut1()", returning = "result")//这里的result是下面的方法参数result
    public void afterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("afterReturning: " + joinPoint +", result: "+result);
    }

    @AfterThrowing(value = "pointcut1()", throwing = "e")//这里的e是下面的方法参数e
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        System.out.println("afterThrowing: "+joinPoint);
    }

    @Around(value = "execution(public String us.fjj.spring.learning.aspectpointcutusage.test2.Service*.m2(int))")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object result = null;
        String methodName = joinPoint.getSignature().getName();
        System.out.println(methodName+"->前置通知，参数："+ Arrays.stream(joinPoint.getArgs()));
        try {
            result = joinPoint.proceed();
            System.out.println(methodName+"->返回通知，结果："+ result);
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println(methodName+"->异常通知，Exception："+ e);
        }
        System.out.println(methodName+"->后置通知");
        return result;
    }
}
