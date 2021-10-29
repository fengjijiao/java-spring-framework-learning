package us.fjj.spring.learning;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import us.fjj.spring.learning.annotationdemo.UserController;

public class MainApp {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");//该类用于加载Spring配置文件、创建和初始化所有对象，也就是下面配置文件中提到的bean。
        //first start
        HelloWorld obj = (HelloWorld) context.getBean("helloWorld");//该方法用来获取bean，返回值类型为Object，通过强制类型转换为HelloWorld的实例对象，根据该对象调用类中的方法。
        obj.getMessage();
        //运行成功后
        //message:Hello World!
        //至此，我们就成功的创建了第一个spring应用程序。
        //first end

        //依赖注入 start
        //1.构造函数方式
        DependencyInjection.Person person = (DependencyInjection.Person) context.getBean("person");
        person.man();
        //在Man的构造函数内
        //在Person的构造函数内
        //name: laokai,age: 3
        //2.setter方式
        DependencyInjection2.Person person2 = (DependencyInjection2.Person) context.getBean("person2");
        person2.man();
        //在Man的无参构造函数内
        //在Person的无参构造函数内
        //name: laokai,age: 3
        //依赖注入 end

        //注入内部bean start
        DependencyInjection2.Person person3 = (DependencyInjection2.Person) context.getBean("person3");
        person3.man();
        //在Man的有参构造函数内
        //在Person的无参构造函数内
        //name: laokai,age: 3
        //注入内部bean end

        //注入集合 start
        //list:用于注入list类型的值，允许重复
        //set:用于注入set类型的值，不允许重复
        //map:用于注入key-value的集合，其中key-value可以是任意类型
        //props:用于注入key-value的集合，其中key-value都是字符串类型
        JavaCollection jc = (JavaCollection) context.getBean("javaCollection");
        jc.getList();
        jc.getSet();
        jc.getMap();
        jc.getProps();
        //list: [us.fjj.spring.learning.DependencyInjection$Man@1f3f4916, us.fjj.spring.learning.DependencyInjection2$Man@794cb805, www.google.com, www.google.com.hk, www.google.com.sg, www.google.co.uk, www.google.tk, www.google.tk, www.google.tk]
        //set: [us.fjj.spring.learning.DependencyInjection$Man@1f3f4916, us.fjj.spring.learning.DependencyInjection2$Man@794cb805, www.google.com, www.google.com.hk, www.google.com.sg, www.google.co.uk, www.google.co.au, www.google.tk]
        //map: {0=us.fjj.spring.learning.DependencyInjection$Man@1f3f4916, us.fjj.spring.learning.DependencyInjection$Man@1f3f4916=us.fjj.spring.learning.DependencyInjection2$Man@794cb805, 1=www, 2=google, 3=com}
        //props: {one=www, two=google, three=tk}
        //注入集合 end

        //自动装配 start
        //自动装配需要配置bean的autowire属性(no/byName/byType/constructor/autodetect)。
        //no: 默认值，不自动装配，bean依赖必须通过ref元素定义。
        //byName: 根据Property的name自动装配，如果一个bean的name和另一个bean的property的name相同，则自动装配这个bean到property中。
        //byType: 根据Property的数据类型（Type）自动装配，如果一个Bean的数据类型兼容另一个Bean中的Property的数据类型，则自动装配。
        //constructor: 类似于byType，根据构造方法参数的数据类型，进行byType模式自动装配。
        //autodetect: 如果bean中有默认的构造方法，则用constructor模式，否则用byType模式。
        //自动装配 end

        //@Qualifier注释 start
        QualifierDemo.Profile profile = (QualifierDemo.Profile) context.getBean("profile");
        profile.printId();
        profile.printName();
        //ID: 12138
        //Name: loakia
        //@Qualifier注释 end

        //注解Demo start
        UserController userController = (UserController) context.getBean("userController");
        userController.outContent();
        //spring!!!
        //1.U should!
        //2.U shoudl!!!
        //注解Demo end
    }
}
