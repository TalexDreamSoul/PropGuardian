package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.resource;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.resource.PlayerRes;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.resource.ResBoxGUI;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.resource.ResModule;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class ResGUI extends MenuBasic {
    public ResGUI(Player player) {
        super(player, "&7[&e资源挑战&7]", 5);
    }

    @Override
    public void SetupForPlayer(Player player) {
        player.playSound(player, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);

        new InventoryPainter(this).drawFull().drawBorder();
        PlayerData pd = PlayerData.g(player);
        PlayerRes pr = pd.getPlayerRes();

        inventoryUI.setItem(20, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                // if has items -> enchants
                ItemBuilder ib = new ItemBuilder(Material.CHEST).setName("&5战利品");

                ib.setLore("", "&8| &7每次挑战都可以获得许多战利品!", "", "&e当前暂无任何战利品.", "");

                return ib.toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                new ResBoxGUI(player).open();
                return false;
            }
        });

        inventoryUI.setItem(22, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                // if has items -> enchants
                ItemBuilder ib = new ItemBuilder(Material.GRASS_BLOCK).setName("&c挑战");

                if ( player.getWorld() == ResModule.INS.getWorld() ) {
                    ib.setLore("", "&8| &a资源&7, 风起云涌肃杀芒!", "&8| &e每 24H 重置, 凌晨04:00-06:00不开放", "&8| &d死亡后战利品全部丢失!", "&8| &7仅低等级可以主动攻击高等级玩家", "&8| &7所有秒杀暴击伤害全部失效", "&8| &a击败别人掠夺资源",
                            "", "&7剩余挑战机会: &c无限", "&7挑战冷却时间: &c暂无", "", "&7&k|&e 点击退出挑战 ...").addEnchant(Enchantment.ARROW_DAMAGE, 1).addFlag(ItemFlag.HIDE_ENCHANTS);
                } else ib.setLore("", "&8| &a资源&7, 风起云涌肃杀芒!", "&8| &e每 24H 重置, 凌晨04:00-06:00不开放", "&8| &d死亡后战利品全部丢失!", "&8| &7仅低等级可以主动攻击高等级玩家", "&8| &7所有秒杀暴击伤害全部失效", "&8| &a击败别人掠夺资源",
                        "", "&7剩余挑战机会: &c无限", "&7挑战冷却时间: &c暂无", "", "&7&k|&a 点击开始挑战.");

                return ib.toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                if ( player.getWorld() == ResModule.INS.getWorld() ) {
                    Location loc = pd.getPlayerRes().saveLoc;

                    if ( loc == null ) {
                        player.kickPlayer("§c你的挑战点未设置, 请重新登录，战利品仍然存在！");
                    } else
                        player.teleport(loc);

                    return false;
                }
                Location loc = ResModule.INS.getWorld().getSpawnLocation();

                Random random = new Random();
                loc.setX(random.nextInt(10000));
                loc.setZ(random.nextInt(10000));

                loc = loc.getWorld().getHighestBlockAt(loc).getLocation();

                pd.getPlayerRes().saveLoc = player.getLocation();
                pr.addAmo();

                player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 20 * 5, 2));

                player.teleport(loc);
                player.setGameMode(GameMode.SURVIVAL);

                new PlayerUser(player)
                        .actionBar("§e挑战开始, 请保护自己, 击杀别人可以掠夺资源！")
                        .title("§c死亡后你的战利品全部掉落", "§e所有挖掘物品进入战利品槽位", 0, 60, 20);

                return false;
            }
        });

        inventoryUI.setItem(24, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                // if has items -> enchants
                ItemBuilder ib = new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(player.getName()).setName("&c挑战");

                ib.setLore("", "&8| &7挑战次数: &e" + pr.getAmo(), "&8| &7死亡次数: &c" + pr.getAccFailed(), "&8| &7战利品率: &a150.00%", "&8| &7沙子沙砾: &a200.00%", "", "&7&k|&a 点击查看排名.");

                return ib.toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                return false;
            }
        });
    }
}
