package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.shop.gui;

import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.modules.shop.entity.ShopProduct;
import com.talexs.talexessential.utils.inventory.addon.menu.TalexListMenu;
import com.talexs.talexessential.utils.inventory.base.IInvView;
import com.talexs.talexessential.utils.inventory.base.InvUI;
import com.talexs.talexessential.utils.inventory.item.IInvClickableItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Define a sell shop for players to quick sell their items.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/27 上午 10:37
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class ShopSellMenu extends TalexListMenu<ShopProduct> {
    public ShopSellMenu(String title) {
        super(title);
    }

    @Override
    public boolean onBeforeDraw(Player player, IInvView view, InvUI ui) {
        OnlinePlayerData opd = PlayerData.g(player);
        if ( player.getInventory().getItemInOffHand().getType() != Material.AIR ) {
            opd.getUser().closeInventory().errorActionBar("要出售物品，你的副手不能有任何物品！").playSound(Sound.BLOCK_ANVIL_LAND, 1, 1);
            return false;
        }
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, .1f, .25f);
        return true;
    }

    @Override
    public List<ShopProduct> filterList(IInvView view, Player player) {
        OnlinePlayerData opd = PlayerData.g(player);

        int rankLevel = opd.getInfo().getInt("Rank.Level", 0);

        return ShopProduct.getPlayerProducts(ShopProduct.ProductType.SELL, rankLevel);
    }

    @Override
    public IInvClickableItem drawEle(IInvView view, InvUI ui, Player player, ShopProduct data) {
        return null;
    }
}
