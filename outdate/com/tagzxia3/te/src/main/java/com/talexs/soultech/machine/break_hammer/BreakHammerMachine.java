package com.tagzxia3.te.src.main.java.com.talexs.soultech.machine.break_hammer;

import com.tagzxia.te.src.main.java.com.talexs.soultech.machine.break_hammer.BreakHammerRecipe;
import com.talexs.soultech.internal.book.IndicateBook;
import com.talexs.soultech.internal.entity.classfies.Classifies;
import com.talexs.soultech.internal.entity.tech_object.TechObject;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.machine.bsae.BaseMachine;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.block.TalexBlock;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class BreakHammerMachine extends BaseMachine {

    public BreakHammerMachine() {

        super("BreakHammer", new ItemBuilder(Material.STONE_PICKAXE)

                .setName("§e破碎锤")
                .setLore("", "§8| §a通过 §b破碎锤 §8敲击物品!", "")

                .toItemStack(), (ignored) -> {

            return false;
        });

    }

    @Override
    public void onOpenMachine(PlayerData playerData, PlayerEvent playerEvent) {

    }

    @Override
    public boolean onOpenRecipeView(IndicateBook guiderBook) {
        Classifies classifies = guiderBook.getClassifies();

        if ( !(classifies instanceof TechObject techObject) ) return false;

        RecipeObject recipeObject = techObject.getRecipe();

        if (recipeObject instanceof BreakHammerRecipe hammerRecipe) {

            guiderBook.inventoryUI.setItem(21, new InventoryUI.EmptyClickableItem(hammerRecipe.getDisplayRequireHammerTool()));

            guiderBook.inventoryUI.setItem(22, new InventoryUI.AbstractSuperClickableItem() {

                @Override
                public ItemStack getItemStack() {

                    return hammerRecipe.getRequire();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {

                    if ( hammerRecipe.getRequire() == null ) {
                        return true;
                    }

                    Classifies co = hammerRecipe.getRequire().getOwnCategoryObject();

                    if ( co == null ) {

                        guiderBook.getUser().actionBar("§c§l无法从 §e§l灵魂科技配方表§c§l 找到这个物品的配方!").playSound(Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);

                    } else {

                        guiderBook.setClassifies(co).open(true);
//                        new IndicateBook(guiderBook.getActivePlayerData(), 0, co, guiderBook).open();

                    }

                    return false;

                }
            });

            guiderBook.inventoryUI.setItem(24, new InventoryUI.EmptyClickableItem(hammerRecipe.getExport()));

            return true;

        }

        return false;

    }

    @Override
    public String onSave() {

        return "";
    }

    @Override
    public void onLoad(String str) {

    }

    @Override
    public boolean onBreakMachine(TalexBlock tblock) {
        return false;
    }

    @Override
    public void onOpenMachineInfoViewer(Player player) {

        new PlayerUser(player).infoActionBar("通过 手持破碎锤 敲碎指定物品 即可!");

    }

}
