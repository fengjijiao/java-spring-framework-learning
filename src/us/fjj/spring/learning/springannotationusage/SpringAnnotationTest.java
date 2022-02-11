package us.fjj.spring.learning.springannotationusage;

import org.springframework.core.annotation.AnnotatedElementUtils;

public class SpringAnnotationTest {
    public static void main(String[] args) throws NoSuchFieldException {
        //@AliceFor注解：解决注解继承后需要变更父类注解的值的时候
        //AnnotatedElementUtils是spring提供的一个查找注解的工具类
        System.out.println(AnnotatedElementUtils.getMergedAnnotation(UseAliceForAnnotation.Ires.class, UseAliceForAnnotation.A1.class));
        System.out.println(AnnotatedElementUtils.getMergedAnnotation(UseAliceForAnnotation.Ires.class, UseAliceForAnnotation.A2.class));
        /**
         * 输出：
         * @us.fjj.spring.learning.springannotationusage.UseAliceForAnnotation$A1(name=i am from Ires, value=A1)
         * @us.fjj.spring.learning.springannotationusage.UseAliceForAnnotation$A2(a1Value=i am from Ires, value=i am from Ires.)
         */
        /**
         * @AliceFor相当于给某个注解指定别名，即将A2的注解中a1Value参数作为A1中name参数的别名，当给A2的a1Value设置值的时候，就相当于给A1的name设置值，有个前提是@AliceFor注解的annotation参数指定的注解需要加载到当前注解上
         */

        //同一个注解中使用@AliceFor test
        System.out.println(AnnotatedElementUtils.getMergedAnnotation(UseAliceForAnnotation.Ires2.class, UseAliceForAnnotation.A3.class));
        System.out.println(AnnotatedElementUtils.getMergedAnnotation(UseAliceForAnnotation.Ires2.class.getDeclaredField("name"), UseAliceForAnnotation.A3.class));
        /**
         * 输出：
         * @us.fjj.spring.learning.springannotationusage.UseAliceForAnnotation$A3(v1=i am from Ires2., v2=i am from Ires2.)
         * @us.fjj.spring.learning.springannotationusage.UseAliceForAnnotation$A3(v1=i am from name field., v2=i am from name field.)
         */
        /**
         * 从输出中可以看出v1和v2的值始终相等，但是若同时设置v1和v2的值那么就将报错。
         */

        //AliceFor
        /**
         * 1.AliceFor注解中value和attribute互为别名，随便设置一个，同时会给另一个设置相同的值。
         * 2.当AliceFor中不指定value或者attribute的时候，自动将@AliceFor修饰的参数作为value和attribute的值
         *
         */
        System.out.println(AnnotatedElementUtils.getMergedAnnotation(UseAliceForAnnotation.Ires3.class, UseAliceForAnnotation.A4.class));
        System.out.println(AnnotatedElementUtils.getMergedAnnotation(UseAliceForAnnotation.Ires3.class, UseAliceForAnnotation.A1.class));
        /**
         *@us.fjj.spring.learning.springannotationusage.UseAliceForAnnotation$A4(name=i am from Ires3.)
         * @us.fjj.spring.learning.springannotationusage.UseAliceForAnnotation$A1(name=i am from Ires3., value=A1)
         */


    }
}
