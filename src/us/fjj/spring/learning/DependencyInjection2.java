package us.fjj.spring.learning;

public class DependencyInjection2 {
    public static class Person {
        private Man man;
        public Person() {
            System.out.println("在Person的无参构造函数内");
        }
        public Person(Man man) {
            System.out.println("在Person的有参构造函数内");
            this.man=man;
        }

        public Man getMan() {
            return man;
        }

        public void setMan(Man man) {
            this.man = man;
        }

        public void man() {
            man.show();
        }
    }
    public static class Man {
        private String name;
        private int age;

        public Man() {
            System.out.println("在Man的无参构造函数内");
        }

        public Man(String name, int age) {
            System.out.println("在Man的有参构造函数内");
            this.name = name;
            this.age = age;
        }

        public void show() {
            System.out.println("name: "+name+",age: "+age);
        }

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
    }
}
