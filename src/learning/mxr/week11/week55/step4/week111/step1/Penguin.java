package learning.mxr.week11.week55.step4.week111.step1;

import learning.mxr.week55.step4.week111.step1.Animal;

public class Penguin extends Animal {
    public Penguin(String name) {
        super("PENGUIN", name);
    }

    public void sleep() {
        System.out.println("Hello, sleeping now.");
    }
}
