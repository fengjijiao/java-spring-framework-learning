package us.fjj.spring.learning.enableaspectjautoproxyusage.test4;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(3)
public class Aspect2 {
    @Pointcut("execution(* us.fjj.spring.learning.enableaspectjautoproxyusage.test4.UserService4.*(..))")
    public void pc2() {}
    @Before("pc2()")
    public void before(JoinPoint joinPoint) {
        System.out.println("Aspect2 Before.");
    }
    @After("pc2()")
    public void after(JoinPoint joinPoint) {
        System.out.println("Aspect2 After.");
    }
    @Around("pc2()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Aspect2 Around start");
        Object result = joinPoint.proceed();
        System.out.println("Aspect2 Around end");
        return result;
    }

    @AfterThrowing(value = "pc2()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) throws Throwable {
        System.out.println("Aspect2 afterThrowing");
    }

    @AfterReturning(value = "pc2()", returning = "retVal")
    public void afterReturning(JoinPoint joinPoint, Object retVal) throws Throwable {
        System.out.println("Aspect2 AfterReturning.");
    }
}
