package us.fjj.spring.learning.conditionalannotationusage.test3;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BeanConfig1.class, BeanConfig2.class})
public class MainConfig3 {
}
