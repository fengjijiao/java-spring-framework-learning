package us.fjj.spring.learning.annotationdemo;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller("userController")
public class UserController {
    @Resource(name="userService")
    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void outContent() {
        userService.outContent();
        System.out.println("2.U shoudl!!!");
    }
}
