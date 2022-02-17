package us.fjj.spring.learning.propertysourceandvalueannotationusage.test3;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

/**
 * @see MyScope 作用域的实现
 */
public class BeanMyScope implements Scope {
    public static final String SCOPE_MY = "my";//定义一个常量，作为定义域的值

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {//自定义作用域会自动调用这个get方法来创建bean对象，这个地方输出了一行日志，为了方便看效果
        System.out.println("BeanMyScope >>> get:" + name);
        return objectFactory.getObject();//通过objectFactory.getObject()获取bean实例返回。
    }

    @Override
    public Object remove(String name) {
        return null;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {

    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
