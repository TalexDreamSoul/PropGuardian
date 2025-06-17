package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.realm.gui;

import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.modules.realm.addon.RealmAddons;
import com.talexs.talexessential.modules.realm.entity.RealmFlag;
import com.talexs.talexessential.utils.inventory.addon.menu.TalexListMenu;
import com.talexs.talexessential.utils.inventory.addon.menu.TalexMenu;
import com.talexs.talexessential.utils.inventory.base.IInvView;
import com.talexs.talexessential.utils.inventory.base.InvUI;
import com.talexs.talexessential.utils.inventory.item.ClickableBuilderItem;
import com.talexs.talexessential.utils.inventory.item.IInvClickableItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Define a menu to manager realm settings.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/23 下午 10:48
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class RealmSetsMenu extends TalexListMenu<RealmFlag> {

    private PlayerRealm pr;

    public RealmSetsMenu(@NotNull PlayerRealm pr) {
        super("&e领域 &8> &e" + pr.getName() + " &8> &e设置");

        this.pr = pr;
    }

    @Override
    public List<RealmFlag> filterList(IInvView view, Player player) {
        return RealmAddons.INS.getAddons();
    }

    @Override
    public IInvClickableItem drawEle(IInvView view, InvUI ui, Player player, RealmFlag data) {
        OnlinePlayerData opd = PlayerData.g(player);

        int rankLevel = opd.getInfo().getInt("Rank.Level", 0);

        ItemBuilder ib = data.getIb().clone();

        boolean enable = pr.getFlag(data.getKey());

        ib.setName(data.getName() + " &8(&r" + (enable ? data.getStatus_enable_name() : data.getStatus_disable_name()) + "&8)");

        ib.isTrueAccessEnchantAndHide(enable, Enchantment.DURABILITY, 1);
        ib.addLoreLine("&8| &e请谨慎给予权限");
        ib.addLoreLine("&8| &7每次切换需要消耗 &e" + (String.format("%.2f", data.getCost())) + " &7金币");
        ib.addLoreLine("&8| &r" + (enable ? data.getStatus_enable_desc() : data.getStatus_disable_desc()));

        if ( data.getRank() > rankLevel ) {
            ib.setType(Material.BARRIER);
            ib.addLoreLine("&8| &r" + data.getRankMention());
            ib.addLoreLine("&8| &e提升通行等级到 &b&l" + data.getRank() + " &e后解锁.");
        }

        ib.addLoreLine("");

        return new ClickableBuilderItem(ib) {
            @Override
            public boolean onClick(InventoryClickEvent event) {
                if ( data.getRank() > rankLevel ) return true;

                EconomyResponse economyResponse = TalexEssential.getInstance().getEcon().withdrawPlayer(player, data.getCost());
                if ( !economyResponse.transactionSuccess() ) {
                    new PlayerUser(player).errorActionBar("您的金币不足!(" + economyResponse.errorMessage + ")")
                            .title("", "&e金币不足!",0, 40, 20)
                            .playSound(Sound.BLOCK_ANVIL_FALL);
                    return true;
                }

                pr.setFlag(data.getKey(), !enable);

                player.playSound(player, enable ? Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_ON : Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF, 1, 1);

                open(player);

                return true;
            }
        };
    }
}
