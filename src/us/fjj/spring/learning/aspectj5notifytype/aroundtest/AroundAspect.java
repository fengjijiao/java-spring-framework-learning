package us.fjj.spring.learning.aspectj5notifytype.aroundtest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class AroundAspect {
    @Pointcut("execution(* us.fjj.spring.learning.aspectj5notifytype.aroundtest.Service2.*(..))")
    public void pc1() {}
    @Around("pc1()")
    public Object around1(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取连接点签名
        Signature signature = joinPoint.getSignature();
        //将其转换为方法签名
        MethodSignature methodSignature = (MethodSignature) signature;
        //通过方法签名获取被调用的目标方法。
        Method method = methodSignature.getMethod();

        long startTime = System.nanoTime();
        //调用proceed方法，继续调用下一个通知
        Object returnVal = joinPoint.proceed();
        long endTime = System.nanoTime();
        long costTime = endTime - startTime;
        //输出方法信息
        System.out.println(String.format("%s，耗时（纳秒）：%s", method.toString(), costTime));
        //返回方法的返回值
        return returnVal;
    }
}
