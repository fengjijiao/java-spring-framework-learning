package us.fjj.spring.learning.transactioninterceptorordercontrolusage.test1;

import org.apache.logging.log4j.core.config.Order;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Aspect
@Order(3)
public class TransactionInterceptorAfter {
    @Pointcut("execution(* UserService.*(..))")
    public void pc() {}

    @Around("pc()")
    public Object tsAfter(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("----------after start-------------");
        Object result = joinPoint.proceed();
        System.out.println("----------after end-------------");
        return result;
    }
}
