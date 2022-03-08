package us.fjj.spring.learning.enableasynandasynccusage.test6;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@ComponentScan
@EnableAsync
public class MainConfig6 {
    public static final String RECHARGE_EXECUTION_BEAN_NAME = "recharge_exec";
    public static final String CASHOUT_EXECUTION_BEAN_NAME = "cashOut_exec";
    @Bean(RECHARGE_EXECUTION_BEAN_NAME)
    public Executor rechargeExec() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(100);
        taskExecutor.setThreadNamePrefix("recharge-thread-");
        return taskExecutor;
    }
    @Bean(CASHOUT_EXECUTION_BEAN_NAME)
    public Executor cashOutExec() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(100);
        taskExecutor.setThreadNamePrefix("cashOut-thread-");
        return taskExecutor;
    }
}
