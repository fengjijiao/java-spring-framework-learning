package us.fjj.spring.learning.importannotationusage.test1;

import org.springframework.context.annotation.Import;

//import中导入了两个普通的类：Service1和Service2，这两个类会被自动注册到容器中
@Import({Service1.class, Service2.class})
public class MainConfig1 {
}
