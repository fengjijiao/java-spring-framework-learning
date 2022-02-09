package us.fjj.spring.learning.lookupmethod;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LookupMethodTest {
    /**
     * 使用lookup-method的方式实现在单例bean中使用多例bean
     * 当调用serviceB中的getServiceA方法时会被spring拦截，根据配置文件中的bean取查找对应id的bean，然后将这个bean作为返回值返回。
     * 由于拦截返回的过程是spring去实现的，达到了解耦和的目的
     * 这个地方底层是使用cglib代理实现的，后面有篇文章会详细介绍代理的2种实现，到时候大家注意下，spring中有很多牛逼的功能都是靠代理实现的。
     * 除了这两种方法以外（applicationcontextaware和lookup-method）还有一个功能同样可以解决上面单例bean中用到多例bean的问题，也就是下面我们要说的replaced-method（方法替换）.
     * @param args
     */
    public static void main(String[] args) {
        String path = "LookupMethodBean.xml";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(path);
        for (int i = 0; i < 3; i++) {
            ServiceB serviceB = context.getBean(ServiceB.class);
            System.out.println("serviceB:"+serviceB);
            serviceB.say();
        }
        /**
         * serviceB:us.fjj.spring.learning.lookupmethod.ServiceB$$EnhancerBySpringCGLIB$$a3f966da@3c9c0d96
         * ServiceA:us.fjj.spring.learning.lookupmethod.ServiceA@3a4621bd
         * serviceB:us.fjj.spring.learning.lookupmethod.ServiceB$$EnhancerBySpringCGLIB$$a3f966da@3c9c0d96
         * ServiceA:us.fjj.spring.learning.lookupmethod.ServiceA@31dadd46
         * serviceB:us.fjj.spring.learning.lookupmethod.ServiceB$$EnhancerBySpringCGLIB$$a3f966da@3c9c0d96
         * ServiceA:us.fjj.spring.learning.lookupmethod.ServiceA@4ed5eb72
         */
    }
}
