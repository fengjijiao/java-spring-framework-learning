package us.fjj.spring.learning.enableasynandasynccusage.test5;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;
import java.util.Arrays;

@EnableAsync
public class MainConfig5 {
    @Bean
    public LogService5 logService5() {
        return new LogService5();
    }

    @Bean
    public AsyncConfigurer asyncConfigurer() {
        return new AsyncConfigurer() {
            @Override
            public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
                return new AsyncUncaughtExceptionHandler() {
                    @Override
                    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                        String msg = String.format("方法[%s]，参数[%s]，发生异常了，异常详细信息：", method, Arrays.asList(params));
                        System.out.println(msg);
                        ex.printStackTrace();
                    }
                };
            }
        };
    }
}
