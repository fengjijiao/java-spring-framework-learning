package us.fjj.spring.learning.event.test2;

public class UserRegisterService {
    private EventMulticaster eventMulticaster;

    public void register(String username) {
        System.out.println(String.format("注册用户%s成功！", username));
        this.eventMulticaster.multicastEvent(new UserRegisterSuccessEvent(this, username));
        /**
         * 参数中的this实际上指的是事件来源的对象（这里是UserRegisterService），在事件广播器
         * @see SimpleEventMulticaster
         * 中，使用的是Event（这里是UserRegisterSuccessEvent）来区分归类事件监听器。
         */
    }

    public EventMulticaster getEventMulticaster() {
        return eventMulticaster;
    }

    public void setEventMulticaster(EventMulticaster eventMulticaster) {
        this.eventMulticaster = eventMulticaster;
    }
}
