package us.fjj.spring.learning.springandmybatisusage.multidatasource;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@MapperScan(annotationClass = Mapper.class, sqlSessionFactoryRef = ModuleSpringConfig.SQL_SESSION_FACTORY_BEAN_NAME)
@ComponentScan
@EnableTransactionManagement
public class ModuleSpringConfig {
    public final static String DATASOURCE_BEAN_NAME = "dataSourceModule1";
    public final static String TRANSACTION_MANAGER_BEAN_NAME = "transactionManagerModule1";
    public final static String SQL_SESSION_FACTORY_BEAN_NAME = "sqlSessionFactoryModule1";

    //定义数据源
    @Bean
    public DataSource dataSourceModule1() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test5?characterEncoding=UTF-8");
        dataSource.setUsername("e");
        dataSource.setPassword("sdfs");
        dataSource.setInitialSize(5);
        return dataSource;
    }

    //定义事务管理器
    @Bean
    public TransactionManager transactionManagerModule1(@Qualifier(DATASOURCE_BEAN_NAME) DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    //定义SqlSessionFactoryBean,用来创建SqlSessionFactory
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBeanModule1(@Qualifier(DATASOURCE_BEAN_NAME) DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }




















}
