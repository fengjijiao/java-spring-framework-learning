package us.fjj.spring.learning.beanlifecycle.test3;

import us.fjj.spring.learning.beanlifecycle.test1.Car;

public class User {
    private String name;
    private Car car;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", car=" + car +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
