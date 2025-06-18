package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.items.tools.beheaded;

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

public class NetheriteBeheaded extends BaseToolItem {
    public NetheriteBeheaded() {
        super("st_netherite_beheaded", new ItemBuilder(Material.NETHERITE_SWORD).setName("&e斩首剑")
                .setLore(
                        "",
                        "&8| &e纯净&7，而不含一丝杂质！",
                        "&8| &e斩首&7，而不留一丝余地！",
                        "&8| &e锋芒&7，而不含一丝瑕疵！",
                        "&8| &e裂魂&7，而不留一丝残魄！",
                        "",
                        "&8| &7强化的材料能更好保存头颅",
                        "&8| &7右键完全释放斩首剑的力量",
                        ""
                )
                .addEnchant(Enchantment.LOOT_BONUS_MOBS, 10)
                .addEnchant(Enchantment.FIRE_ASPECT, 2)
                .addEnchant(Enchantment.KNOCKBACK, 2)
                .addEnchant(Enchantment.DAMAGE_ALL, 8)
                .addEnchant(Enchantment.SWEEPING_EDGE, 3)
//                .addEnchant(Enchantment.MENDING, 1)
                .addEnchant(Enchantment.DURABILITY, 6)
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_netherite_beheaded", this)
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.NETHERITE_BLOCK))
                .addRequiredNull()
                .addRequired("st_diamond_beheaded")
                .addRequired("st_strengthen_netherite_stick")
                .addRequired("st_diamond_beheaded")
                .addRequired("st_strengthen_emerald_stick")
                .addRequired("st_glow_dust")
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
        if ( diff < 15000 ) {
            new PlayerUser(player).errorActionBar("斩首剑技能冷却中!");
            return;
        }

        if ( player.getFoodLevel() < 8 ) {
            new PlayerUser(player).errorActionBar("饱食度不足!");
            return;
        }

        Entity targetEntity = player.getTargetEntity(8);
        if ( targetEntity == null ) {
            new PlayerUser(player).errorActionBar("你只能对面前五格以内的实体发动攻击!");
            return;
        }

        cd.put(playerData.getName(), now);
        player.setFoodLevel((int) (player.getFoodLevel() * 0.65));

        LivingEntity le = (LivingEntity) targetEntity;

        Location eyeLocation = player.getEyeLocation();
        Location launchLocation = eyeLocation.add(player.getLocation().getDirection().multiply(1.2));

        Ray ray = new Ray(launchLocation, le.getEyeLocation().add(0, 1.8, 0).getDirection(), 10, 0.2);

        ray.setPeriod(40);
        ray.setParticle(Particle.FIREWORKS_SPARK);

        double damage = le.getHealth() * 0.6 + 5;
        EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(player, le, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage);

        Bukkit.getPluginManager().callEvent(damageEvent);

        if ( !damageEvent.isCancelled() ) {
            targetEntity.setVelocity(targetEntity.getLocation().getDirection().multiply(1.75).setY(1.125));

            ray.show();
            le.damage(damageEvent.getDamage(), player);
            le.getWorld().strikeLightning(le.getEyeLocation());

            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);

            PlayerInventory inventory = player.getInventory();

            ItemStack item = inventory.getItemInMainHand();

            item.setDurability((short) (item.getDurability() - Math.abs(damage * 0.25)));

//            player.damageItemStack(item, (int) -damage);

            inventory.setItemInMainHand(item);
        }

    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(100));
    }
}
