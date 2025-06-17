package com.tagzxia2.te.src.main.java.com.talexs.soultech.items.tools.beheaded;

import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.soultech.items.tools.BaseToolItem;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import top.zoyn.particlelib.pobject.Ray;

import java.util.HashMap;
import java.util.Set;

public class NetheriteBeheadedPlus extends BaseToolItem {
    public NetheriteBeheadedPlus() {
        super("st_netherite_beheaded_plus", new ItemBuilder(Material.NETHERITE_SWORD).setName("&e斩首剑 &b&l+")
                .setLore(
                        "",
                        "&8| &e纯净&7，而不含一丝杂质！",
                        "&8| &e斩首&7，而不留一丝余地！",
                        "&8| &e锋芒&7，而不含一丝瑕疵！",
                        "&8| &e裂魂&7，而不留一丝残魄！",
                        "",
                        "&8| &7强化的材料能更好保存头颅",
                        "&8| &7右键完全释放斩首剑的力量",
                        "",
                        "&8| &7附加技 &b魂嗜",
                        "&8| &7  每次攻击时转换部分伤害为生命",
                        "&8|",
                        "&8| &7附加技 &b耐掠",
                        "&8| &7  星落引雷 触发时转换部分伤害为耐久",
                        "&8|",
                        "&8| &7主动技 &e星落引雷",
                        "&8| &7  对前方 6 格以内的任意生物造成巨额伤害",
                        ""
                )
                .addEnchant(Enchantment.LOOT_BONUS_MOBS, 10)
                .addEnchant(Enchantment.DAMAGE_ALL, 8)
                .addEnchant(Enchantment.SWEEPING_EDGE, 5)
                .addEnchant(Enchantment.DURABILITY, 8)
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_netherite_beheaded_plus", this)
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.SCULK_CATALYST))
                .addRequiredNull()
                .addRequired("st_netherite_beheaded")
                .addRequired("st_strengthen_netherite_stick")
                .addRequired("st_netherite_beheaded")
                .addRequired("st_strengthen_emerald_stick")
                .addRequired("st_strengthen_emerald_stick")
                .addRequired("st_strengthen_emerald_stick")
                ;
    }

    private static HashMap<String, Long> cd = new HashMap<>();

    @Override
    public void onInteract(PlayerData playerData, PlayerInteractEvent event) {
        if ( event.getAction().name().contains("LEFT") ) return;

        Player player = event.getPlayer();
        long now = System.currentTimeMillis();
        long diff = now - cd.getOrDefault(playerData.getName(), -1L);
        if ( diff < 10000 ) {
            new PlayerUser(player).errorActionBar("斩首剑技能冷却中!");
            return;
        }

        if ( player.getFoodLevel() < 4 ) {
            new PlayerUser(player).errorActionBar("饱食度不足!");
            return;
        }

        Entity targetEntity = player.getTargetEntity(6);
        if ( targetEntity == null ) {
            new PlayerUser(player).errorActionBar("你只能对面前六格以内的实体发动攻击!");
            return;
        }

        cd.put(playerData.getName(), now);
        player.setFoodLevel((int) (player.getFoodLevel() * 0.6));

        LivingEntity le = (LivingEntity) targetEntity;

        Location eyeLocation = player.getEyeLocation();
        Location launchLocation = eyeLocation.add(player.getLocation().getDirection().multiply(1.2));

        Ray ray = new Ray(launchLocation, le.getEyeLocation().add(0, 1.8, 0).getDirection(), 10, 0.2);

        ray.setPeriod(40);
        ray.setParticle(Particle.FIREWORKS_SPARK);

        double damage = le.getHealth() * 0.6 + 8;
        EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(player, le, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage);

        Bukkit.getPluginManager().callEvent(damageEvent);

        if ( !damageEvent.isCancelled() ) {
            targetEntity.setVelocity(targetEntity.getLocation().getDirection().multiply(2.5).setY(1.25));

            ray.show();
            le.damage(damageEvent.getDamage(), player);
            le.getWorld().strikeLightning(le.getEyeLocation());

            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);

            PlayerInventory inventory = player.getInventory();

            ItemStack item = inventory.getItemInMainHand();

            item.setDurability((short) (item.getDurability() - Math.abs(damage * 0.25)));

            inventory.setItemInMainHand(item);

            double health = player.getHealth();

            health += damage * 0.15;

            if ( health > player.getMaxHealth() ) return;

            player.setHealth(health);
        }

    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(150));
    }

}
