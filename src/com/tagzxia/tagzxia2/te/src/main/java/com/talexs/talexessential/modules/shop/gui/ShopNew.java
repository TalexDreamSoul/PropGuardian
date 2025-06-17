package com.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.shop.gui;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.modules.shop.entity.ShopProduct;
import com.talexs.talexessential.utils.inventory.addon.menu.TalexListMenu;
import com.talexs.talexessential.utils.inventory.base.IInvView;
import com.talexs.talexessential.utils.inventory.base.InvUI;
import com.talexs.talexessential.utils.inventory.item.ClickableBuilderItem;
import com.talexs.talexessential.utils.inventory.item.IInvClickableItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Define a shop base management.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/25 下午 09:28
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class ShopNew extends TalexListMenu<ShopProduct> {

    private ShopProduct.ProductType productType;

    public ShopNew(ShopProduct.ProductType productType) {
        super("&e商店 &8> &e管理");

        this.productType = productType;
    }

//    @Override
//    public InvUIPainter getPainter(InvUI ui) {
//
//        return new InvUIPainter(ui) {
//
//            @Override
//            public IInvClickableItem onDrawFull(int slot) {
//                return new ClickableBuilderItem(
//                        new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("")
//                                .setLore(
//                                        "",
//                                        "&8| &e位置空缺",
//                                        "&8| &7等待编辑这个位置",
//                                        "",
//                                        "&8| &e左键 &7编辑这个位置",
//                                        ""
//                                )
//                ) {
//                    @Override
//                    public boolean onClick(InventoryClickEvent event) {
//                        return false;
//                    }
//                };
//            }
//        };
//    }

    @Override
    public List<ShopProduct> filterList(IInvView view, Player player) {
        view.getInvOptions().setAutoRefresh(4);

        return ShopProduct.getAllProducts(productType); //Arrays.asList(new Integer[(int) totalSize]);
    }

    @Override
    public IInvClickableItem drawEle(IInvView view, InvUI ui, Player player, ShopProduct data) {

        ItemBuilder ib = new ItemBuilder(data.getItemStack().clone());

        long diff = DateUtil.betweenMs(DateUtil.date(), data.getLastUpdated());

        ib.addLoreLine("");
        ib.addLoreLine("&8| &7编号: &e" + data.getProductId());
        ib.addLoreLine("&8| &7分类: &e" + data.getCategoryId());
        ib.addLoreLine("&8| &7库存: &e" + data.getInventory());
        ib.addLoreLine("&8| &7价格: &e" + String.format("%.2f", data.getPrice()));
        ib.addLoreLine("&8| &7更新: &e" + DateUtil.formatBetween(diff, BetweenFormatter.Level.MILLISECOND) + " 前");
        ib.addLoreLine("");
        ib.addLoreLine("&8| &e左键 &7查阅流水 &7| &e右键 &7设置信息");
        ib.addLoreLine("&8| &eSHIFT + 左键 &7下架此商品.");
        ib.addLoreLine("");

        return new ClickableBuilderItem(ib) {
            @Override
            public boolean onClick(InventoryClickEvent event) {
                OnlinePlayerData opd = PlayerData.g(player);
                if ( !event.isShiftClick() ) {
                    opd.getUser().errorActionBar("此功能暂未开放.");
                    return true;
                }

                data.delete();

                open(player);

                return true;
            }
        };
    }

//    @Override
//    public void onBeforeDraw(Player player, IInvView view, InvUI ui) {
//
//        maps.put(player.getUniqueId(), new ListMenuObject());
//
//    }
}
