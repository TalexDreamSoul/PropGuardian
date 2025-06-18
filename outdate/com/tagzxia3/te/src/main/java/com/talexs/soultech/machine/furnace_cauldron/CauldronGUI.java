package com.tagzxia3.te.src.main.java.com.talexs.soultech.machine.furnace_cauldron;

import com.tagzxia.te.src.main.java.com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronObject;
import com.tagzxia.te.src.main.java.com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronRecipe;
import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.entity.classfies.Classifies;
import com.talexs.soultech.internal.entity.classfies.ClassifiesAddon;
import com.talexs.soultech.internal.entity.tech_object.TechObject;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.items.TalexItem;
import com.talexs.soultech.machine.bsae.MachineGUI;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.LocationFloat;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.machine.furnace_cauldron }
 *
 * @author TalexDreamSoul
 * @date 2021/8/14 4:22
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
public class CauldronGUI extends MachineGUI {

    private final FurnaceCauldronObject object;

    public CauldronGUI(FurnaceCauldronObject furnaceCauldronObject) {

        super("§c冶炼熔炉 ");

        this.object = furnaceCauldronObject;

        inventoryUI.setAutoRefresh(true);
        inventoryUI.setCouldClickPlayerInventory(object.getProcessingItem() == null && object.getSaveItems().size() == 0);

    }

    @Override
    public void SetupForPlayer(Player player) {

        InventoryPainter inventoryPainter = new InventoryPainter(this).drawFull().drawArena9(20, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                return new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setDurability((short) 14)
                        .setName("§e放置冶炼物品").toItemStack();

            }

            @Override
            public boolean onClick(InventoryClickEvent e) {

                return true;
            }

        });

        double percent = object.getProgressPercent();

        if ( object.getSaveItems().size() >= 9 ) {

            new InventoryPainter(this) {

                @Override
                public InventoryUI.ClickableItem onDrawLine(int slot) {

                    return new InventoryUI.EmptyCancelledClickableItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDurability((short) 14).setName("§c空位不足").toItemStack());

                }
            }.drawLineRow(6);

        } else {

            inventoryPainter.drawProgressBarHorizontal(5, 7, percent, LocationFloat.FLOAT_CENTER, new InventoryUI.EmptyCancelledClickableItem(

                    new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDurability((short) 3).setName("§a*").toItemStack()

            ), new InventoryUI.EmptyCancelledClickableItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§a*").setDurability((short) 4).toItemStack()));

        }

        inventoryUI.setItem(20, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                ItemBuilder builder;

                if ( object.getProcessingItem() == null ) {

                    builder = new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setDurability((short) 8);

                    builder.setName("§b点击放置物品");

                } else {

                    builder = new ItemBuilder(object.getProcessingItem());

                }

                return builder.toItemStack();

            }

            @Override
            public boolean onClick(InventoryClickEvent e) {

                ItemStack stack = e.getCursor();
                if ( e.isLeftClick() ) {

                    if ( stack != null ) {

                        if ( object.getProcessingItem() == null ) {

                            FurnaceCauldronRecipe recipe = searchCategory(stack);

                            if ( recipe == null ) {

                                new PlayerUser((Player) e.getWhoClicked())
                                        .actionBar("§c§l这个物品无法冶炼!")
                                        .closeInventory();

                                return false;

                            }

                            e.setResult(Event.Result.DENY);

                            object.setOwnRecipe(recipe);
                            object.setProcessingItem(stack.clone());

                            e.getWhoClicked().setItemOnCursor(null);

                            return false;

                        }

                    }

                    e.getWhoClicked().getInventory().addItem(stack);
                    object.setProcessingItem(null);
                    object.setRun(false);

                    return false;

                }

//                e.setCurrentItem(null);

                e.setResult(Event.Result.DENY);

//                e.getWhoClicked().getInventory().addItem(stack);


                open(true);

                return false;

            }
        });

        int startSlot = 14;

        for ( TalexItem item : new ArrayList<>(object.getSaveItems()) ) {

            inventoryUI.setItem(startSlot, new InventoryUI.AbstractClickableItem(item.getItemBuilder().toItemStack()) {

                @Override
                public boolean onClick(InventoryClickEvent e) {
//                    if (
//                            e.getAction() == InventoryAction.PICKUP_ALL || e.getAction() == InventoryAction.DROP_ALL_CURSOR || e.getAction() == InventoryAction.DROP_ALL_SLOT
//                                    || e.getAction() == InventoryAction.DROP_ONE_SLOT || e.getAction() == InventoryAction.COLLECT_TO_CURSOR
//                                    || e.getAction() == InventoryAction.DROP_ONE_CURSOR || e.getAction() == InventoryAction.PICKUP_ONE
//                    ) return true;
//
//                    e.getWhoClicked().sendMessage(e.getAction().name());

                    if ( object.getSaveItems().remove(item) ) {

                        if ( item instanceof SoulTechItem) {

                            ( (SoulTechItem) item ).onCrafted((Player) e.getWhoClicked());

                        }

                        e.setResult(Event.Result.DENY);

                        e.getWhoClicked().getInventory().addItem(item.getItemBuilder().toItemStack());
                        new PlayerUser(player)
//                                .addItem(item.getItemBuilder().toItemStack())
                                .playSound(Sound.ITEM_ARMOR_EQUIP_CHAIN, 1.1f, 1.2f);

                    }

                    return false;

                }
            });

            startSlot++;

            if ( ( startSlot + 1 ) % 9 == 0 ) {

                startSlot += 6;

            }

            if ( startSlot >= 44 ) {
                break;
            }

        }

    }

    private FurnaceCauldronRecipe searchCategory(ItemStack stack) {
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

            if ( !(techObject.getRecipe() instanceof FurnaceCauldronRecipe fcd) ) {
                continue;
            }

            TalexItem ti = fcd.getNeed();

            if ( ti != null && ti.getItemBuilder().toItemStack().getType() != Material.AIR ) {

                ti = new TalexItem(ti.getItemBuilder()).addIgnoreType(TalexItem.VerifyIgnoreTypes.IgnoreAmount);

                if ( ti.verify(stack) ) {

                    return fcd;

                }

            }

        }

        return null;

    }

}
