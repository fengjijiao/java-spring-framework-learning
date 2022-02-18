package us.fjj.spring.learning.event.test1;

public class UserRegisterService {
    private EventMulticaster eventMulticaster;

    public void register(String username) {
        System.out.println(String.format("注册用户%s成功！", username));
        this.getEventMulticaster().multicastEvent(new UserRegisterSuccessEvent(this, username));
    }

    public EventMulticaster getEventMulticaster() {
        return eventMulticaster;
    }

    public void setEventMulticaster(EventMulticaster eventMulticaster) {
        this.eventMulticaster = eventMulticaster;
    }
}
