package com.tagzxia3.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.painter;

import com.talexs.talexessential.utils.inventory.LocationFloat;
import com.talexs.talexessential.utils.inventory.item.IInvClickableItem;

/**
 * Define a series of actions for quick create inv ui.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 2023-08-20 下午 09:36
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */

public interface IInvPainter {

    IInvClickableItem onDrawBorder(int slot);

    IInvPainter drawBorder();

    IInvClickableItem onDrawLine(int slot);

    IInvPainter drawLine();

    IInvPainter drawLineRow(int row);

    IInvPainter drawLine(int startSlot);

    /**
     * Full代表一行9个 不然一行7个
     */
    IInvPainter drawLineFull();

    IInvPainter drawLineRowFull(int row);

    IInvPainter drawLineFull(int startSlot);

    /**
     * 填充整个容器
     *
     * @return 支持链式调用
     */
    IInvPainter drawFull();

    /**
     * 填充屏幕的物品
     *
     * @param slot 当前要填充的格子
     *
     * @return 返回要填充的物品
     */
    IInvClickableItem onDrawFull(int slot);

    /**
     * 基于一个坐标打印周围物品
     *
     * @param slot 槽位标准
     * @param item 答应的物品
     */
    IInvPainter drawArena9(int slot, IInvClickableItem item);

    /**
     * 打印水平位置的 进度条
     *
     * @param row      打印的行 行数从 0 开始
     * @param maxWidth 最多打印数量
     * @param percent  进度条百分比
     * @param location 进度条浮动位置 {@link LocationFloat}
     * @param finished 当进度条走过的物品
     * @param will     进度条没有到达的位置显示的物品
     */
    IInvPainter drawProgressBarHorizontal(int row, int maxWidth, double percent, LocationFloat location, IInvClickableItem finished, IInvClickableItem will);

}
