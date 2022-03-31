package us.fjj.spring.learning.databaseseparateusage.demo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import us.fjj.spring.learning.databaseseparateusage.base.DsType;
import us.fjj.spring.learning.databaseseparateusage.base.EnableReadWrite;
import us.fjj.spring.learning.databaseseparateusage.base.ReadWriteDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring配置类
 * @1：启用读写分离
 * masterDs()方法：定义主库数据源
 * slaverDs()方法：定义从库数据源
 * dataSource(): 定义读写分离路由数据源
 * 后面还有2个方法用来定义JdbcTemplate和事务管理器，方法中都通过@Qualifier("dataSource")限定了注入的bean名称为dataSource；即注入了上面dataSource()返回的读写分离路由数据源。
 *
 */
@EnableReadWrite//@1
@Configuration
@ComponentScan
public class MainConfig {
    //主库数据源
    @Bean
    public DataSource masterDs() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test4?characterEncoding=UTF-8");
        dataSource.setUsername("w");
        dataSource.setPassword("sdf");
        dataSource.setInitialSize(5);
        return dataSource;
    }

    //从库数据源
    @Bean
    public DataSource slaverDs() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test5?characterEncoding=UTF-8");
        dataSource.setUsername("er");
        dataSource.setPassword("sdfr");
        dataSource.setInitialSize(5);
        return dataSource;
    }

    //读写分离路由数据源
    @Bean
    public ReadWriteDataSource dataSource() {
        ReadWriteDataSource dataSource = new ReadWriteDataSource();
        //设置主库为默认的库，当路由的时候没有在dataSource那个map中找到对应的数据源的时候，会使用这个默认的数据源
        dataSource.setDefaultTargetDataSource(masterDs());
        //设置多个目标库
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DsType.MASTER, this.masterDs());
        targetDataSources.put(DsType.SLAVER, this.slaverDs());
        dataSource.setTargetDataSources(targetDataSources);
        return dataSource;
    }

    //JdbcTempalte, dataSource为上面定义的注入读写分离的数据源
    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    //定义事务管理器，dataSource为上面定义的注入读写分离的数据源
    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
