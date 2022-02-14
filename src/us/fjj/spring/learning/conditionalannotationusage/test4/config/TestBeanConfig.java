package us.fjj.spring.learning.conditionalannotationusage.test4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import us.fjj.spring.learning.conditionalannotationusage.test4.EnvironmentConditional;

@Configuration
@EnvironmentConditional(EnvironmentConditional.ENVIRONMENT.TEST)
public class TestBeanConfig {
    @Bean
    public String name() {
        return "测试环境";
    }
}
