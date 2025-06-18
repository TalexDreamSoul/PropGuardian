package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.realm.gui;

import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.base.PlayerPicker;
import com.talexs.talexessential.modules.guider.PlayerProfile;
import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RealmMemberGUI extends MenuBasic {

    private PlayerRealm pr;

    public RealmMemberGUI(Player player, PlayerRealm pr) {
        super(player, "&a领域成员管理", 6);

        this.pr = pr;
        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawLineFull(10);

        inventoryUI.setItem(12, new InventoryUI.EmptyCancelledClickableItem(
                new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7*").toItemStack()
        ));

        inventoryUI.setItem(4, PlayerProfile.getMainItem(player));

        int startSlot = 20;

        if ( pr.getInfo().contains("Permissions") ) startSlot = drawMembers(startSlot);

        inventoryUI.setItem(startSlot, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.TOTEM_OF_UNDYING).setName("§a添加成员")
                        .setLore(
                                "",
                                "&8| &7添加成员后，该成员享有领域所有权限",
                                "&8| &7默认添加为 &e临时 &7权限，需在团队里",
                                "&8| &b再次点击玩家 &7更改为永久权限！",
                                "",
                                "&8| &e临时 &7权限仅在您在线时玩家拥有！",
                                ""
                        )
                        .toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                new PlayerPicker(player, 0, target -> {
                    if ( pr.doPlayerHasPer(target) ) {
                        player.sendMessage("§c该玩家已经拥有领地权限了！");
                        return;
                    }
                    else player.sendMessage("§a已经将该玩家添加为领地信任玩家！");

                    pr.setPlayerPer(target, "temp");
                    
                    open(true);
                }) {

                    @Override
                    public Collection<? extends Player> getPlayers() {
                        return super.getPlayers().stream().filter(p -> !pr.doPlayerHasPer(p)).collect(Collectors.toList());
                    }

                }.open();

                return false;
            }
        });
    }

    private int drawMembers(int start) {
        int startSlot = start;

        Set<String> keys = Objects.requireNonNull(pr.getInfo().getConfigurationSection("Permissions")).getKeys(false);

        for ( String key : keys ) {
            String path = "Permissions." + key;
            String playerPer = pr.getInfo().getString(path + ".per");

            if ( playerPer == null || playerPer.equalsIgnoreCase("null") ) continue;

            String name = pr.getInfo().getString(path + ".name");

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(key);

            inventoryUI.setItem(startSlot, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {

                    ItemBuilder ib = new ItemBuilder(Material.PLAYER_HEAD).setName("§a" + name)
                            .setSkullOwner(offlinePlayer.getName())
                            .setLore(
                                    "§8#" + key,
                                    "",
                                    "&8| &7这位玩家可以在领域内自由活动",
                                    "&8| &7因为你给予了他权限！",
                                    "",
                                    "&8| &7权限: §e" + (playerPer.equalsIgnoreCase("temp") ? "暂时" : "永久"),
                                    "",
                                    "&8| &a左键切换 &8| &e右键踢出",
                                    "");

                    return ib.toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    if ( e.isRightClick() ) {
                        pr.getInfo().set(path, null);

                        new PlayerUser(player).actionBar("&e您已将 &b" + offlinePlayer.getName() + " &e移出了领域！").playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
                        open();
                        return false;
                    }
                    pr.getInfo().set(path + ".per", playerPer.equalsIgnoreCase("temp") ? "has" : "temp");

                    new PlayerUser(player).actionBar("&e您已将 &b" + offlinePlayer.getName() + " &e的权限改为 &b" + (playerPer.equalsIgnoreCase("temp") ? "永久" : "暂时") + " &e！").playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
                    open();
                    return false;
                }
            });

            if ( (startSlot + 3) % 9 == 0 ) startSlot += 5;
            else startSlot += 1;

        }

        return startSlot;
    }
}
