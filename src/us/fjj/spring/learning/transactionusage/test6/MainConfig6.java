package us.fjj.spring.learning.transactionusage.test6;

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
public class MainConfig6 {
    @Bean
    public DataSource dataSource() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test1?characterEncoding=UTF-8");
        dataSource.setUsername("g");
        dataSource.setPassword("re");
        dataSource.setInitialSize(5);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate1(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate2(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager1(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager2(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
