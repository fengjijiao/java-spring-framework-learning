package us.fjj.spring.learning.internationalization;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import us.fjj.spring.learning.internationalization.test1.MainConfig1;
import us.fjj.spring.learning.internationalization.test3.MainConfig3;
import us.fjj.spring.learning.internationalization.test4.MainConfig4;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * 国际化
 * spring中国际化是通过MessageSource这个接口来支持的
 * @see org.springframework.context.MessageSource
 *
 * 常见3个实现类
 * 1.ResourceBundleMessageSource: 这个是基于Java的ResourceBundle基础类实现，允许仅通过资源名加载国际化资源
 * 2.ReloadableResourceBundleMessageSource: 这个功能和第一个类似，多了定时刷新功能，允许在不重启系统的情况下，更新资源的信息
 * 3.StaticMessageSource: 它允许通过变成的方式提供国际化信息，可以通过这个实现db中存储国际化信息的功能。
 *
 * 带有ApplicationContext字样的spring容器，一般是继承了AbstractApplicaionContext接口的，而这个接口实现类上面说的国际化接口MessageSource，所以通常我们用到的ApplicationContext类型的容器都自带了国际化的功能。
 *
 * 在ApplicationContext类型的容器中使用国际化的3个步骤
 * 1.创建国际化文件
 * 2.向容器中注册一个MessageSource类型的bean，bean名称必须为：messageSource
 * 3.调用AbstractApplicationContext中的getMessage来获取国际化信息，其内部将交给第二步中注册的messageSource名称的bean进行处理
 *
 */
public class InternationalizationTest {
    /**
     * 案例1
     */
    @Test
    public void test1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MainConfig1.class);
        context.refresh();
        //未指定Locale,此时系统会取默认的locale对象，本地默认的值中文【中国】，即zh_CN
        System.out.println(context.getMessage("name", null, null));
        System.out.println(context.getMessage("name", null, Locale.CHINA));
        System.out.println(context.getMessage("name", null, Locale.UK));
        /**
         * ??
         * ??
         * sheep yang
         */
    }
    /**
     * 动态参数使用
     * 配置文件中的personal_introduction，包含了{0},{1},{0}这部分内容，这个就是动态参数，调用getMessage的时候，通过第二个参数传递过去
     */
    @Test
    public void test2() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MainConfig1.class);
        context.refresh();
        //未指定Locale,此时系统会取默认的locale对象，本地默认的值中文【中国】，即zh_CN
        System.out.println(context.getMessage("personal_introduction", new String[]{"ssw", "jyx"}, null));
        System.out.println(context.getMessage("personal_introduction", new String[]{"ssw", "jyx"}, Locale.CHINA));
        System.out.println(context.getMessage("personal_introduction", new String[]{"ssw", "jyx"}, Locale.UK));
        /**
         * ???????ssw?jyx
         * ???????ssw?jyx
         * personal introduction: ssw,jyx,ssw
         */
    }
    /**
     * 监控国际化文件的变化
     * 用ReloadableResourceBundleMessageSource这个类，功能和上面的案例中的ResourceBundleMessageSource类似，不过多了个可以监控国际化资源文件变化的功能，有个方法来设置缓存时间L
     * public void setCacheMillis(long cacheMillis)
     * -1: 表示永远缓存
     * 0: 每次获取国际化信息的时候，都会重新读取国际化文件
     * 大于0：上次读取配置文件的时间距离当前时间超过了这个时间，重新读取国际化文件
     *
     * 还有个按秒设置缓存时间的方法setCacheSeconds
     *
     */
    @Test
    public void test3() throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MainConfig3.class);
        context.refresh();
        //输出2次
        for (int i = 0; i < 2; i++) {
            System.out.println(context.getMessage("address", null, Locale.CHINA));
            TimeUnit.SECONDS.sleep(5);//在5s内修改中文国际化文件内address的值即可
        }
    }

    /**
     * 国际化信息存在db中
     * StaticMessageSource，这个类允许通过编程的方式提供国际化信息，我们通过这个类来实现从db中获取国际化信息的功能。
     * 这个类有2个方法比较重要：
     * public void addMessage(String code, Locale locale, String msg);
     * public void addMessages(Map<String, String> message, Locale locale);
     * 通过这两个方法来添加国际化配置信息
     *
     */
    @Test
    public void test4() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MainConfig4.class);
        context.refresh();
        System.out.println(context.getMessage("desc", null, Locale.CHINA));
        System.out.println(context.getMessage("desc", null, Locale.UK));
        /**
         * 我是来自db的信息
         * this message from db
         */
    }

    /**
     * bean名称为什么必须是messageSource
     * 上面容器启动的时候会调用refresh方法，过程如下:
     * @see AbstractApplicationContext#refresh() 内部会调用（下）
     * @see AbstractApplicationContext#initMessageSource() //这个方法用来初始化MesssageSource，方法内部会查找当前容器中是否有messageSource名称的bean，如果有就将其作为处理国际化的对象
     * 如果没有找到，此时会注册一个名称为messageSource的MessageSouecw
     *
     */

    /**
     * 自定义bean中使用国际化
     * 只需要实现下面这个接口，spring容器会自动调用这个方法，将MessageSource注入，然后我们就可以使用MessageSource获取国际化信息了。
     * public interface MessageSourceAware extends Aware {
     *     void setMessageSource(MessageSource messageSource);
     * }
     */

    /**
     * 总结
     * 介绍了国际化的使用，涉及到了java中的Locale类，这个类用来表示语言国家信息，获取国际化信息的时候需要携带这个参数，spring中通过MessageSource接口来支持国际化的功能，有3个常用的实现类需要了解，StaticMessageSource支持硬编码的方式配置国际化信息。
     *
     * 如果需要spring支撑国际化，需要注册一个bean名称为messageSource的MessageSource。
     */
}
