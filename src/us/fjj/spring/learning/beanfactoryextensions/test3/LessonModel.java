package us.fjj.spring.learning.beanfactoryextensions.test3;

import org.springframework.stereotype.Component;

@Component
public class LessonModel {
    private String name;

    @Override
    public String toString() {
        return "LessonInfo{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
