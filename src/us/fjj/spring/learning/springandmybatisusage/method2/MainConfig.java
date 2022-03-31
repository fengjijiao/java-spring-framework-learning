package us.fjj.spring.learning.springandmybatisusage.method2;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import us.fjj.spring.learning.springandmybatisusage.method1.mapper.UserMapper;

import javax.sql.DataSource;
import java.io.IOException;

@EnableTransactionManagement
@ComponentScan
@Configuration
@MapperScan(basePackageClasses = {UserMapper.class}, annotationClass = Mapper.class)//@MapperScan这个注解是关键，会扫描标记有@Mapper注解的Mapper接口，将其注册到spring容器中
public class MainConfig {
    //定义数据源
    @Bean
    public DataSource dataSource() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test5?characterEncoding=UTF-8");
        dataSource.setUsername("e");
        dataSource.setPassword("dfs");
        dataSource.setInitialSize(5);
        return dataSource;
    }

    //定义事务管理器
    @Bean
    public TransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    //定义SqlSessionFactoryBean,用来创建SqlSessionFactory

    /**
     * 与方式一相比，如下，更简洁了，不需要在指定mapper xml 的位置了。这里需要注意一点，方式二中将mapper xml 文件和mapper接口放在一个目录的时候，这2个文件名称必须一样，这样在定义SqlSessionFactoryBean的时候才不需要指定mapper xml的位置。
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    /**
     * 这个类中有2个关键信息：
     * 1.@MapperScan注解：这个注解是关键，会扫描标记有@Mapper注解的Mapper接口类，然后给Mapper接口生成代理对象，将其注册到spring容器中，这个注解有几个属性需要注意。
     * value或者basePackages: String类型的数组，用来指定扫描的包
     * basePackageClasses: 可以指定一个类，扫描范围为这个类所在的包及其所有子包
     * sqlSessionFactoryRef: 用来指定sqlSessionFactory的bean名称， 当我们的系统中需要操作多个库的时候，每个库对应一个SqlSessionFactory,此时可以通过这个属性指定需要使用哪个SqlSessionFactory.
     *
     * 2.定义SqlSessionFactoryBean: 通过名字大家可以看出这个是用来生成SqlSessionFactory的，内部需要指定数据源和本地mapper xml的未知，我们将mapper xml文件放在resources/mapper文件中，此处我们采用通配符的方式，加载classpath中的mapper目录及子目录中的所有xml文件。
     *
     * Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:mapper\/**\/*.xml");
     * sqlSessionFactoryBean.setMapperLocations(resources);
     */
}
