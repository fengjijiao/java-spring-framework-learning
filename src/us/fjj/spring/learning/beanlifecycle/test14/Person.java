package us.fjj.spring.learning.beanlifecycle.test14;

public class Person {
    private String name;
    private Integer age;

    public Person() {
        System.out.println("调用了Person()");
    }

    @MyAutowired
    public Person(String name) {
        System.out.println("调用了Person(String name)");
        this.name = name;
    }

    public Person(String name, Integer age) {
        System.out.println("调用了Person(String name, int age)");
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
