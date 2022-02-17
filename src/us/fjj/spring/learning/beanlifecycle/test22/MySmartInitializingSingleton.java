package us.fjj.spring.learning.beanlifecycle.test22;

import org.springframework.beans.factory.SmartInitializingSingleton;

public class MySmartInitializingSingleton implements SmartInitializingSingleton {
    @Override
    public void afterSingletonsInstantiated() {
        System.out.println("afterSingletonsInstantiated()");
    }
}
