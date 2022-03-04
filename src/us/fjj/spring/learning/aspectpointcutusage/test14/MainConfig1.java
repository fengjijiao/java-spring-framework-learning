package us.fjj.spring.learning.aspectpointcutusage.test14;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BeanConfig1.class)
public class MainConfig1 {
}
