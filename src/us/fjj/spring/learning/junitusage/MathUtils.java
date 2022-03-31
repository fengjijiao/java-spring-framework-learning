package us.fjj.spring.learning.junitusage;

import java.util.Arrays;
import java.util.IntSummaryStatistics;

public class MathUtils {
    /**
     * 获取最大的数字
     * @param arg
     * @return
     */
    public static int max(int... arg) {
        IntSummaryStatistics summaryStatistics = Arrays.stream(arg).summaryStatistics();
        return summaryStatistics.getMax();
    }

    /**
     * 获取最小的数字
     * @param arg
     * @return
     */
    public static int min(int... arg) {
        IntSummaryStatistics summaryStatistics = Arrays.stream(arg).summaryStatistics();
        return summaryStatistics.getMin();
    }
}
