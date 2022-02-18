package us.fjj.spring.learning.event.test2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan
public class MainConfig2 {
    /**
     * 注册一个bean: 事件发布者
     * 方法传入了EventListener类型的List，这个地方会将容器中所有的事件监听器注入进来，丢到EventMulticaster中。
     * @param eventListeners 事件列表
     * @return 事件发布者
     */
    @Bean
    @Autowired(required = false)
    public EventMulticaster eventMulticaster(List<EventListener> eventListeners) {
        EventMulticaster eventPublisher = new SimpleEventMulticaster();
        eventListeners.forEach(eventPublisher::addEventListener);
        return eventPublisher;
    }

    /**
     * 注册一个bean: 用户注册服务
     * @param eventMulticaster 事件发布者
     * @return 用户注册服务
     */
    @Bean
    public UserRegisterService userRegisterService(EventMulticaster eventMulticaster) {
        UserRegisterService userRegisterService = new UserRegisterService();
        userRegisterService.setEventMulticaster(eventMulticaster);
        return userRegisterService;
    }
}
