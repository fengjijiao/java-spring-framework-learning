package us.fjj.spring.learning.databaseseparateusage.base;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 读写分离数据源，继承ReadWriteDataSource,注意其内部的determineCurrentLookupKey方法，从上面的ThreadLocal中获取当前需要走主库还是从库的标志。
 */
public class ReadWriteDataSource extends AbstractRoutingDataSource {
    /**
     * 这里决定走哪个库， 默认走哪个库在MainConfig dataSource中设置
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DsTypeHolder.getDsType();
    }
}
