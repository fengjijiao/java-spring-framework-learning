package us.fjj.spring.learning.annotationusage;

import java.util.Map;

@Ann9("用在了类上")
@Ann9_1(0)
public class UseAnnotation9<@Ann9("用在了类变量类型V1上") @Ann9_1(1) V1, @Ann9("用在了类变量类型V2上") @Ann9_1(2) V2> {
    @Ann9("用在了字段上")
    @Ann9_1(3)
    private String name;

    private Map<@Ann9("用在了泛型类型上，String") @Ann9_1(4) String, @Ann9("用在了泛型类型上，Integer") @Ann9_1(5) Integer> map;

    @Ann9("用在了字段上")
    @Ann9_1(9)
    private String animal;

    @Ann9("用在了构造方法上")
    @Ann9_1(6)
    public UseAnnotation9(String name) {
        this.name = name;
    }

    @Ann9("用在了返回值上")
    @Ann9_1(7)
    public String m1(@Ann9("用在了参数上") @Ann9_1(8) String name) {
        return null;
    }
}
