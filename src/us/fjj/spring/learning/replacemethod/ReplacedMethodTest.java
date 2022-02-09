package us.fjj.spring.learning.replacemethod;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ReplacedMethodTest {
    /**
     * 在配置中调用serviceB的getServiceA的时候，会自动调用serviceBMethodReplacer这个bean中的reimplement方法进行处理。
     * 从输出中可以看出结果和lookup-method案例的效果差不多，实现了单例bean中使用多例bean的案例。
     * 输出都有CGLIB这样的字样，说明这玩意也是通过cglib实现的。
     *
     * 总结
     * 1. lookup-method：方法查找，可以对指定的bean的方法进行拦截，然后从容器中查找指定的
     * bean作为被拦截方法的返回值
     * 2. replaced-method：方法替换，可以实现bean方法替换的效果，整体来说比lookup-method更
     * 灵活一些
     * 3. 单例bean中使用多例bean，本文中列出了3种方式，大家消化一下。
     *
     * @param args
     */
    public static void main(String[] args) {
        String path = "ReplacedMethodBean.xml";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(path);
        for (int i = 0; i < 3; i++) {
            ServiceB serviceB = context.getBean(ServiceB.class);
            serviceB.say();
        }
        /**
         * serviceB:us.fjj.spring.learning.replacemethod.ServiceB$$EnhancerBySpringCGLIB$$ca9fa94a@19835e64,serviceA:us.fjj.spring.learning.replacemethod.ServiceA@68b32e3e
         * serviceB:us.fjj.spring.learning.replacemethod.ServiceB$$EnhancerBySpringCGLIB$$ca9fa94a@19835e64,serviceA:us.fjj.spring.learning.replacemethod.ServiceA@bcef303
         * serviceB:us.fjj.spring.learning.replacemethod.ServiceB$$EnhancerBySpringCGLIB$$ca9fa94a@19835e64,serviceA:us.fjj.spring.learning.replacemethod.ServiceA@41709512
         */
    }
}
