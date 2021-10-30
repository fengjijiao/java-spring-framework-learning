package us.fjj.spring.learning.springjdkdynamicproxy;

public interface UserManager {
    //新增用户抽象方法
    void addUser(String username, String password);
    //删除用户抽象方法
    void delUser(String username);
}
