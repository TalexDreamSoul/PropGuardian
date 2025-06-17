package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.inventory;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.LocationFloat;

/**
 * @Description: 为您的容器批量打印物品
 */
public interface IInventoryPainter {

    com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem onDrawBorder(int slot);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryPainter drawBorder();

    com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem onDrawLine(int slot);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryPainter drawLine();

    com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryPainter drawLineRow(int row);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryPainter drawLine(int startSlot);

    /**
     * @Description: Full代表一行9个 不然一行7个
     */
    com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryPainter drawLineFull();

    com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryPainter drawLineRowFull(int row);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryPainter drawLineFull(int startSlot);

    /**
     * 填充整个容器
     *
     * @return 支持链式调用
     */
    com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryPainter drawFull();

    /**
     * 填充屏幕的物品
     *
     * @param slot 当前要填充的格子
     *
     * @return 返回要填充的物品
     */
    com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem onDrawFull(int slot);

    /**
     * 基于一个坐标打印周围物品
     *
     * @param slot 槽位标准
     * @param item 答应的物品
     */
    com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryPainter drawArena9(int slot, com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem item);

    /**
     * 打印水平位置的 进度条
     *
     * @param row      打印的行 行数从 0 开始
     * @param maxWidth 最多打印数量
     * @param percent  进度条百分比
     * @param location 进度条浮动位置 {@link com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.LocationFloat}
     * @param finished 当进度条走过的物品
     * @param will     进度条没有到达的位置显示的物品
     */
    InventoryPainter drawProgressBarHorizontal(int row, int maxWidth, double percent, LocationFloat location, com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem finished, InventoryUI.ClickableItem will);

}
