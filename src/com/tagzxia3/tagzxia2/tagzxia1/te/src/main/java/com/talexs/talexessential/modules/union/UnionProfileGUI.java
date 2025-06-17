package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.union;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.union.MemberPermission;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.union.UnionControlGUI;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.union.UnionData;
import com.talexs.talexessential.modules.guider.PlayerProfile;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;

public class UnionProfileGUI extends MenuBasic {
    public UnionProfileGUI(Player player) {
        super(player, "&e联盟一览", 6);

        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
    }

    public static InventoryUI.ClickableItem getUnionSymbol(Player player, UnionProfileGUI gui, UnionData ud, boolean back) {
        return new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                long createdTime = ud.getCreated();

                String created = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createdTime);

                ItemBuilder ib = new ItemBuilder(Material.GRASS_BLOCK).setName("§a" + ud.getName())
                        .setLore(
                                "§8#" + ud.getUuid(),
                                "",
                                "§8| &7创建: &e" + created,
                                "§8| &7等级: &e百川",
                                "&8| &7成员: "
                        );

                ud.getMembers().forEach(p -> {
                    if ( p.getPermission() == MemberPermission.OWNER ) {
                        ib.addLoreLine("    &c◈ &b" + p.getName());
                    } else
                        ib.addLoreLine("    " + (p.getPermission() == MemberPermission.ADMIN ? "&e◆" : "&7◇") + " &3" + p.getName());
                });

                ib.addLoreLine("");
                ib.addLoreLine("&7&ki&a 点击管理 ...");
                ib.addLoreLine("");

                return ib.toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                if ( back ) {
                    gui.open(true);
                    player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
                }
                new UnionControlGUI(player, ud).open();
                return false;
            }
        };
    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawLineFull(10);

        inventoryUI.setItem(13, new InventoryUI.EmptyCancelledClickableItem(
           new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7*").toItemStack()
        ));

        inventoryUI.setItem(4, PlayerProfile.getMainItem(player));

        int startSlot = 20;

        for ( UnionData ud : UnionData.getAllUnion() ) {

            inventoryUI.setItem(startSlot, getUnionSymbol(player, this, ud, false));

            if ( (startSlot + 3) % 9 == 0 ) startSlot += 5;
            else startSlot += 1;

        }

    }
}
