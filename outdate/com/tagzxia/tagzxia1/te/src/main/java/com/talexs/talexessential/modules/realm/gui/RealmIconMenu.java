package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.realm.gui;

import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.modules.realm.RealmModule;
import com.talexs.talexessential.modules.realm.entity.RealmIcon;
import com.talexs.talexessential.modules.realm.entity.RealmValuableIcon;
import com.talexs.talexessential.utils.inventory.addon.menu.TalexListMenu;
import com.talexs.talexessential.utils.inventory.base.IInvView;
import com.talexs.talexessential.utils.inventory.base.InvUI;
import com.talexs.talexessential.utils.inventory.item.ClickableBuilderItem;
import com.talexs.talexessential.utils.inventory.item.IInvClickableItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Define a ream icon list.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/24 下午 10:41
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class RealmIconMenu extends TalexListMenu<RealmValuableIcon> {

    private PlayerRealm pr;

    public RealmIconMenu(@NotNull PlayerRealm pr) {
        super("&e领域 &8> &e图标 &8> &e购买");

        this.pr = pr;
    }

    @Override
    public List<RealmValuableIcon> filterList(IInvView view, Player player) {
        List<RealmValuableIcon> icons = new ArrayList<>();
        Set<String> keys = Objects.requireNonNull(RealmModule.realmIconYaml.getConfigurationSection("Icons")).getKeys(false);

        keys.forEach(key -> {
            RealmIcon ri = new RealmIcon(key);

            RealmValuableIcon icon = new RealmValuableIcon(
                    ri,
                    RealmModule.realmIconYaml.getDouble("Icons." + key, 0f)
            );

            icons.add(icon);
        });

        return icons;
    }

    @Override
    public IInvClickableItem drawEle(IInvView view, InvUI ui, Player player, RealmValuableIcon data) {
        double origin = data.getCost();

        double takeoff = 0.0d;

        if ( player.hasPermission("te.vip.plus") ) takeoff = .03f;
        else if ( player.hasPermission("te.vip.pro") ) takeoff = .05f;

        double price = (1 - takeoff) * origin;
        double tax = price * 0.15;

        ItemBuilder ib = new ItemBuilder(data.getIcon().getIcon())
                .setLore(
                        "",
                        "&8| &7新心相映的领域图标",
                        "&8| &7来打造您的独特领域",
                        "",
                        "&8| &7价格: &e" + (String.format("%.2f", price)) + " &7元"
                );

        ib.addLoreLine("&8| &7税收: &e" + (String.format("%.2f", tax)) + " &7元");

        if ( origin != price ) {
            ib.addLoreLine("&8| &b您是贵宾用户, 享受 &e" + (String.format("%.0f", takeoff * 100)) + "% &b折扣");
        }

        ib.addLoreLine("");

        return new ClickableBuilderItem(ib) {
            @Override
            public boolean onClick(InventoryClickEvent event) {
                OnlinePlayerData opd = PlayerData.g(player);
                EconomyResponse economyResponse = TalexEssential.getInstance().getEcon().withdrawPlayer(player, (price + tax));

                if ( !economyResponse.transactionSuccess() ) {
                    opd.getUser()
                            .playSound(Sound.ENTITY_WANDERING_TRADER_NO)
                            .errorActionBar("您没有足够的金币来支付!");
                    return true;
                }

                pr.getRealmIcon().setIcon(data.getIcon().getMaterial());

                opd.getUser()
                        .closeInventory()
                        .playSound(Sound.ENTITY_WANDERING_TRADER_YES)
                        .infoActionBar("购买成功, 已自动设置!");

                return false;
            }
        };
    }

}
