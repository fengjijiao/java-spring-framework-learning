package us.fjj.spring.learning.event.test6;

import org.springframework.context.ApplicationEvent;

public class UserRegisterSuccessEvent extends ApplicationEvent {
    private String username;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public UserRegisterSuccessEvent(Object source, String username) {
        super(source);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
