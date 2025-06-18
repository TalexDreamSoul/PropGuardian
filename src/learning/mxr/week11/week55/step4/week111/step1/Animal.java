package learning.mxr.week11.week55.step4.week111.step1;

import lombok.Data;

@Data
public class Animal {
    private final String id, name;

    public Animal(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void introduceSelf() {
        System.out.println("Hello, I'm " + this.name);
    }
}
