package us.fjj.spring.learning.internationalization.test4;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfig4 {
    @Bean
    public MessageSource messageSource() {
        return new MessageSourceFromDB();
    }
}
