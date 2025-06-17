package com.tagzxia.te.src.main.java.com.talexs.soultech.machine.advanced_workbench;

import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.entity.classfies.Classifies;
import com.talexs.soultech.internal.entity.classfies.ClassifiesAddon;
import com.talexs.soultech.internal.entity.tech_object.TechObject;
import com.talexs.soultech.internal.entity.tech_object.TechObjectAddon;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.items.TalexItem;
import com.talexs.soultech.machine.bsae.MachineGUI;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AdvancedWorkBenchGUI extends MachineGUI {

    private final int[] empties = new int[] { 12, 13, 14, 21, 22, 23, 30, 31, 32 };

    public AdvancedWorkBenchGUI() {

        super("§b高级工作台");
    }

    public void dropContents() {
        for ( int i : empties ) {

            ItemStack stack = inventoryUI.getCurrentPage().getItem(i);

            if ( stack != null && stack.getType() != Material.AIR ) {

                new PlayerUser(player).dropItem(stack);

            }

        }
    }

    public ItemStack[] getStacks() {
        ItemStack[] stacks = new ItemStack[empties.length];

        for ( int i = 0; i < empties.length; ++i ) {

            ItemStack item = inventoryUI.getCurrentPage().getItem(empties[i]);

            if ( item == null || item.getType() == Material.AIR ) {
                continue;
            }

            stacks[i] = item;

        }

        return stacks;
    }

    private boolean first = false;

    public void setStacks(ItemStack[] stacks) {
        init();

        for ( int i = 0; i < empties.length; ++i ) {

            ItemStack item = stacks[i];

            if ( item == null || item.getType() == Material.AIR )
                continue;

            inventoryUI.getCurrentPage().setItem(empties[i], item);

        }
    }

    private void init() {
        if ( !first ) {
            first = true;

            new InventoryPainter(this).drawFull().drawBorder();

            inventoryUI.setItem(12, new InventoryUI.EmptyClickableItem(null));
            inventoryUI.setItem(13, new InventoryUI.EmptyClickableItem(null));
            inventoryUI.setItem(14, new InventoryUI.EmptyClickableItem(null));

            inventoryUI.setItem(21, new InventoryUI.EmptyClickableItem(null));
            inventoryUI.setItem(22, new InventoryUI.EmptyClickableItem(null));
            inventoryUI.setItem(23, new InventoryUI.EmptyClickableItem(null));

            inventoryUI.setItem(30, new InventoryUI.EmptyClickableItem(null));
            inventoryUI.setItem(31, new InventoryUI.EmptyClickableItem(null));
            inventoryUI.setItem(32, new InventoryUI.EmptyClickableItem(null));

        }
    }

    @Override
    public void SetupForPlayer(Player player) {

        init();

        inventoryUI.setItem(40, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                return new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDurability((short) 5).setLore("", "§8| §a伟大的, 不是创造", "&8| &7按下 &eSHIFT &7开启批量合成", "").setName("§a合成").toItemStack();

            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                PlayerUser user = new PlayerUser((Player) e.getWhoClicked());

                List<Classifies> classifiesArray = new ArrayList<>(
                        List.of(AddonHolder.getINSTANCE().get(ClassifiesAddon.class).getRoot())
                );

                while ( !classifiesArray.isEmpty() ) {
                    Classifies cls = classifiesArray.remove(0);

                    if ( !cls.getChildren().isEmpty() ) {
                        classifiesArray.addAll(cls.getChildren());
                    }

                    if ( !(cls instanceof TechObject techObject) ) {
                        continue;
                    }

                    if ( !(techObject.getRecipe() instanceof WorkBenchRecipe wbr) ) {
                        continue;
                    }

                    if ( validateRecipe(user, wbr, e) ) {
                        return true;
                    }

                }

                user.errorActionBar("没有这个配方").playSound(Sound.ENTITY_VILLAGER_NO, 1.2F, 1.2F);

                return true;

            }

        });

    }

    public boolean validateRecipe(PlayerUser user, WorkBenchRecipe wbr, InventoryClickEvent e) {
        int accessAmount = 0;
        int least = Integer.MAX_VALUE;

        for ( int i = 0; i < empties.length; ++i ) {

            ItemStack item = inventoryUI.getCurrentPage().getItem(empties[i]);

            TalexItem ti = wbr.getRecipeAsID(i + 1);

            if ( ti == null || ti.getItemBuilder().toItemStack().getType() == Material.AIR ) {

                if ( item == null || item.getType() == Material.AIR ) {
                    accessAmount++;
                }

                continue;

            }

            ti = new TalexItem(ti.getItemBuilder()).addIgnoreType(TalexItem.VerifyIgnoreTypes.IgnoreAmount);

            if ( ti.verify(item) ) {

                if (item != null) {
                    least = Math.min(item.getAmount(), least);
                }
                accessAmount++;

            }

        }

        least = e.isShiftClick() ? least : 1;

        if ( accessAmount >= 9 ) {

            for ( int i = 0; i < empties.length; ++i ) {

                ItemStack stack = inventoryUI.getCurrentPage().getItem(empties[i]);

                TalexItem ti = wbr.getRecipeAsID(i + 1);

                if ( ti == null || ti.getItemBuilder().toItemStack().getType() == Material.AIR ) {
                    accessAmount++;
                    continue;
                }

                if (stack != null && stack.getAmount() >= 1) {
                    if ( stack.getAmount() == least )
                        inventoryUI.setItem(empties[i], new InventoryUI.EmptyClickableItem(null));
                    else
                        inventoryUI.setItem(empties[i], new InventoryUI.EmptyClickableItem(new ItemBuilder(stack).setAmount(stack.getAmount() - least).toItemStack()));
                }

            }

            if ( wbr.getExport() == null || wbr.getExport().getItemBuilder().toItemStack().getType() == Material.AIR ) {

                return true;

            }

            TalexItem item = wbr.getExport();

            if ( item instanceof SoulTechItem) {

                ( (SoulTechItem) item ).onCrafted(player);

            }

            ItemStack itemStack = item.getItemBuilder().setAmount(wbr.getAmount()).toItemStack();
            user.dropItem(item.getItemBuilder().setAmount(itemStack.getAmount() * least).toItemStack()).actionBar("§a§l合成成功!")
                    .playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.3F, 1.3F);

            return true;

        }

        return false;

    }

}
