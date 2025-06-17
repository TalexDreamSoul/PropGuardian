package learning.mxr.week171.week17.step1;

import learning.mxr.week11.step1.Animal;

public class Penguin extends Animal {
    public Penguin(String name) {
        super("PENGUIN", name);
    }

    public void sleep() {
        System.out.println("Hello, sleeping now.");
    }
}
