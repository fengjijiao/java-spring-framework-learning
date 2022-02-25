package us.fjj.spring.learning.proxyexpend.cglib.test3;

public class UserModel {
    private String name;

    public UserModel() {
    }

    public UserModel(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void say() {
        System.out.printf("%s say: hello!\n", name);
    }
}
