package com.tagzxia.te.src.main.java.com.talexs.soultech.items.alchemy.ingots;

import com.talexs.soultech.internal.RecipeObject;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.alchemy.BaseAlchemyItem;
import com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronRecipe;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.Set;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.items.material.ingots }
 *
 * @author TalexDreamSoul
 * @date 2021/8/14 22:06
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
public class FireEnderPearl extends BaseAlchemyItem {

    public FireEnderPearl() {

        super("fire_ender_pearl", new ItemBuilder(Material.ENDER_PEARL)
                .addEnchant(Enchantment.DURABILITY, 1)
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .setName("§4灼热珍珠").setLore("", "§8| §c微微的灼烧感..", "&8| &e右键女巫使其破除封印", "").toItemStack());
    }

    @Override
    public RecipeObject getRecipe() {

        return new FurnaceCauldronRecipe("fire_ender_pearl", this, 40).setExport(this).setNeed(new MineCraftItem(Material.ENDER_PEARL));

    }

    @Override
    public void onCrafted(Player player) {

        player.setFireTicks(player.getFireTicks() + 40);

    }

    @Override
    public void onInteractEntity(PlayerData playerData, PlayerInteractEntityEvent event) {
        Entity rightClicked = event.getRightClicked();

        if ( rightClicked.getType() == EntityType.WITCH ) {
            Player player = event.getPlayer();

            new PlayerUser(player)
                    .dropItem(SoulTechItem.get("fire_ender_eye"))
                    .reducePlayerHandItem(1);

            rightClicked.getWorld().strikeLightning(rightClicked.getLocation());

            LivingEntity livingEntity = (LivingEntity) rightClicked;

            livingEntity.setHealth(20);
            livingEntity.setGlowing(true);

            event.setCancelled(true);
        }
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(10));
    }
}
