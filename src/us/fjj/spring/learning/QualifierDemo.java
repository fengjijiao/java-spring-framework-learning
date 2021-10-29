package us.fjj.spring.learning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class QualifierDemo {
    public static class Student {
        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class Profile {
        @Autowired
        @Qualifier("student1")
        private Student student;

        public Profile() {
            System.out.println("inside profile constructor.");
        }

        public void printId() {
            System.out.println("ID: "+student.getId());
        }

        public void printName() {
            System.out.println("Name: "+student.getName());
        }
    }
}
