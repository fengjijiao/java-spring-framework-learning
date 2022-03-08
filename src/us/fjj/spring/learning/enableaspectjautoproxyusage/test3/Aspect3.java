package us.fjj.spring.learning.enableaspectjautoproxyusage.test3;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class Aspect3 {
    @Pointcut("execution(* us.fjj.spring.learning.enableaspectjautoproxyusage.test3.UserService3.*(..))")
    public void pc1( ){}
    @Before("pc1()")
    public void before(JoinPoint joinPoint) {
        System.out.println("Before");
    }
    @After("pc1()")
    public void after(JoinPoint joinPoint) {
        System.out.println("After");
    }
    @Around("pc1()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Around start");
        Object retVal= joinPoint.proceed();
        System.out.println("Around end");
        return retVal;
    }
    @AfterReturning(value = "pc1()", returning = "retVal")
    public void afterReturning(JoinPoint joinPoint, Object retVal) throws Throwable {
        System.out.println("AfterReturning: "+retVal);
    }
    @AfterThrowing(value = "pc1()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) throws Throwable {
        System.out.println("AfterThrowing: "+ e.getMessage());
    }
}
