package learning.zhy.week11234.step1;

public class Mouse extends Animal {
    public Mouse(String name) {
        super("MOUSE", name);
    }

    public void eat() {
        System.out.println("Hello, eating now.");
    }
}
