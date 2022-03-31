package us.fjj.spring.learning.springandmybatisusage.method1.service;

import us.fjj.spring.learning.springandmybatisusage.method1.mybatis.model.UserModel;

import java.util.List;

public interface IUserService {
    /**
     * 插入用户信息
     * @param userModel
     * @return
     */
    UserModel insert(UserModel userModel);

    /**
     * 查询用户所有记录
     * @return
     */
    List<UserModel> getList();

}
