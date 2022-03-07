package us.fjj.spring.learning.enableaspectjautoproxyusage.test1;

import org.springframework.stereotype.Component;

@Component
public class UserService {
    public void say() {
        System.out.println("我是UserService");
    }
}
