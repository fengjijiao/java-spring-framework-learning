package us.fjj.spring.learning.propertysourceandvalueannotationusage.test4;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.concurrent.ConcurrentHashMap;

public class BeanRefreshScope implements Scope {
    public static final String SCOPE_REFRESH = "refresh";
    private static final BeanRefreshScope INSTANCE = new BeanRefreshScope();
    private ConcurrentHashMap<String, Object> beanMap = new ConcurrentHashMap<>();

    private BeanRefreshScope() {
    }

    public static BeanRefreshScope getInstance() {
        return INSTANCE;
    }

    /**
     * 清理已缓存的所有bean
     */
    public static void clean() {
        INSTANCE.beanMap.clear();
    }

    /**
     * 支持从缓存中获取bean对象
     * @param name
     * @param objectFactory
     * @return
     */
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Object bean = beanMap.get(name);
        if (bean == null) {
            bean = objectFactory.getObject();
            beanMap.put(name, bean);
        }
        return bean;
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
