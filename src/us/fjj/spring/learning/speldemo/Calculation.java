package us.fjj.spring.learning.speldemo;

public class Calculation {
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int cube() {
        return number*number*number;
    }
}
