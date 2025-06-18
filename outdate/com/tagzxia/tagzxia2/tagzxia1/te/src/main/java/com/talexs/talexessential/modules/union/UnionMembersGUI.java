package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.union;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.union.MemberPermission;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.union.UnionControlGUI;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.union.UnionData;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.union.UnionMember;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.base.PlayerPicker;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UnionMembersGUI extends MenuBasic {

    public static Map<String, UnionData> unionInvitations = new HashMap<>();

    private UnionData ud;

    public UnionMembersGUI(Player player, UnionData ud) {
        super(player, "&a联盟成员管理", 6);

        this.ud = ud;
        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawLineFull(10);

        inventoryUI.setItem(14, new InventoryUI.EmptyCancelledClickableItem(
           new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7*").toItemStack()
        ));

        inventoryUI.setItem(4, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.ARROW).setName("&e返回").toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                new UnionControlGUI(player, ud).open();
                return false;
            }
        });

        int startSlot = 20;

        for ( int i = 0; i < ud.getMembers().size(); ++i ) {
            UnionMember um = ud.getMembers().get(i);

            inventoryUI.setItem(startSlot, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {

                    ItemBuilder ib = new ItemBuilder(Material.PLAYER_HEAD).setName("§a" + um.getName())
                            .setSkullOwner(um.getName())
                            .setLore(
                                    "§8#" + um.getUuid(),
                                    "",
                                    "&8| &7加入时间: §e" + um.getCreated(),
                                    "&8| &7权限等级: §e" + um.getPermission().name(),
                                    "§8| &7贡献积分: &e" + um.getDummy(),
                                    "",
                                    "&8| &a左键管理 &8| &e右键踢出",
                                    "");

                    return ib.toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    if ( e.isRightClick() ) {
                        Player target = Bukkit.getPlayer(um.getUuid());
                        if ( target == null || !target.isOnline() ) {
                            new PlayerUser(player).infoActionBar("对方不在线！").playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
                            return false;
                        }

                        if ( um.getUuid().equals(player.getUniqueId()) ) {
                            new PlayerUser(player).infoActionBar("您不能踢出自己！").playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
                            return false;
                        }

//                        if ( ud.getMembers().size() <= 1 ) {
//                            new PlayerUser(player).infoActionBar("您不能踢出最后一个成员！").playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
//                            return false;
//                        }

                        if ( um.getPermission() == MemberPermission.OWNER ) {
                            new PlayerUser(player).infoActionBar("您不能踢出联盟所有者！").playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
                            return false;
                        }

                        um.kickSelf();

                        PlayerData tpd = PlayerData.g(target);

                        tpd.getInfo().set("Union.UUID", "");
                        tpd.getInfo().set("Union.name", "");

                        new PlayerUser(player).infoActionBar("您已踢出 " + um.getName() + " !").playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
                        new PlayerUser(target).infoActionBar("您已被踢出联盟 " + ud.getName() + " !").playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);

                        ud.addLog(player.getName() + " 踢出了 " + um.getName() + " .");
                        ud.save();

                        open();
                        return false;
                    }
                    return false;
                }
            });

            if ( (startSlot + 3) % 9 == 0 ) startSlot += 5;
            else startSlot += 1;

        }

        inventoryUI.setItem(startSlot, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.TOTEM_OF_UNDYING).setName("§a添加成员").toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                if ( ud.getMembers().size() >= 10 ) {
                    new PlayerUser(player).infoActionBar("联盟成员已满！").playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
                    return false;
                }

                Component agree = Component.text("    [同意]")
                        .hoverEvent(HoverEvent.showText(Component.text("点击同意加入联盟.")))
                        .clickEvent(ClickEvent.runCommand("/te union agree " + player.getName()))
                        .color(TextColor.color(0x5afa5a));

                Component disagree = Component.text("    [拒绝]")
                        .hoverEvent(HoverEvent.showText(Component.text("点击拒绝加入联盟.")))
                        .clickEvent(ClickEvent.runCommand("/te union disagree " + player.getName()))
                        .color(TextColor.color(0xfa5a5a));

                Component ignore = Component.text("    [忽略]")
                        .hoverEvent(HoverEvent.showText(Component.text("点击忽略加入联盟.")))
                        .clickEvent(ClickEvent.runCommand("/te union ignore " + player.getName()))
                        .color(TextColor.color(0x5a5afa));

                new PlayerPicker(player, 0, (picker) -> {
                    boolean match = ud.getMembers().stream().anyMatch((um) -> um.getUuid().equals(picker.getUniqueId()));
                    if ( match ) {
                        new PlayerUser(player).infoActionBar("对方已经是联盟成员了！").playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
                        return;
                    }

                    UnionData tud = UnionData.getFromPlayer(picker.getUniqueId());
                    if ( tud != null ) {
                        new PlayerUser(player).infoActionBar("对方已经加入了其他联盟！").playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
                        return;
                    }

                    PlayerData tpd = PlayerData.g(picker);
                    int targetRankLevel = tpd.getInfo().getInt("Rank.Level", 0);
                    int rankLevel = PlayerData.g(player).getInfo().getInt("Rank.Level", 0);

                    if ( rankLevel - targetRankLevel > 4 ) {
                        new PlayerUser(player)
                                .errorActionBar("对方通行等级过低，无法加入联盟！")
                                .playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
                        return;
                    }

                    unionInvitations.put(player.getName(), ud);

                    picker.sendMessage(
                            Component.text("").appendNewline()
                                    .append(Component.text("[联盟✦Union] " + player.getName() + " 想邀请你加入联盟 " + ud.getName()).color(TextColor.color(0xeffeef)))
                                    .appendNewline()
                                    .append(agree).append(disagree).append(ignore)
                                    .appendNewline()
                    );

                    ud.addLog(player.getName() + " 邀请了 " + picker.getName() + " .");
                    ud.save();

                    new PlayerUser(picker).infoActionBar("您收到了一份联盟邀请！").playSound(Sound.BLOCK_ANVIL_PLACE, 1, 1);
                    new PlayerUser(player).infoActionBar("您已向对方发出邀请！").playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
                }) {

                    @Override
                    public Collection<? extends Player> getPlayers() {
                        return super.getPlayers().stream().filter(p -> !ud.getMembers().stream().anyMatch((um) -> um.getUuid().equals(p.getUniqueId()))).collect(Collectors.toList());
                    }
                }.open();
                return false;
            }
        });

    }
}
