package us.fjj.spring.learning.event.test1;

public class UserRegisterSuccessEvent extends AbstractEvent {
    private String username;

    public UserRegisterSuccessEvent(Object source, String username) {
        super(source);
        System.out.println(source);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
