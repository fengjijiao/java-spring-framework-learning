package us.fjj.spring.learning.internationalization.test4;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.StaticMessageSource;

import java.util.Locale;
/**
 * 实现了spring的InitializingBean接口，重写了接口中的afterPropertiesSet方法，这个方法会在当前bean初始化之后调用，在这个方法中模拟从db中获取国际化信息，然后调用弄addMessage来配置国际化信息
 */
public class MessageSourceFromDB extends StaticMessageSource implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        //此处我们在当前bean初始化之后，模拟从db中获取国际化信息，然后调用addMessage来配置国际化信息
        this.addMessage("desc", Locale.CHINA, "我是来自db的信息");
        this.addMessage("desc", Locale.UK, "this message from db");
    }
}
