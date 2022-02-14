package us.fjj.spring.learning.importannotationusage.test6;

import org.springframework.stereotype.Component;

@Component
public class Ncp3 {
    public void m1() {
        System.out.println(this.getClass() + ".m1()");
    }
}
