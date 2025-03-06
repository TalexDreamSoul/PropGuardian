package learning.zhy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

    List<String> lines = new ArrayList<>(6);

    for (int i = 0; i < 3; i++) {
        lines.add("Hello");
    }
    if (lines.size() > 1) {
        lines.add(1, "Java");
    } else {
        System.out.println("无法在索引1处插入元素，因为列表长度不足");
    }

    lines.add("World");
    lines.add("!");

    for (String line : lines) {
        System.out.print(line + " ");
    }
    System.out.println();
}

}
