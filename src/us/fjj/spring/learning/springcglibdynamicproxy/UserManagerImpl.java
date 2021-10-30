package us.fjj.spring.learning.springcglibdynamicproxy;

public class UserManagerImpl implements UserManager {

    @Override
    public void addUser(String username, String password) {
        System.out.println("正在执行添加用户方法");
        System.out.println("用户名称："+username+"，密码："+password);
    }

    @Override
    public void delUser(String username) {
        System.out.println("正在执行删除用户方法");
        System.out.println("用户名称："+username);
    }
}
