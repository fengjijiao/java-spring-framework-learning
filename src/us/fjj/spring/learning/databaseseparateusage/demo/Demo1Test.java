package us.fjj.spring.learning.databaseseparateusage.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import us.fjj.spring.learning.databaseseparateusage.base.DsType;

public class Demo1Test {
    UserService userService;

    @BeforeEach
    public void before() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MainConfig.class);
        context.refresh();
        this.userService = context.getBean(UserService.class);
    }

    @Test
    public void test1() {
        System.out.println(this.userService.getUserNameById(1, DsType.MASTER));
        System.out.println(this.userService.getUserNameById(1, DsType.SLAVER));
        /**
         * master库
         * slaver库
         */
        /**
         * test1方法执行2此查询，分别查询主库和从库
         * 可以看到确实实现了，由开发者自己控制具体走主库还是从库。
         */
    }

    @Test
    public void test2() {
        this.userService.insert(555L, "张三");
        /**
         *插入数据{id: 555, name: 张三}
         * 查询结果: 张三
         *
         * 可以看到查询到了刚刚插入的数据，说明insert中所有操作都走主库。
         */
    }
}
