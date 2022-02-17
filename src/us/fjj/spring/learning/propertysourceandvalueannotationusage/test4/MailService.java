package us.fjj.spring.learning.propertysourceandvalueannotationusage.test4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailService {
    @Autowired
    private MailConfig mailConfig;

    @Override
    public String toString() {
        return "MailService{" +
                "mailConfig=" + mailConfig +
                '}';
    }
}
