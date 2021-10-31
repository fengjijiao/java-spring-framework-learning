package us.fjj.spring.learning.aspectjannotationdemo;

public class Man {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void throwException() {
        System.out.println("抛出异常");
        throw new IllegalArgumentException();
    }
}
