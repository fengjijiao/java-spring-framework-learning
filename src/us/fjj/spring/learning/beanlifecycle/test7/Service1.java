package us.fjj.spring.learning.beanlifecycle.test7;

public class Service1 {
    private String name;
    private Integer salary;

    @Override
    public String toString() {
        return "Service1{" +
                "name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
