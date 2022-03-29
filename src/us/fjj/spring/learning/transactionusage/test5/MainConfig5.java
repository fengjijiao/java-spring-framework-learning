package us.fjj.spring.learning.transactionusage.test5;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableTransactionManagement
@Configuration
@ComponentScan
public class MainConfig5 {
    @Bean("dataSource1")
    public DataSource dataSource1() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test1?characterEncoding=UTF-8");
        dataSource.setUsername("er");
        dataSource.setPassword("asdeg");
        dataSource.setInitialSize(5);
        return dataSource;
    }

    @Bean("transactionManager1")
    public PlatformTransactionManager transactionManager1(@Qualifier("dataSource1") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("jdbcTemplate1")
    public JdbcTemplate jdbcTemplate1(@Qualifier("dataSource1") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("dataSource2")
    public DataSource dataSource2() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test2?characterEncoding=UTF-8");
        dataSource.setUsername("ef");
        dataSource.setPassword("sgsdgs");
        dataSource.setInitialSize(5);
        return dataSource;
    }

    @Bean("transactionManager2")
    public PlatformTransactionManager transactionManager2(@Qualifier("dataSource2") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("jdbcTemplate2")
    public JdbcTemplate jdbcTemplate2(@Qualifier("dataSource2") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
