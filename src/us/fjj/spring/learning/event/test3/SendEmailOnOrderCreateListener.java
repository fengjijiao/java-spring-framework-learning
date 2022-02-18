package us.fjj.spring.learning.event.test3;

import org.springframework.context.ApplicationListener;

public class SendEmailOnOrderCreateListener implements ApplicationListener<OrderCreateEvent> {
    @Override
    public void onApplicationEvent(OrderCreateEvent event) {
        System.out.println(String.format("发送邮件：订单(ID: %d)创建成功！", event.getOrderId()));
    }
}
