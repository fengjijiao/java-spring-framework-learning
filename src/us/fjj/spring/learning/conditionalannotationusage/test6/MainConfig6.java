package us.fjj.spring.learning.conditionalannotationusage.test6;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional({Condition2.class, Condition3.class, Condition1.class})
public class MainConfig6 {
}
