package us.fjj.spring.learning;

public class DependencyInjection {
    public static class Person {
        private Man man;
        public Person(Man man) {
            System.out.println("在Person的构造函数内");
            this.man=man;
        }
        public void man() {
            man.show();
        }
    }
    public static class Man {
        private String name;
        private int age;

        public Man(String name, int age) {
            System.out.println("在Man的构造函数内");
            this.name = name;
            this.age = age;
        }

        public void show() {
            System.out.println("name: "+name+",age: "+age);
        }
    }
}
