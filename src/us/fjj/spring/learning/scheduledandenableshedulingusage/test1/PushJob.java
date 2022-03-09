package us.fjj.spring.learning.scheduledandenableshedulingusage.test1;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PushJob {
    /**
     * 推送任务，每秒1次
     *
     */
    @Scheduled(fixedRate = 1000)
    public void push() {
        System.out.println("模拟推送消息：" + System.currentTimeMillis());
    }
}
