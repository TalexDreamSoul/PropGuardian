package com.tagzxia3.te.src.main.java.com.talexs.soultech.items.tools.pickaxe.breakhammer;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.tools.pickaxe.breakhammer.BaseBreakHammer;
import com.tagzxia.te.src.main.java.com.talexs.soultech.items.tools.pickaxe.breakhammer.GoldHammerEmbryo;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class GoldHammer extends BaseBreakHammer {

    private final GoldHammerEmbryo ghe;

    public GoldHammer(GoldHammerEmbryo ghe) {

        super("gold", new ItemBuilder(Material.GOLDEN_PICKAXE)
                .addEnchant(Enchantment.DIG_SPEED, 1).setName("§f◆ 残缺破碎锤(金)").setLore("", "§8| §e破坏总比创造易!", "&8| &7难以得到有效物品").toItemStack());


        this.ghe = ghe;
    }

    public FurnaceCauldronRecipe getRecipe() {

        return new FurnaceCauldronRecipe("stc_break_hammer_gold", this, 300000).setNeed(ghe);

    }

    @Override
    public ItemStack produce(PlayerData playerData, Block block) {

        Material material = block.getType();

        if ( material == Material.END_STONE ) {

            if ( Math.random() <= 0.0001 ) {

                return SoulTechItem.get("space_dust");

            }

        }

        return null;

    }

}
