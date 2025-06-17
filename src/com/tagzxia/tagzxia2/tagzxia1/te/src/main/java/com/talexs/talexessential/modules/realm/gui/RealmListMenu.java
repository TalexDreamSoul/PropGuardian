package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.realm.gui;

import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.modules.realm.RealmModule;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.addon.menu.TalexListMenu;
import com.talexs.talexessential.utils.inventory.base.IInvView;
import com.talexs.talexessential.utils.inventory.base.InvUI;
import com.talexs.talexessential.utils.inventory.item.ClickableBuilderItem;
import com.talexs.talexessential.utils.inventory.item.IInvClickableItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Define a realm list menu.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/22 下午 11:46
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class RealmListMenu extends TalexListMenu<PlayerRealm> {
    public RealmListMenu() {
        super("&e领域 &8> &e列表");
    }

    @Override
    public List<PlayerRealm> filterList(IInvView view, Player player) {
        return RealmModule.realms.stream().filter(pr -> player.hasPermission("talex.realm.admin") || pr.doVisible(player)).collect(Collectors.toList());
    }

    @Override
    public IInvClickableItem drawEle(IInvView view, InvUI ui, Player player, PlayerRealm pr) {
        String created = new SimpleDateFormat("yy/MM/dd HH:mm:ss").format(pr.getCreated());

        return new ClickableBuilderItem(new ItemBuilder(pr.getRealmIcon().getIcon())
                .setName("§e" + pr.getName())
                .setLore(
                        "",
                        "&8| &7这是一个公开的领域",
                        "&8| &7点击可以传送到该领域",
                        "",
                        "&8| &e主人: &b" + pr.getOwner(),
                        "&8| &e创建: &b" + created,
                        "&8| &e位置: &b" + pr.getServerName(),
                        "",
                        "&8| &a左键传送 &7| &e右键编辑",
                        "",
                        "&8#" + pr.getUuid()
                )) {

            @Override
            public boolean onClick(InventoryClickEvent e) {
                if ( e.isRightClick() ) {
                    player.chat("/realm set " + pr.getName());
                    return false;
                }
                player.closeInventory();
                player.chat("/realm tp " + pr.getName());
                return false;
            }
        };

    }
}
