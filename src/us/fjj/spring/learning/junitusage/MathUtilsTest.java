package us.fjj.spring.learning.junitusage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathUtilsTest {
    @Test
    public void max() {
        int result = MathUtils.max(1, 2, 3);
        //判断测试结果和我们期望的结果是否一致
        assertEquals(result, 3);
        System.out.println("max() tested");
    }

    @Test
    public void min() {
        int result = MathUtils.min(1, 2, 3);
        //判断测试结果和我们期望的结果是否一致
        assertEquals(result, 1);
        System.out.println("min() tested");
    }
}
