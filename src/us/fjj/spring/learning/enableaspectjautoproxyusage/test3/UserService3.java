package us.fjj.spring.learning.enableaspectjautoproxyusage.test3;

import org.springframework.stereotype.Component;

@Component
public class UserService3 {
    public String say() {
        return "你好，lk";
    }
    public void say2() {
        System.out.println("你好，lk");
    }
}
