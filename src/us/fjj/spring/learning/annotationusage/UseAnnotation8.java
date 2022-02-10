package us.fjj.spring.learning.annotationusage;

import java.util.Map;

@Ann8("用在了类上")
public class UseAnnotation8<@Ann8("用在了类变量类型V1上") V1, @Ann8("用在了类变量类型V2上") V2> {
    private Map<@Ann8("用在了泛型类型上") V1, V2> map;

    public <@Ann8("用在了参数上") T> T m1(T name) {
        return name;
    }
}
