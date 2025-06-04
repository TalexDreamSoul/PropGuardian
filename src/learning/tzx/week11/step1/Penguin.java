package learning.tzx.week11.step1;

import learning.lxl.week112.step1.Animal;

public class Penguin extends Animal {
    public Penguin(String name) {
        super("PENGUIN", name);
    }

    public void sleep() {
        System.out.println("Hello, sleeping now.");
    }
}
