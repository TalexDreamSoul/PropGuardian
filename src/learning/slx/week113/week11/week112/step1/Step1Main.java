package learning.slx.week113.week11.week112.step1;

import learning.lxl.week111.step1.Mouse;

public class Step1Main {

    public static void main(String[] args) {
        Penguin penguin = new Penguin("Penguin1");
        Mouse mouse = new Mouse("Mouse1");

        penguin.introduceSelf();
        penguin.sleep();

        mouse.introduceSelf();
        mouse.eat();
    }

}
