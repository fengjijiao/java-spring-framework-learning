package us.fjj.spring.learning.aspectpointcutusage.test1;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AspectA {
    //定义一个切入点，可以匹配ServiceA中所有方法
    @Pointcut("execution(* us.fjj.spring.learning.aspectpointcutusage.test1.ServiceA.*(..))")
    public void pointcut1() {
    }
    //定义一个前置通知，对上面定义的切入点有效
    @Before(value = "pointcut1()")
    public void before(JoinPoint joinPoint) {
        //输出连接点的信息
        System.out.println("前置通知,"+joinPoint);
    }
    //定义一个异常通知，这个通知对上面定义的切入点有效, e对应参数e
    @AfterThrowing(value = "pointcut1()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        //发生异常之后输出异常信息
        System.out.println(joinPoint+"发生异常"+e.getMessage());
    }
}
