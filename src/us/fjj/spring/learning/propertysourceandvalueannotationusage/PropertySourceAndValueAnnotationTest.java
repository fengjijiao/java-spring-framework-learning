package us.fjj.spring.learning.propertysourceandvalueannotationusage;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import us.fjj.spring.learning.propertysourceandvalueannotationusage.test1.MainConfig1;
import us.fjj.spring.learning.propertysourceandvalueannotationusage.test2.DBUtil;
import us.fjj.spring.learning.propertysourceandvalueannotationusage.test2.MailConfig;
import us.fjj.spring.learning.propertysourceandvalueannotationusage.test2.MainConfig2;
import us.fjj.spring.learning.propertysourceandvalueannotationusage.test3.BeanMyScope;
import us.fjj.spring.learning.propertysourceandvalueannotationusage.test3.MainConfig3;
import us.fjj.spring.learning.propertysourceandvalueannotationusage.test3.User;
import us.fjj.spring.learning.propertysourceandvalueannotationusage.test4.BeanRefreshScope;
import us.fjj.spring.learning.propertysourceandvalueannotationusage.test4.MailService;
import us.fjj.spring.learning.propertysourceandvalueannotationusage.test4.MainConfig4;
import us.fjj.spring.learning.propertysourceandvalueannotationusage.test4.RefreshConfigUtil;
import us.fjj.spring.learning.utils.AnnUtil;

import java.util.Map;

/**
 * @PropertySource、@Value注解及动态刷新实现
 */
public class PropertySourceAndValueAnnotationTest {
    /**
     * @Value使用步骤
     * 1.使用@PropertySource注解引入配置文件
     * 2.使用@Value注解引用配置文件的值
     *
     */
    @Test
    public void test1() {
        AnnUtil.printAllBean(MainConfig1.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@410ae9a3
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@319988b0
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@d5ae57e
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@68759011
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@7e242b4d
         * mainConfig1->us.fjj.spring.learning.propertysourceandvalueannotationusage.test1.MainConfig1$$EnhancerBySpringCGLIB$$9e14bc03@305f031
         * DBConfig->DBConfig{host='127.0.0.1', port=3306, username='root', password='default', database='test01'}
         */
        /**
         * 从最后一行可以看出读取配置文件成功了
         */
    }
    /**
     * 自定义@Value数据来源
     * 解析@Value的过程:
     * 1.将@Value注解的value参数值作为Environment.resolvePlaceholders方法参数进行解析
     * 2.Environment内部会访问MutablePropertySources来解析
     * 3.MutablePropertySources内部有多个PropertySource，此时会遍历PropertySource列表，调用PropertySource.getProperty方法来解析key对应的值。
     *
     */
    @Test
    public void test2() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //关键start
        //模拟从db获取配置信息
        Map<String,Object> mailInfoFromDB = DBUtil.getMailInfoFromDB();
        //将其丢在MapPropertySource中（MapPropertySource类是spring提供的一个类，是PropertySource的子类）
        MapPropertySource mailPropertySource = new MapPropertySource("mail", mailInfoFromDB);
        //将mailPropertySource丢在Environment中的PropertySource列表的第一个中，让其优先级最高
        context.getEnvironment().getPropertySources().addFirst(mailPropertySource);
        //关键end
        context.register(MainConfig2.class);
        context.refresh();
        MailConfig mailConfig = context.getBean(MailConfig.class);
        System.out.println(mailConfig);
        /**
         * MainConfig{host='222.22.22.22', username='root', password='default'}
         */
    }

    /**
     * 如果我们将配置信息放在db中，可能我们会通过一个界面来修改这些配置信息，然后保存之后，希望系统在不重启的情况下，让这些值在spring容器中立即生效。
     */

    /**
     * 实现@Value动态刷新
     */
    @Test
    public void test3() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //将自定义作用域注册到spring容器中
        context.getBeanFactory().registerScope(BeanMyScope.SCOPE_MY, new BeanMyScope());
        context.register(MainConfig3.class);
        context.refresh();

        System.out.println("从容器中获取User对象");
        User user = context.getBean(User.class);
        System.out.println("user对象的class为："+user.getClass());

        System.out.println("多次调用user的getUsername感受效果\n");
        for (int i = 0; i < 3; i++) {
            System.out.println(String.format("第%d次调用getUsername()开始", i));
            System.out.println(user.getUsername());
            System.out.println(String.format("第%d次调用getUsername()结束", i));
        }
        /**
         * 从容器中获取User对象
         * user对象的class为：class us.fjj.spring.learning.propertysourceandvalueannotationusage.test3.User$$EnhancerBySpringCGLIB$$f3ef55d3
         * 多次调用user的getUsername感受效果
         *
         * 第0次调用getUsername()开始
         * BeanMyScope >>> get:scopedTarget.user
         * 创建User对象us.fjj.spring.learning.propertysourceandvalueannotationusage.test3.User@532a02d9
         * f1b4983c-b014-4e05-89bc-9bad31db69b5
         * 第0次调用getUsername()结束
         * 第1次调用getUsername()开始
         * BeanMyScope >>> get:scopedTarget.user
         * 创建User对象us.fjj.spring.learning.propertysourceandvalueannotationusage.test3.User@24a1c17f
         * 9ebfed6e-6d30-4526-9155-d7d08d6c1e07
         * 第1次调用getUsername()结束
         * 第2次调用getUsername()开始
         * BeanMyScope >>> get:scopedTarget.user
         * 创建User对象us.fjj.spring.learning.propertysourceandvalueannotationusage.test3.User@56102e1c
         * d35e1ed4-8bdd-4fde-8cca-71a109e54a6e
         * 第2次调用getUsername()结束
         */
        /**
         * 从输出的前2行可以看出：
         * 1.调用context.getBean(User.class)从容器中获取bean的时候，此时并没有调用User的构造函数去创建User对象。
         * 2.第二行输出的类型可以看出，getBean返回的user对象是一个cglib代理对象。
         *
         * 后面的日志输出可以看出，每次调用user.getUsername方法的时候，内部自动调用了BeanMyScope#get方法和User的构造函数。
         * 通过上面的案例可以看出，当自定义的Scope中proxyMode=ScopedProxyMode.TARGET_CLASS的时候，会给这个bean创建一个代理对象，调用代理对象的任何方法，都会调用这个自定义的作用域实现类（上面的BeanMyScope）中get方法来重新获取这个bean对象。
         */
        /**
         * (当注释掉MyScope中的proxyMode属性时，在getBean时就已经创建好User实例了，且后3次getUsername获取的值都一直，说明是同一个User对象。
         * 可见当没有CGlib时，值为静态。可以使用这种方式实现动态刷新@Value值。)
         *
         * 从容器中获取User对象
         * BeanMyScope >>> get:user
         * 创建User对象us.fjj.spring.learning.propertysourceandvalueannotationusage.test3.User@768ccdc5
         * user对象的class为：class us.fjj.spring.learning.propertysourceandvalueannotationusage.test3.User
         * 多次调用user的getUsername感受效果
         *
         * 第0次调用getUsername()开始
         * e5c3a5af-161a-420b-b3d5-d57e9c163cd2
         * 第0次调用getUsername()结束
         * 第1次调用getUsername()开始
         * e5c3a5af-161a-420b-b3d5-d57e9c163cd2
         * 第1次调用getUsername()结束
         * 第2次调用getUsername()开始
         * e5c3a5af-161a-420b-b3d5-d57e9c163cd2
         * 第2次调用getUsername()结束
         *
         */
    }
    /**
     * 动态刷新@Value值的实现
     */
    @Test
    public void test4() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getBeanFactory().registerScope(BeanRefreshScope.SCOPE_REFRESH, BeanRefreshScope.getInstance());
        context.register(MainConfig4.class);
        //刷新mail的配置到Environment
        RefreshConfigUtil.refreshMailPropertySource(context);
        context.refresh();

        MailService mailService = context.getBean(MailService.class);
        System.out.println("配置未更新的情况下输出3次");
        for (int i = 0; i < 3; i++) {
            System.out.println(mailService);
        }
        System.out.println("配置更新的情况下输出3次");
        for (int i = 0; i < 3; i++) {
            RefreshConfigUtil.updateDBConfig(context);//将新的配置加载到Environment，并且清空BeanRefreshScope中的缓存(相当于让原先的MailConfig失效)
            System.out.println(mailService);
        }
        /**
         *配置未更新的情况下输出3次
         * MailService{mailConfig=MailConfig{username='547f4f24-4496-4f13-a82f-dede1957de20', password='df2f5e10-dfeb-45ea-9abf-d39c57c34eed'}}
         * MailService{mailConfig=MailConfig{username='547f4f24-4496-4f13-a82f-dede1957de20', password='df2f5e10-dfeb-45ea-9abf-d39c57c34eed'}}
         * MailService{mailConfig=MailConfig{username='547f4f24-4496-4f13-a82f-dede1957de20', password='df2f5e10-dfeb-45ea-9abf-d39c57c34eed'}}
         * 配置更新的情况下输出3次
         * MailService{mailConfig=MailConfig{username='67f764fa-5e5b-4818-b82a-f2ed25dbdc47', password='2bb6263f-8c9b-46c9-bfc2-f8c3071162fe'}}
         * MailService{mailConfig=MailConfig{username='d520e919-a553-4750-b428-47777f353480', password='d2b08082-f112-4067-ad66-ee4e398b4f13'}}
         * MailService{mailConfig=MailConfig{username='856612d1-000f-4f08-b2d5-42ff70f381c9', password='a79d8498-c444-4891-915b-ee279c0137da'}}
         */
    }
    /**
     * 小结：
     * 动态@Value实现的关键是@Scope中proxyMode参数，值为ScopedProxyMode.TARGET_CLASS，会生成一个代理，通过这个代理来实现@Value动态刷新效果，这个地方是关键。
     *
     */
}
