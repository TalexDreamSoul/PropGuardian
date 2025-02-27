package learning.tzx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> lines = new ArrayList<>();

        lines.add("Hello");
        lines.add("Hello");
        lines.add("Hello");
        lines.add("World");
        lines.add(1, "Java");
        lines.add("!");

        for (String line : lines) {
            System.out.print(line);
        }
    }
}
