package us.fjj.spring.learning.conditionalannotationusage.test5;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional({Condition1.class, Condition2.class, Condition3.class})
public class MainConfig5 {//BeanConfig
}
