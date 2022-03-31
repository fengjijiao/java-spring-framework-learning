package us.fjj.spring.learning.databaseseparateusage.base;

import org.apache.logging.log4j.core.config.Order;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 读写分离拦截器，需放在事务拦截器(maybe?默认是Integer.MAX_VALUE)前面执行，通过@1代码我们将此拦截器的顺序设置为Integer.MAX_VALUE - 2，稍后我们将事务拦截器的顺序设置为Integer.MAX_VALUE - 1,事务拦截器的执行顺序是从小到大的，所以ReadWriteInterceptor会在事务拦截器org.springframework.transaction.interceptor.TransactionInterceptor之前执行。
 * 由于业务方法中存在相互调用的情况，比如service1.m1中调用service2.m2，而service2.m2中调用了service2.m3，我们只需要在m1方法执行之前，获取具体要用哪个数据源就可以了，所以下面代码中会在第一次进入这个拦截器 的时候，记录一下走主库还是从库。
 * 下面方法中会获取当前目标方法的最后一个参数，最后一个参数可以是DsType类型的，开发者可以通过这个参数控制具体是走主库还是从库。
 */
@Aspect
@Order(Integer.MAX_VALUE - 2)//@1
@Component
public class ReadWriteInterceptor {
    @Pointcut("target(IService)")
    public void pc() {
    }

    //获取当前目标方法的最后一个参数
    private Object getLastArgs(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        if (null != args && args.length > 0) {
            return args[args.length - 1];
        } else {
            return null;
        }
    }

    @Around("pc()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        //判断是否是第一次进来，用于处理事务嵌套
        boolean isFirst = false;
        try {
            if (DsTypeHolder.getDsType() == null) {
                isFirst = true;
            }
            if (isFirst) {
                Object lastArg = getLastArgs(pjp);
                if (DsType.MASTER.equals(lastArg)) {
                    DsTypeHolder.master();
                } else {
                    DsTypeHolder.slaver();
                }
            }
            return pjp.proceed();
        } finally {
            //退出时，清理
            if (isFirst) {
                DsTypeHolder.clearDsType();
            }
        }
    }
}
