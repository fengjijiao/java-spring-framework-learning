package us.fjj.spring.learning.transactioninterceptorordercontrolusage.test1;

import org.apache.logging.log4j.core.config.Order;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
public class TransactionInterceptorBefore {
    @Pointcut("execution(* UserService.*(..))")
    public void pointcut() {}

    @Around("pointcut()")
    public Object tsBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("---------before start----------");
        Object result = joinPoint.proceed();
        System.out.println("---------before end  ----------");
        return result;
    }
}
