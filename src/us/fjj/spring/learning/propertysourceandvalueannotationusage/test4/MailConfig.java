package us.fjj.spring.learning.propertysourceandvalueannotationusage.test4;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 邮件配置信息
 */
@Component
@RefreshScope
public class MailConfig {
    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;

    @Override
    public String toString() {
        return "MailConfig{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
