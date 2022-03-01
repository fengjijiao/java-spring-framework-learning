package us.fjj.spring.learning.aopusage.test9;

public class AServiceImpl implements IService {
    @Override
    public void say(String name) {
        System.out.println(name);
    }
}
