package us.fjj.spring.learning.propertysourceandvalueannotationusage.test1;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource("classpath:us/fjj/spring/learning/propertysourceandvalueannotationusage/test1/db.properties")
public class MainConfig1 {
}
