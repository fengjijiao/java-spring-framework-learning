package us.fjj.spring.learning.configurationannotationandbeanannotation;

//@Bean注解类似于bean xml配置文件中的bean元素，用来在spring容器中注册一个bean。

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Bean注解用在方法上，表示通过方法来定义一个bean，默认将方法名称作为bean名称，将方法返回值作为bean对象，注册到spring容器中。
 *
 */
@Configuration
public class ABean {
    //bean名称为方法默认值：user1
    @Bean
    public User user1() {
        return new User();
    }

    //bean名称通过value指定了：user2Bean
    @Bean("user2Bean")
    public User user2() {
        return new User();
    }

    //bean名称为：user3Bean，2个别名：[user3BeanAlias1,user3BeanAlias2]
    @Bean({"user3Bean", "user3BeanAlias1", "user3BeanAlias2"})
    public User user3() {
        return new User();
    }
}
