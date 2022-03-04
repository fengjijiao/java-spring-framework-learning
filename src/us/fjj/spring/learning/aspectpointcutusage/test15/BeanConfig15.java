package us.fjj.spring.learning.aspectpointcutusage.test15;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy//inportant
public class BeanConfig15 {
    /**
     * important
     * @return
     */
    @Bean
    public Aspect15 aspect15() {
        return new Aspect15();
    }
    @Bean("bean1")
    public Service15 bean1() {
        return new Service15("jyx");
    }
    @Bean("bean2")
    public Service15 bean2() {
        return new Service15("ssw");
    }
    @Bean("bean3")
    public Service15 bean3() {
        return new Service15("yk");
    }
}
