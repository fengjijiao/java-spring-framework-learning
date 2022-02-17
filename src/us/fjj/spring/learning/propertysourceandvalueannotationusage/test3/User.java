package us.fjj.spring.learning.propertysourceandvalueannotationusage.test3;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@MyScope
public class User {
    private String username;

    public User() {
        System.out.println("创建User对象"+this);
        this.username = UUID.randomUUID().toString();//给username赋值，通过uuid随机生成了一个
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
