package learning.slx.week114.week11.step1;

import learning.lxl.week112.step1.Animal;

public class Mouse extends Animal {
    public Mouse(String name) {
        super("MOUSE", name);
    }

    public void eat() {
        System.out.println("Hello, eating now.");
    }
}
