package com.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.attract;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.AttractAddon;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.IAttractAddon;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.PlayerAttractData;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.utils.MathUtil;
import com.talexs.talexessential.utils.MathUtils;
import com.talexs.talexessential.utils.ParticleUtil;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.player.InventoryUtils;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttractGUI extends MenuBasic {
    public AttractGUI(Player player) {
        super(player, "&e引波吸呐", 6);
        
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1);
    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawFull().drawBorder();
        PlayerData pd = PlayerData.g(player);
        PlayerAttractData pad = pd.getAttractData();

        if ( !pad.isEnable() ) {

            inventoryUI.setItem(49, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    return new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("&c暂未开启")
                            .setLore("", "&8| &e引波吸呐&7, 帮助你快速拾取物品.", "", "&7当前状态: &c未开启", "", "&e消耗:", "&7- &e1000 &7金币", "&7- &e30级 &7等级", "&7- &e末影珍珠 &7x16", "", "&e点击开启引波吸呐", "")
                            .toItemStack()
                            ;
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    player.closeInventory();
                    int amo = InventoryUtils.getPlayerItemInInventory(player, Material.ENDER_PEARL);

                    if ( amo < 16 ) {
                        player.sendActionBar("§e末影珍珠 §7不足!");
                        player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    }

                    if ( player.getLevel() < 30 ) {
                        player.sendActionBar("§e等级 §7不足!");
                        player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    }

                    EconomyResponse r = TalexEssential.getInstance().getEcon().withdrawPlayer(player, 1000);
                    if ( !r.transactionSuccess() ) {
                        player.sendActionBar("§e金币 §7不足!(" + r.errorMessage + ")");
                        player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    }

                    InventoryUtils.deletePlayerItem(player, Material.ENDER_PEARL, 16);
                    player.setLevel(player.getLevel() - 30);

                    pad.setEnable(true);

                    player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
                    player.playEffect(player.getLocation(), Effect.FIREWORK_SHOOT, 1);
                    player.sendTitle("", "§7- §e引波吸呐 §a已开启 §7-", 10, 40, 10);

                    ParticleUtil.CircleUp(player, 2, Particle.VILLAGER_HAPPY);

                    return false;
                }
            });

            return;

        }

        IAttractAddon[] addons = new IAttractAddon[] { pad.getExpansion(), pad.getCoolDown(), pad.getParticles() };
        int startSlot = 10;

        for (AttractAddon addon : AttractAddon.values()) {

            // 让 startSlot 取模后得到 index
            int index = (startSlot - 10) / 9;
            if (index < addons.length) {

                IAttractAddon a = addons[index];

                inventoryUI.setItem(startSlot - 1, new InventoryUI.AbstractSuperClickableItem() {
                    @Override
                    public ItemStack getItemStack() {

                        List<String> lore = new ArrayList<>();

                        addon.getLore().forEach(str -> {
                            String s = str.replace("%level%", String.valueOf(a.getLevel())).replace("%value%", a.getLevelDisplayed());

                            lore.add(s);
                        });

                        return new ItemBuilder(addon.getMaterial()).setName(addon.getName()).setAmount(a.getLevel() + 1).setLore(lore).toItemStack();
                    }

                    @Override
                    public boolean onClick(InventoryClickEvent e) {
                        return false;
                    }
                });

                for ( int j = 0; j < 8; j++ ) {
                    int slot = startSlot + j;

                    if ( a.getLevel() >= j ) {
                        int finalJ = j;
                        inventoryUI.setItem(slot, new InventoryUI.AbstractSuperClickableItem() {
                          @Override
                          public ItemStack getItemStack() {
                              List<String> lore = new ArrayList<>();

                              addon.getLore().forEach(str -> {
                                  String s = str.replace("%level%", String.valueOf(a.getLevel() + 1)).replace("%value%", a.getLevelDisplayed(finalJ + 1));

                                  lore.add(s);
                              });

                              ItemBuilder itemBuilder = new ItemBuilder(addon.getUnlockMaterial()).setName("&e" + MathUtils.toRome(finalJ + 1) + "级");

                              if ( a.getLevel() > finalJ ) {
                                  lore.add("&a等级已解锁!");
                                  itemBuilder.addEnchant(Enchantment.ARROW_FIRE, 1).addFlag(ItemFlag.HIDE_ENCHANTS);
                              } else lore.add("&7点击升级到 &e" + MathUtils.toRome(finalJ + 1) + "级");

                              lore.addAll(Arrays.asList("", "&7消耗:", "&7- &e" + a.getLevelCostMoney(finalJ) + " &7金币",
                                      "&7- &e" + a.getLevelCostExp(finalJ) + " &7等级", "&7- &e末影珍珠" + " &7x" + a.getLevelCostEnderPearl(finalJ),
                                      "&7- &e" + getProficiency(finalJ) + " &7熟练度",
                                      ""));

                              return itemBuilder.setLore(lore)
                                      .toItemStack();
                          }

                          @Override
                          public boolean onClick(InventoryClickEvent e) {
                              if ( finalJ - a.getLevel() != 0 ) {
                                  return false;
                              }

                              player.closeInventory();
                              int amo = InventoryUtils.getPlayerItemInInventory(player, Material.ENDER_PEARL);

                              int proficiency = getProficiency(a.getLevel());

                              if ( pad.getUseAmo() < proficiency ) {
                                    player.sendActionBar("§e熟练度 §7不足!");
                                    player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
                                    return false;
                              }

                              if ( amo < a.getLevelCostEnderPearl(finalJ) ) {
                                  player.sendActionBar("§e末影珍珠 §7不足!");
                                  player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
                                  return false;
                              }

                              if ( player.getLevel() < a.getLevelCostExp(finalJ) ) {
                                  player.sendActionBar("§e等级 §7不足!");
                                  player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
                                  return false;
                              }

                              EconomyResponse r = TalexEssential.getInstance().getEcon().withdrawPlayer(player, a.getLevelCostMoney(finalJ));
                              if ( !r.transactionSuccess() ) {
                                  player.sendActionBar("§e金币 §7不足!(" + r.errorMessage + ")");
                                  player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
                                  return false;
                              }

                              InventoryUtils.deletePlayerItem(player, Material.ENDER_PEARL, a.getLevelCostEnderPearl(finalJ));
                              player.setLevel(player.getLevel() - a.getLevelCostExp(finalJ));

                              a.levelUp();

                              player.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
                              player.playEffect(player.getLocation(), Effect.FIREWORK_SHOOT, 1);
                              player.sendTitle("", "§7- §e" + ChatColor.translateAlternateColorCodes('&', addon.getName()) + " §a已升级 §7-", 10, 40, 10);

                              ParticleUtil.CircleJumpUp(player, Particle.FIREWORKS_SPARK, 2);

                              return false;
                          }
                      });
                    }

                }

            }

            startSlot += 9;

        }

        int costMoney = accCostMoney(pad.getExpansion(), pad.getExpansion().getLevel()) + accCostMoney(pad.getCoolDown(), pad.getCoolDown().getLevel()) + accCostMoney(pad.getParticles(), pad.getParticles().getLevel());
        int costExp = accCostExp(pad.getExpansion(), pad.getExpansion().getLevel()) + accCostExp(pad.getCoolDown(), pad.getCoolDown().getLevel()) + accCostExp(pad.getParticles(), pad.getParticles().getLevel());
        int costEnderPearl = accCostEnderPearl(pad.getExpansion(), pad.getExpansion().getLevel()) + accCostEnderPearl(pad.getCoolDown(), pad.getCoolDown().getLevel()) + accCostEnderPearl(pad.getParticles(), pad.getParticles().getLevel());

        inventoryUI.setItem(49, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.PLAYER_HEAD).setName("&e" + player.getName() + " &7的档案")
                        .setSkullOwner(player.getName())
                        .setLore("", "&7使用次数: &e" + pad.getUseAmo() + "次", "&7累计吸收: &e" + pad.getAccItems() + "个", ""
                        , "&8累计消耗:", "&7- &e" + costMoney + " &7金币", "&7- &e" + costExp + " &7等级", "&7- &e末影珍珠 &7x" + costEnderPearl, "")
                        .toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                return false;
            }
        });

    }

    private int getProficiency(int level) {
//        (int) ((int) Math.PI * Math.atan(a.getLevel() + 10))
        return (int) Math.E * level * 10 + 10;
    }

    private int accCostMoney(IAttractAddon addon, int level) {
        int m = 0;

        for ( int i = 1; i <= level; i++ ) {
            m += addon.getLevelCostMoney(i);
        }

        return m;
    }

    private int accCostExp(IAttractAddon addon, int level) {
        int m = 0;

        for ( int i = 1; i <= level; i++ ) {
            m += addon.getLevelCostExp(i);
        }

        return m;
    }

    private int accCostEnderPearl(IAttractAddon addon, int level) {
        int m = 0;

        for ( int i = 1; i <= level; i++ ) {
            m += addon.getLevelCostEnderPearl(i);
        }

        return m;
    }
}
