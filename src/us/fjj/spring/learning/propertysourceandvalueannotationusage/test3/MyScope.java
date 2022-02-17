package us.fjj.spring.learning.propertysourceandvalueannotationusage.test3;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(BeanMyScope.SCOPE_MY)//这里将Scope指定为自定义的Scope
public @interface MyScope {
    /**
     * @see Scope#proxyMode()
     */
    ScopedProxyMode proxyMode() default ScopedProxyMode.TARGET_CLASS;
    /**
     * 这里参数名称也是proxyMode，类型也是ScopedProxyMode，而@Scope注解中有个和这个同类型的参数，spring容器解析的时候，会将这个参数的值赋给@MyScope注解上面的@Scope注解的proxyMode参数，所以此处我们设置proxyMode值，最后的效果就是直接改变了@Scope中proxyMode参数的值，此处默认值为ScopeProxyMode.TARGET_CLASS
     */
}
