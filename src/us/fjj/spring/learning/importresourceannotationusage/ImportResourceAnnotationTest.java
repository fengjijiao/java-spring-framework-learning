package us.fjj.spring.learning.importresourceannotationusage;

import org.junit.jupiter.api.Test;
import us.fjj.spring.learning.utils.AnnUtil;

/**
 * @ImportResource: 配置类中导入bean定义的配置文件
 * 用法：有些项目，前期可能采用xml的方式配置bean，后期可能想采用spring注解的方式来重构项目，但是有些老的模块可能还是xml的方式，spring为了方便在注解方式中兼容老的xml的方法，提供了@ImportResource注解来引入bean定义的配置文件。s
 */

/**
 * 资源文件路径的写法
 * 通常我们的项目是采用maven来组织的，配置文件一般会放在resources目录，这个目录中的文件被编译之后会在target/classes目录中。
 * spring中资源文件路径最常用的有2种写法：
 * 1.以classpath:开头：检索目标为当前项目的classes目录
 * 2.以classpath*:开头：检索目标为当前项目的classes目录，以及项目中所有jar包中的目录，如果确定jar不是检索目标，就不要使用这种方式，由于需要扫描所有jar包，所以速度相对于第一种会慢一些
 * classpath:和classpath*:后面的部分，后面的部分是确定资源文件的位置，几种常见的如下：
 * 1.相对路径方式：classpath:us/fjj/learning/demo/beans.xml
 * 2./绝对路径方式：classpath:/us/fjj/learning/demo/beans.xml
 * 3.*文件通配符方式: classpath:/us/fjj/learning/demo/beans-*.xml
 * 4.*目录通配符方式：classpath:/us/fjj/learning/demo/♥/beans-*.xml（仅包含demo下的子目录，不包含子目录的子目录）
 * 5.**递归任意子目录方式：classpath: /us/fjj/learning/demo/♥♥/beans-*.xml
 */
public class ImportResourceAnnotationTest {
    @Test
    public void test1() {
        AnnUtil.printAllBean(MainConfig1.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@305f031
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@592e843a
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@1d1f7216
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@423e4cbb
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@6e16b8b5
         * mainConfig1->us.fjj.spring.learning.importresourceannotationusage.MainConfig1@43b4fe19
         * serviceA->us.fjj.spring.learning.importresourceannotationusage.ServiceA@25ddbbbb
         * serviceB->us.fjj.spring.learning.importresourceannotationusage.ServiceB@1536602f
         */
    }
}
