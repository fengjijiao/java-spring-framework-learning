package us.fjj.spring.learning.beanfactoryextensions.test4;

import org.springframework.beans.factory.annotation.Autowired;

public class UserModel {

    @Autowired
    private String username;

    @Override
    public String toString() {
        return "UserModel{" +
                "username='" + username + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
