package learning.mxr.week11.week55.step4.week111.step1;

import learning.mxr.week55.step4.week111.step1.Animal;

public class Mouse extends Animal {
    public Mouse(String name) {
        super("MOUSE", name);
    }

    public void eat() {
        System.out.println("Hello, eating now.");
    }
}
