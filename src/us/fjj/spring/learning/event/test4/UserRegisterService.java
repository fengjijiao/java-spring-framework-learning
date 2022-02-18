package us.fjj.spring.learning.event.test4;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterService implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    public void register(String username) {
        System.out.println(String.format("用户%s注册成功！", username));
        //注册成功，发布注册成功事件
        this.applicationEventPublisher.publishEvent(new UserRegisterSuccessEvent(this, username));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
