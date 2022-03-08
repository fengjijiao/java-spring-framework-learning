package us.fjj.spring.learning.enableaspectjautoproxyusage.test4;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
public class Aspect1 {
    @Pointcut("execution(* us.fjj.spring.learning.enableaspectjautoproxyusage.test4.UserService4.*(..))")
    public void pc1() {}
    @Before("pc1()")
    public void before(JoinPoint joinPoint) {
        System.out.println("Aspect1 Before.");
    }
    @After("pc1()")
    public void after(JoinPoint joinPoint) {
        System.out.println("Aspect1 After.");
    }
    @Around("pc1()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Aspect1 Around start");
        Object result = joinPoint.proceed();
        System.out.println("Aspect1 Around end");
        return result;
    }

    @AfterThrowing(value = "pc1()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) throws Throwable {
        System.out.println("Aspect1 afterThrowing");
    }

    @AfterReturning(value = "pc1()", returning = "retVal")
    public void afterReturning(JoinPoint joinPoint, Object retVal) throws Throwable {
        System.out.println("Aspect1 AfterReturning.");
    }
}
