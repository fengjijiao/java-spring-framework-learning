package us.fjj.spring.learning.enableasynandasynccusage.test1;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class LogService {
    @Async
    public void log(String msg) throws InterruptedException {
        System.out.println(Thread.currentThread()+"开始记录日志，"+System.currentTimeMillis());
        //模拟耗时2s
        TimeUnit.SECONDS.sleep(2);
        System.out.println(Thread.currentThread()+"日志记录完毕，"+System.currentTimeMillis());
    }
}
