package utils;

import javax.swing.*;

public class MentionUtil {

    public static void mentionForDelete(boolean success,  JFrame parentComponent, Runnable refreshTable) {
        if (success) {
            JOptionPane.showMessageDialog(parentComponent, "删除成功！");
            refreshTable.run();
        } else {
            JOptionPane.showMessageDialog(parentComponent, "删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void mentionForAdd(boolean success,  JFrame parentComponent, Runnable refreshTable) {
        if (success) {
            JOptionPane.showMessageDialog(parentComponent, "新增成功！");
            refreshTable.run();
        } else {
            JOptionPane.showMessageDialog(parentComponent, "新增失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void mentionForUpdate(boolean success,  JFrame parentComponent, Runnable refreshTable) {
        if (success) {
            JOptionPane.showMessageDialog(parentComponent, "修改成功！");
            refreshTable.run();
        } else {
            JOptionPane.showMessageDialog(parentComponent, "修改失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

}
