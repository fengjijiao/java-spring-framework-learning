package us.fjj.spring.learning.scheduledandenableshedulingusage.test3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@ComponentScan
@EnableScheduling
public class MainConfig3 {
    @Bean
    public ScheduledExecutorService taskExecutor() {
        return Executors.newScheduledThreadPool(10);
    }
}
