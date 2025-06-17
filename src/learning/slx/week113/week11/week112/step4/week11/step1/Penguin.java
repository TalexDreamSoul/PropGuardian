package learning.slx.week113.week11.week112.step4.week11.step1;

import learning.lxl.week112.step1.Animal;

public class Penguin extends Animal {
    public Penguin(String name) {
        super("PENGUIN", name);
    }

    public void sleep() {
        System.out.println("Hello, sleeping now.");
    }
}
