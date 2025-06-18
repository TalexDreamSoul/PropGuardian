package learning.mxr.week55.step1;

import learning.mxr.week11.step1.Animal;

public class Mouse extends Animal {
    public Mouse(String name) {
        super("MOUSE", name);
    }

    public void eat() {
        System.out.println("Hello, eating now.");
    }
}
