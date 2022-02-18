package us.fjj.spring.learning.event.test2;

import org.springframework.stereotype.Component;

/**
 * 用户注册成功事件监听器：负责给用户发送邮件
 */
@Component
public class SendEmailOnUserRegisterSuccessListener implements EventListener<UserRegisterSuccessEvent> {
    @Override
    public void onEvent(UserRegisterSuccessEvent event) {
        System.out.println(String.format("发送欢迎邮件到用户%s成功！", event.getUsername()));
    }
}
