package learning.lxl.week2222.step1;

public class Penguin extends Animal {
    public Penguin(String name) {
        super("PENGUIN", name);
    }

    public void sleep() {
        System.out.println("Hello, sleeping now.");
    }
}
