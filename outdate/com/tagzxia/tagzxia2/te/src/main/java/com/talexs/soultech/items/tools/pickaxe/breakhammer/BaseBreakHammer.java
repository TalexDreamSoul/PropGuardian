package com.tagzxia2.te.src.main.java.com.talexs.soultech.items.tools.pickaxe.breakhammer;

import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.tools.BaseToolItem;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Set;


public abstract class BaseBreakHammer extends BaseToolItem {

    public BaseBreakHammer(String ID, ItemStack stack) {

        super("st_break_hammer_" + ID, stack);

        addIgnoreType(SoulTechItem.VerifyIgnoreTypes.IgnoreAmount)
                .addIgnoreType(SoulTechItem.VerifyIgnoreTypes.IgnoreDurability)
                .addIgnoreType(SoulTechItem.VerifyIgnoreTypes.IgnoreEnchants)
                .addIgnoreType(SoulTechItem.VerifyIgnoreTypes.IgnoreItemFlags)
                .addIgnoreType(SoulTechItem.VerifyIgnoreTypes.IgnoreUnbreakable);

    }

    @Override
    public boolean useItemBreakBlock(PlayerData playerData, BlockBreakEvent event) {

        Block block = event.getBlock();

        if ( Math.random() <= 0.00325 ) {

            block.setType(Material.AIR);

            Player player = event.getPlayer();

            player.getLocation().getWorld().createExplosion(block.getLocation(), 1.45F);

            player.setItemInHand(null);
            Vector vector = player.getVelocity();

            vector.multiply(-1.5).setY(1.85).setZ(-1.2).setX(-1.2);

            player.setVelocity(vector);

            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.2F, 1.2F);
            player.sendTitle("§c砰!", "§e你的破碎锤爆炸了..", 4, 8, 3);

        } else {

            ItemStack target = produce(playerData, block);

            if ( target != null ) {

                block.setType(Material.AIR);

                ItemStack stack = event.getPlayer().getItemInHand();
                event.getPlayer().setItemInHand(stack);

                block.getWorld().spawnParticle(Particle.CLOUD, block.getLocation().clone().add(0.5, 0.5, 0.5), 3);
                block.getWorld().dropItem(block.getLocation(), target);

            } else {

                event.setCancelled(true);
                new PlayerUser(event.getPlayer()).errorActionBar("破碎锤只能破坏指定的物品!").playSound(Sound.BLOCK_ANVIL_LAND, 1.1F, 1.1F);

            }

        }

        return true;

    }

    public abstract ItemStack produce(PlayerData playerData, Block block);

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(15));
    }

}
