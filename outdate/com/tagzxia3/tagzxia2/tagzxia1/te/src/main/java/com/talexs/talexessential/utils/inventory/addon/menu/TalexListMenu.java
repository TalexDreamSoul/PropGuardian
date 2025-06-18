package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.menu;

import cn.hutool.core.util.StrUtil;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.menu.TalexMenu;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.utils.StringUtil;
import com.talexs.talexessential.utils.inventory.addon.painter.InvUIPainter;
import com.talexs.talexessential.utils.inventory.base.IInvView;
import com.talexs.talexessential.utils.inventory.base.InvUI;
import com.talexs.talexessential.utils.inventory.base.InvView;
import com.talexs.talexessential.utils.inventory.item.ClickableBuilderItem;
import com.talexs.talexessential.utils.inventory.item.IInvClickableItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Define a menu that automatically generated with given list.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/21 上午 09:24
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public abstract class TalexListMenu<T> extends TalexMenu {

    private static final int PAGE_SIZE = 28;

    protected Map<UUID, ListMenuObject> maps = new WeakHashMap<>();

    public TalexListMenu(String title) {
        super(title, 6);
    }

    @Override
    public void establish(IInvView view, InvUI ui, Player player) {
        if ( !maps.containsKey(player.getUniqueId()) ) maps.put(player.getUniqueId(), new ListMenuObject(filterList(view, player)));

        if ( !onBeforeDraw(player, view, ui) ) return;

        ListMenuObject lmo = maps.get(player.getUniqueId());

//        lmo.setList(filterList(view, player));

        getPainter(ui).drawFull().drawBorder();

        AtomicInteger startIndex = new AtomicInteger(10);

        try {

            lmo.currentList().forEach(T -> {

                ui.setItem(startIndex.get(), drawEle(view, ui, player, T));

                if ( (startIndex.get() + 2) % 9 == 0 ) startIndex.addAndGet(3);
                else if ( startIndex.get() >= 43 ) throw new ArrayIndexOutOfBoundsException();
                else startIndex.addAndGet(1);

            });

        } catch ( Exception ignored ) {

        }

        drawPageSelector(lmo, player, ui, false);
        drawPageSelector(lmo, player, ui, true);

        onAfterDraw(player, view, ui, startIndex);
    }

    public InvUIPainter getPainter(InvUI ui) {
        return ui.getPainter();
    }

    public static final int LAST_PAGE_SELECTOR_SLOT = 26, NEXT_PAGE_SELECTOR = 35;

    private void drawPageSelector(ListMenuObject lmo, Player player, InvUI ui, boolean doNext) {
        int slot = doNext ? NEXT_PAGE_SELECTOR : LAST_PAGE_SELECTOR_SLOT;

        ItemBuilder pageSelector = lmo.getPageSelector(doNext);

        if ( pageSelector != null ) {

            ui.setItem(slot, new ClickableBuilderItem(pageSelector) {
                @Override
                public boolean onClick(InventoryClickEvent event) {
                    int targetPage = event.isRightClick() ? (doNext ? lmo.totalPage : 1) : (lmo.nowPage + (doNext ? 1 : -1));

                    IInvView iInvView = ui.getIInvView();
                    List<InvUI> uis = iInvView.getUis();

                    while ( targetPage < 1 || targetPage > uis.size() ) {
                        iInvView.createNewInvUI().setNowPage(targetPage - 1);
                    }

                    lmo.nowPage = targetPage;

                    open(player);

                    OnlinePlayerData opd = PlayerData.g(player);

                    opd.getUser()
                            .playSound(Sound.UI_BUTTON_CLICK)
                            .infoActionBar("已打开！");

                    return false;
                }
            });

        }

    }

    public abstract List<T> filterList(IInvView view, Player player);

    /**
     * Return false to deny draw.
     */
    public boolean onBeforeDraw(Player player, IInvView view, InvUI ui) {
        return true;
    }

    public void onAfterDraw(Player player, IInvView view, InvUI ui, AtomicInteger index) {}

    public abstract IInvClickableItem drawEle(IInvView view, InvUI ui, Player player, T data);

    // TODO: sort / filter

    public class ListMenuObject {

        private int totalPage, nowPage;

        private List<T> list;

        public ListMenuObject(List<T> list) {
            setList(list);
        }

        public void setList(List<T> list) {
            this.list = list;

            initPages();
        }

        private void initPages() {
            int totalSize = this.list.size();

            this.totalPage = Math.max(1, totalSize / PAGE_SIZE);
            this.nowPage = 1;
        }

        public List<T> currentList() {
            return getListByPage(this.nowPage);
        }

        public List<T> getListByPage(int page) {
            if ( page > this.totalPage || page < 1 ) return new ArrayList<>();

            int starts = (page - 1) * PAGE_SIZE;
            int ends = Math.min(this.list.size(), starts + PAGE_SIZE);

            return new ArrayList<>(this.list.subList(starts, ends));
        }

        public ItemBuilder getPageSelector(boolean doNext) {
            int targetPage = this.nowPage + (doNext ? 1 : -1);
            if ( targetPage < 1 || targetPage > this.totalPage ) return null;

            return new ItemBuilder(Material.ARROW)
                    .setName("&f" + (doNext ? "下一页" : "上一页"))
                    .setLore(
                            "",
                            "&8| &7你可以使用这个图标来切换页码.",
                            "&8| &7只需轻点即可选择，右击以切换到" + (doNext ? "末尾" : "起始"),
                            "",
                            "&8| &7页码: &b" + (this.nowPage) + "&8/&e" + this.totalPage,
                            "&8| &7切换到 &e第" + (StringUtil.covertNumber2Char(targetPage)) + "页 &7.",
                            ""
                    );
        }

    }

}
