package learning.mxr.week11.week55.step4.week111.step1;

import learning.mxr.week55.step4.week111.step1.Mouse;
import learning.mxr.week55.step4.week111.step1.Penguin;

public class Step1Main {

    public static void main(String[] args) {
        learning.mxr.week55.step4.week111.step1.Penguin penguin = new Penguin("Penguin1");
        learning.mxr.week55.step4.week111.step1.Mouse mouse = new Mouse("Mouse1");

        penguin.introduceSelf();
        penguin.sleep();

        mouse.introduceSelf();
        mouse.eat();
    }

}
