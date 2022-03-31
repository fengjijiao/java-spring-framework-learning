package us.fjj.spring.learning.databaseseparateusage.base;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 这个注解用来开启读写分离的功能，@1通过@Import将ReadWriteConfiguration导入到Spring容器了，这样就会自动启动读写分离的功能，业务中需要使用读写分离，只需要在spring配置类中加上@EnableReadWrite注解就可以了。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ReadWriteConfiguration.class)//@1
public @interface EnableReadWrite {
}
