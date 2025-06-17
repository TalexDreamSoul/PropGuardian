package learning.zhy.xxxx.step1;

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
