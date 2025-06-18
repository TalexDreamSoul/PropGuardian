package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.feather.boots;

import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.soultech.items.feather.BaseFeatherItem;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.ParticleUtil;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Set;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.items.boots }
 *
 * @author TalexDreamSoul
 * @date 2021/8/16 15:33
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
public class FeatherJumperBoot extends BaseFeatherItem {

    HashMap<String, Long> CD = new HashMap<>();

    public FeatherJumperBoot() {

        super("feather_jumper_boots", new ItemBuilder(Material.NETHERITE_BOOTS)
                .setName("&e轻辉蹦蹦靴")
                .setLore("", "§8| §a奇妙, 无与伦比", "&8| §e在空中蹲下可飞跃.", "&8| &7免疫一切摔落伤害", "&8| &7蹲下右键切换模式", "")
                .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10)
                .addEnchant(Enchantment.SOUL_SPEED, 5)
                .addEnchant(Enchantment.DURABILITY, 8)
                .addEnchant(Enchantment.THORNS, 5)
                .addEnchant(Enchantment.DEPTH_STRIDER, 5)
                .attributeSpeedScale(.1, EquipmentSlot.FEET)
                .toItemStack());
    }

    @Override
    public RecipeObject getRecipe() {

        return new WorkBenchRecipe("jumper_boots", this)

                .addRequiredNull()
                .addRequiredNull()
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.HONEY_BLOCK))
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.HONEY_BLOCK))
                .addRequired("resin")
                .addRequired("feather_core")
                .addRequired("resin")

                ;
    }

    @Override
    public void onInteract(PlayerData playerData, PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if ( !player.isSneaking() ) return;

        PlayerInventory inventory = player.getInventory();

        ItemStack stack = inventory.getItemInMainHand();

        ItemMeta meta = stack.getItemMeta();

        if ( meta.hasEnchant(Enchantment.DEPTH_STRIDER) ) {
            meta.removeEnchant(Enchantment.DEPTH_STRIDER);
            meta.addEnchant(Enchantment.FROST_WALKER, 3, true);
        } else {
            meta.removeEnchant(Enchantment.FROST_WALKER);
            meta.addEnchant(Enchantment.DEPTH_STRIDER, 5, true);
        }

        stack.setItemMeta(meta);

        player.damageItemStack(stack, 10);

        inventory.setItemInMainHand(stack);

        event.setCancelled(true);

        new PlayerUser(event.getPlayer()).infoActionBar("模式已切换!").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);

    }

    @Override
    public void onItemHeld(PlayerData playerData, PlayerItemHeldEvent event) {
        new PlayerUser(event.getPlayer()).infoActionBar("蹲下右键以切换模式!");
    }

    @Override
    public void onDamaged(PlayerData playerData, EntityDamageEvent event) {

        Player player = playerData.getOfflinePlayer().getPlayer();

        assert player != null;
        ItemStack stack = player.getInventory().getBoots();

        if ( !checkID(stack) ) {
            return;
        }

        if ( event.getCause() == EntityDamageEvent.DamageCause.FALL ) {

            event.setCancelled(true);

            Vector velocity = player.getVelocity();

            player.setVelocity(velocity.setY(Math.abs(velocity.getY() * 0.75) + 0.55));

            ParticleUtil.Circle(player.getLocation().add(0.5, -0.25, 0.5), Particle.CLOUD, 0.5, 0.5);

            assert stack != null;
            player.damageItemStack(stack, (int) (-.25 * event.getDamage()));

        }

    }

    @Override
    public void onSneak(PlayerData playerData, PlayerToggleSneakEvent event) {

        if ( !event.isSneaking() ) {
            return;
        }

        Player player = event.getPlayer();

        ItemStack stack = player.getInventory().getBoots();

        if ( !checkID(stack) ) {
            return;
        }

        if ( player.isOnGround() || player.isFlying() || player.isInLava() || player.isInWater() ) {
            return;
        }

        if ( CD.containsKey(player.getName()) ) {

            if ( System.currentTimeMillis() - CD.get(player.getName()) < 2000 ) {
                return;
            }

        }

        if ( player.getFoodLevel() < 1 ) {

            new PlayerUser(player).playSound(Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0f, 1.2f).title("", "§7饱食度不足!!", 5, 15, 10);

            return;

        }

        player.setFoodLevel((int) ( player.getFoodLevel() * 0.85 ) - 3);

        Vector vector = player.getVelocity();

        player.setVelocity(vector.setY(1.85));

        new PlayerUser(player).playSound(Sound.ENTITY_PARROT_IMITATE_ENDERMITE, 1f, 1.1f);

        CD.put(player.getName(), System.currentTimeMillis());

        ParticleUtil.Circle(player.getLocation(), Particle.BUBBLE_COLUMN_UP, 0.5, 0.5);

    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(8), new LevelCondition(100));
    }
}
