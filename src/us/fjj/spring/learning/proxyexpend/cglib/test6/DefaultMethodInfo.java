package us.fjj.spring.learning.proxyexpend.cglib.test6;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultMethodInfo implements IMethodInfo {
    private Class<?> targetClass;

    public DefaultMethodInfo(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public int methodCount() {
        return targetClass.getDeclaredMethods().length;
    }

    @Override
    public List<String> methodNames() {
        return Arrays.stream(targetClass.getDeclaredMethods()).map(Method::getName).collect(Collectors.toList());
    }
}
