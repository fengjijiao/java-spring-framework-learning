package us.fjj.spring.learning.aspectj5notifytype.afterthrowingtest;

public class Service5 {
    public void login(String username, String password) throws IllegalArgumentException {
        if (!username.equals("lk")) {
            throw new IllegalArgumentException("username != lk");
        }
        System.out.println("login success.");
    }
}
