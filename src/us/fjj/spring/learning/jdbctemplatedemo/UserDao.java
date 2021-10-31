package us.fjj.spring.learning.jdbctemplatedemo;

import java.util.List;

public interface UserDao {
    //初始化User表
    void createUserTable();
    //保存用户
    void saveUser(User user);
    //查询用户
    List<User> listUser();
}
