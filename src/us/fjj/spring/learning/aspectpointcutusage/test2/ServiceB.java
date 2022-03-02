package us.fjj.spring.learning.aspectpointcutusage.test2;

public class ServiceB {
    public void m1() {
        System.out.println("lxh yyds!");
    }
    public String m2(int count) {
        for (int i = 0; i <count; i++) {
            System.out.println(i+":lxh yyds!");
        }
        if(count == 6) {
            throw new RuntimeException("lxh yyds!");
        }
        return "yk yyds!";
    }
}
