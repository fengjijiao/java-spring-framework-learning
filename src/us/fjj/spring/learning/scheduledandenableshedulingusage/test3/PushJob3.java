package us.fjj.spring.learning.scheduledandenableshedulingusage.test3;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class PushJob3 {
    @Scheduled(fixedRate = 1000)
    public void job1() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        System.out.println(Thread.currentThread()+" job1执行时间"+System.currentTimeMillis());
    }
    @Scheduled(fixedRate = 1000)
    public void job2() {
        System.out.println(Thread.currentThread()+" job2执行时间"+System.currentTimeMillis());
    }
}
