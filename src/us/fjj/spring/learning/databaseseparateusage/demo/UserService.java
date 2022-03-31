package us.fjj.spring.learning.databaseseparateusage.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import us.fjj.spring.learning.databaseseparateusage.base.DsType;
import us.fjj.spring.learning.databaseseparateusage.base.IService;

import java.util.List;
import java.util.Optional;

/**
 * 这个类就相当于我们平时写的service,我是为了方便，直接在里面使用了JDBCTemplate来操作数据库，真实的项目操作db会放在dao里面。
 * getUserNameById方法： 通过id查询name。
 * insert方法： 插入数据，这个内部的所有操作都会走主库，为了验证是不是查询也会走主库，插入数据之后，我们会调用this.UserService.getUserNameById(id, DsType.SLAVER)方法去执行查询操作，第二个参数故意使用 SLAVE，如果查询有结果说明走的是主库，否则走的是从库，这里为什么需要通过this.userService来调用getUserNameById？
 * this.userService最终是个代理对象，通过代理对象访问其内部的方法，才会被读写分离的拦截器拦截。
 */
@Component
public class UserService implements IService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserService userService;

    /**
     * 最后一个参数决定了是主库还是从库，具体看ReadWriteInterceptor
     * @param id
     * @param dsType
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getUserNameById(long id, DsType dsType) {
        String sql = "select name from t_user where id = ?";
        List<String> list  = this.jdbcTemplate.queryForList(sql, String.class, id);
        return null != list && list.size() > 0 ? list.get(0): null;
    }

    //这个insert方法会走主库，内部的所有操作都会走主库
    @Transactional
    public void insert(long id, String name) {
        System.out.println(String.format("插入数据{id: %s, name: %s}", id, name));
        this.jdbcTemplate.update("insert into t_user (id, name) value (?, ?)", id, name);
        String userName = this.userService.getUserNameById(id, DsType.SLAVER);
        System.out.println("查询结果: " + userName);
    }
}
