package us.fjj.spring.learning;

public class HelloWorld {
    private String message;

    public String getMessage() {
        System.out.println("message:"+message);
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
