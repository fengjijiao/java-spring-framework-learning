package us.fjj.spring.learning.aopusage.test6;

public class FoundsService {
    private double balance;

    public double recharge(String userName, double amount) {
        System.out.println(String.format("用户：%s充值：%f", userName, amount));
        balance+=amount;
        return balance;
    }

    public double withdraw(String userName, double amount) {
        if (amount > balance) {
            throw new RuntimeException("余额不足！");
        }
        System.out.println(String.format("用户：%s提现：%f", userName, amount));
        balance-=amount;
        return balance;
    }

    public double getBalance() {
        return balance;
    }
}
