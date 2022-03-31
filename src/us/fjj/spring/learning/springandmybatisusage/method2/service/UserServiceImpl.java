package us.fjj.spring.learning.springandmybatisusage.method2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import us.fjj.spring.learning.springandmybatisusage.method1.mapper.UserMapper;
import us.fjj.spring.learning.springandmybatisusage.method1.mybatis.model.UserModel;

import java.util.List;

/**
 * IUserService的实现类，内部将UserMapper通过@AutoWired注入进来，通过userMapper来访问数据库，userMapper实际上是mybatis创建的一个代理对象。
 *
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserModel insert(UserModel userModel) {
        userMapper.insert(userModel);
        return userModel;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<UserModel> getList() {
        return userMapper.getList();
    }
}
