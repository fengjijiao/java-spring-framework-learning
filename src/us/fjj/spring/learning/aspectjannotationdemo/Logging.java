package us.fjj.spring.learning.aspectjannotationdemo;

import org.aspectj.lang.annotation.*;

@Aspect
public class Logging {
    //定义切入点
    @Pointcut("execution(* us.fjj.spring.learning.aspectjannotationdemo.*.*(..))")
    private void selectAll() {
    }

    //前置通知
    @Before("selectAll()")
    public void beforeAdvice() {
        System.out.println("前置通知");
    }

    //后置通知
    @After("selectAll()")
    public void afterAdvice() {
        System.out.println("后置通知");
    }

    //返回后通知
    @AfterReturning(pointcut = "selectAll()", returning = "retVal")
    public void afterReturningAdvice(Object retVal) {
        System.out.println("返回值为："+retVal.toString());
    }

    //抛出异常通知
    @AfterThrowing(pointcut = "selectAll()", throwing = "ex")
    public void afterThrowingAdvice(IllegalArgumentException ex) {
        System.out.println("这里的异常为："+ex.toString());
    }
}
