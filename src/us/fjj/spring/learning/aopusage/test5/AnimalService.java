package us.fjj.spring.learning.aopusage.test5;

public class AnimalService {
    public void throwIllegalArgumentException() {
        System.out.println("throwIllegalArgumentException: ok");
        throw new IllegalArgumentException("argument must be integer type.");
    }
}
