package us.fjj.spring.learning.event.test4;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SendEmailOnUserRegisterSuccessListener implements ApplicationListener<UserRegisterSuccessEvent> {
    @Override
    public void onApplicationEvent(UserRegisterSuccessEvent event) {
        System.out.println(String.format("发送邮件：用户%s注册成功！", event.getUsername()));
    }
}
