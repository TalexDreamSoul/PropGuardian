package com.tagzxia3.tagzxia1.src.main.java.alarm.star.alarmstarsystem.inventory.menu;

import alarm.star.alarmstarsystem.AlarmStarSystem;
import alarm.star.alarmstarsystem.config.AlarmStarConfig;
import alarm.star.alarmstarsystem.config.PlayerDataConfig;
import alarm.star.alarmstarsystem.entity.AlarmStar;
import alarm.star.alarmstarsystem.entity.PlayerData;
import alarm.star.alarmstarsystem.inventory.InventoryPainter;
import alarm.star.alarmstarsystem.inventory.MenuBasic;
import alarm.star.alarmstarsystem.utils.inventory.InventoryUI;
import alarm.star.alarmstarsystem.utils.item.ItemBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInPrison extends MenuBasic {
    public PlayerInPrison(Player player) {
        super(player, "§8[§6§l监狱§8] §a在线的监狱玩家", 6);
    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawFull().drawBorder();

        int slot = 10;

        for ( PlayerData playerData : PlayerDataConfig.getInstance().getPlayerDatas() ) {
            if ( playerData.isPrison() ) {

                AlarmStar as = (AlarmStar) AlarmStarConfig.getInstance(AlarmStarConfig.class).get(String.valueOf(playerData.getStarCount()));

                super.inventoryUI.setItem(slot, new InventoryUI.AbstractSuperClickableItem() {
                    @Override
                    public ItemStack getItemStack() {
                        return new ItemBuilder(Material.SKULL).setName("§a" + playerData.getPlayer().getName())
                                .setSkullOwner(playerData.getPlayer().getName())
                                .setLore("", "§7保释金: §a" + as.getPay() + " 元", "", "§a左键保释 §7| §e右键劫狱", "")
                                .toItemStack();
                    }

                    @Override
                    public boolean onClick(InventoryClickEvent e) {
                        if ( e.isRightClick() ) {
                            String cmd = AlarmStarSystem.getInstance().getConfig().getString("Settings.bail.rescue");
                            cmd = PlaceholderAPI.setPlaceholders(player, cmd);

                            player.performCommand(cmd.replace("%player%", playerData.getPlayer().getName()));
                        } else {
                            EconomyResponse er = AlarmStarSystem.getEcon().withdrawPlayer(player, as.getPay());

                            if ( er.transactionSuccess() ) {
                                playerData.setPrison(false);
                            } else {
                                player.sendMessage("§c你的钱余额不足以令其保释!");
                                player.getWorld().strikeLightningEffect(player.getLocation());
                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_FALL, 1, 1);
                            }
                        }
                        return false;
                    }
                });
                slot++;

                // ai auto generate (it seems simply 23333)
                if ( slot == 17 ) {
                    slot = 19;
                } else if ( slot == 26 ) {
                    slot = 28;
                } else if ( slot == 35 ) {
                    slot = 37;
                } else if ( slot == 44 ) {
                    slot = 46;
                } else if ( slot == 53 ) {
                    slot = 55;
                }
            }
        }

    }
}
