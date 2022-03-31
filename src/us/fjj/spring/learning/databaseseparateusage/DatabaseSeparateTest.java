package us.fjj.spring.learning.databaseseparateusage;

/**
 * Spring实现数据库读写分离
 *
 * 1.背景
 * 大多数系统都是读多写少，为了降低数据库的压力，可以对主库创建多个从库，从库自动从主库同步数据，程序中将写的操作发生到主库，将读操作发送到从库去执行。
 *
 * 目标：通过Spring实现读写分离。
 * 读写分离需实现下面2个功能：
 * 1.读的方法，由调用者来控制具体是读从库还是主库
 * 2.有事务的方法，内部的所有读写操作都走主库
 *
 *
 * 2.3个问题
 * 1.读的方法，由调用者来控制具体是读从库还是主库，如何实现？
 * 可以给所有读的方法加一个参数，来控制读从库还是主库。
 * 2.数据源如何路由？
 * spring-jdbc包中提供了一个抽象类：AbstractRoutingDataSource，实现了javax.sql.DataSource接口，我们用这个类来作为数据源类，重点是这个类可以用来做数据源的路由，可以在其内部配置多个真实的数据源，最终用哪个数据源，由开发者来决定。
 * AbstractRoutingDataSource中有个map,用来存储多个目标数据源。
 * private Map<Object, DataSource> resolvedDataSources;
 * 比如主从库可以这么存储
 * resolvedDataSources.put("master", 主库数据源);
 * resolvedDataSources.put("slave", 从库数据源);
 *
 * AbstractRoutingDataSource中还有抽象方法determineCurrentLookupKey, 将这个方法的返回值作为key到上面的resolvedDataSources中查找对应的数据源，作为当前操作db的数据源。
 * protected abstract Object determineCurrentLookupKey();
 *
 *
 * 3.读写分离在哪控制？
 * 读写分离属于一个通用的功能，可以通过Spring的aop来实现，添加一个拦截器，拦截目标方法执行之前，在目标方法执行之前，获取一下当前需要走哪个库，将这个标志存储在ThreadLocal中，将这个标志作为AbstractRoutingDataSource.determineCurrentLookupKey()方法的返回值，拦截器中在目标方法执行完毕之后，将这个标志从ThreadLocal中清除。
 *
 */
public class DatabaseSeparateTest {
}
