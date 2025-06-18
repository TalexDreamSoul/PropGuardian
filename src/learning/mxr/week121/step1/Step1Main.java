package learning.mxr.week121.step1;

import learning.mxr.week11.step1.Mouse;
import learning.mxr.week11.step1.Penguin;

public class Step1Main {

    public static void main(String[] args) {
        learning.mxr.week11.step1.Penguin penguin = new Penguin("Penguin1");
        learning.mxr.week11.step1.Mouse mouse = new Mouse("Mouse1");

        penguin.introduceSelf();
        penguin.sleep();

        mouse.introduceSelf();
        mouse.eat();
    }

}
