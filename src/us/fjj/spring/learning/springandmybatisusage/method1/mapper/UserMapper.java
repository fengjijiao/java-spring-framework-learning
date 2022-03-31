package us.fjj.spring.learning.springandmybatisusage.method1.mapper;

import org.apache.ibatis.annotations.Mapper;
import us.fjj.spring.learning.springandmybatisusage.method1.mybatis.model.UserModel;

import java.util.List;

/**
 * 这个是mapper接口，类上面需要添加@Mapper注解，用来标注这是一个Mapper接口。
 */
@Mapper
public interface UserMapper {
    void insert(UserModel userModel);
    List<UserModel> getList();
}
