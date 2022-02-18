package us.fjj.spring.learning.event.test7;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import java.util.concurrent.Executor;

@Configuration
@ComponentScan
public class MainConfig7 {
    /**
     * 定义一个名称为applicationEventMulticaster的事件广播器，内部设置了一个线程池用于异步调用监听器
     * @return
     */
    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {
        //创建一个事件广播器
        SimpleApplicationEventMulticaster result = new SimpleApplicationEventMulticaster();
        //给广播器提供一个线程池，通过这个线程池来调用事件监听器
        Executor executor = this.applicationEventMulticasterThreadPool().getObject();
        //设置异步执行器
        result.setTaskExecutor(executor);
        return result;
    }

    @Bean
    public ThreadPoolExecutorFactoryBean applicationEventMulticasterThreadPool() {
        ThreadPoolExecutorFactoryBean result = new ThreadPoolExecutorFactoryBean();
        result.setThreadNamePrefix("applicationEventMulticasterThreadPool-");
        result.setCorePoolSize(5);
        return result;
    }
}
