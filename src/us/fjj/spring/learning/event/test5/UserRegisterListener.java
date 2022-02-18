package us.fjj.spring.learning.event.test5;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 用户注册监听器
 */
@Component
public class UserRegisterListener {
    /**
     * 发送邮件
     * @param event
     */
    @EventListener
    public void sendMail(UserRegisterSuccessEvent event) {
        System.out.println(String.format("发送邮件：用户%s注册成功！", event.getUsername()));
    }

    /**
     * 发送优惠卷
     * @param event
     */
    @EventListener
    public void sendCoupon(UserRegisterSuccessEvent event) {
        System.out.println(String.format("给用户%s发送优惠卷成功!", event.getUsername()));
    }
}
