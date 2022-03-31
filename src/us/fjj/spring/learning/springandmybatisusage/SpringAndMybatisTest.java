package us.fjj.spring.learning.springandmybatisusage;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import us.fjj.spring.learning.springandmybatisusage.method1.MainConfig;
import us.fjj.spring.learning.springandmybatisusage.method1.mybatis.model.UserModel;
import us.fjj.spring.learning.springandmybatisusage.method1.service.IUserService;

import java.util.List;

/**
 * Spring集成MyBatis
 *
 * 目前注解的方式我们用的比较多，所以主要介绍注解的方式，xml的方式这里就暂时不介绍了。
 * 注解的方式mybatis集成spring主要有2种方式：
 * 方式一：mapper xml文件放在resource目录，和Mapper接口不在一个目录的情况
 * 方式二：mapper xml文件和Mapper接口在同一个目录
 *
 * 还会介绍另一个点： 多数据库的时候，如何配置？
 *
 *
 */
public class SpringAndMybatisTest {
    /**
     * 方式一
     */
    @Test
    public void insert() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        IUserService userService = context.getBean(IUserService.class);
        UserModel userModel = UserModel.builder().name("张三").build();
        userService.insert(userModel);
        System.out.println(userModel);
        /**
         * UserModel(id=2, name=张三)
         */
    }

    @Test
    public void getList() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        IUserService userService = context.getBean(IUserService.class);
        List<UserModel> list = userService.getList();
        System.out.println(list);
        /**
         * [UserModel(id=1, name=slaver库), UserModel(id=2, name=张三)]
         */
    }


    /**
     * 小结
     * 主要分为3步
     * 1.Mapper接口上添加@Mapper注解
     * 2.定义mapper xml文件，如将user.xml文件放在了resources\mapper目录
     * 3.spring配置类中添加@MapperScan注解，用来扫描@Mapper标注的类，将其注册到spring容器中
     * @MapperScan(basePackageClasses={UserMapper.class}, annotationClass=Mapper.class)
     * 4.定义SqlSessionFactoryBean
     * 见MainConfig.java -> SqlSessionFactoryBean
     */


    /**
     * 方式二
     * 除了要修改MainConfig中关于SqlSessionFactoryBean的配置以外
     * 还需要在pom.xml中配置，这样maven打包的时候，才会将src/main/java目录下的xml打包到目录内，否则打包之后这些xml都丢失。
     *
     * <build>
     *     <resources>
     *         <resource>
     *             <directory>${project.basedir}/src/main/java</directory>
     *             <includes>
     *                 <include>**//*//xml</include>
     *             </includes>
     *         </resource>
                <resource>
                <directory>${project.basedir}/src/main/resources</directory>
                <includes>
                    <include>**//*</include>
                </includes>
                </resource>
     *     </resources>
     * </build>
     */


    /**
     * 小结
     * 如果使用方式二需要注意下面2点：
     * 1.mapper接口和mapper xml 必须同名，且放在同一个目录
     * 2.需要在pom.xml文件中加入指定配置，否则打包之后xml丢失
     */


    /**
     * 集成多个数据源
     * 有时候我们的系统中需要用到多个数据源，每个数据源对应一个SqlSessionFactory，@MapperScan注解中可以通过SqlSessionTemplateRef来指定SqlSessionFactory的bean名称。
     *
     * 多数据源开发步骤
     * 1.@MapperScan中指定SqlSessionFactory的bean名称
     * 每个db对应一个模块，通过包区分不同的模块，每个模块中指定一个spring的配置类，配置类需配置3个bean：数据源、事务管理器、SqlSessionFactory，下面是一个模块的Spring配置类，注意下面代码的@MapperScan注解，当系统中有多个sqlSessionFactory的时候需要通过sqlSessionFactoryRef属性来指定sqlSessionFactory的bean名称。
     *
     * 由于有多个数据源，所以代码中需要通过@Qualifier(DATASOURCE_BEAN_NAME)来限定注入的dataSource的bean名称。
     *
     * 2.指定事务管理器
     * 每个db对应一个dataSource，每个dataSource需要指定一个事务管理器，通过@Transaction注解的transactionManager属性指定事务管理器，如下：
     * @Transactional(transactionManager = Module1SpringConfig.TRANSACTION_MANAGER_BEAN_NAME, rollbackFor = Exception.class)
     *
     *
     * 案例
     * ...
     */


    /**
     * 小结
     * 系统中集成多个数据源这种方式，大家了解即可，不建议这么使用，通常一个业务库对应一个module，模块与模块之间相互隔离，独立部署，便于维护扩展。
     */








































}
