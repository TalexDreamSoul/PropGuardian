package learning.slx.week114.step1;

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
