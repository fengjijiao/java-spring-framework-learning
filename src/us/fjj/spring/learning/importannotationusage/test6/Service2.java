package us.fjj.spring.learning.importannotationusage.test6;

import org.springframework.stereotype.Component;

@Component
public class Service2 {
    public void m1() {
        for (int i = 0; i < 222; ) {
            i++;
            i--;
            i++;
        }
        System.out.println("这是Service2的方法m1！");
    }
}
