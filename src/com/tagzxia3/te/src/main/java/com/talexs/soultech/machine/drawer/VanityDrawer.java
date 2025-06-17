package com.tagzxia3.te.src.main.java.com.talexs.soultech.machine.drawer;

import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.block.BlockAddon;
import com.talexs.soultech.internal.book.IndicateBook;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.StNameSpace;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.items.space.drawer.BaseDrawer;
import com.talexs.soultech.machine.bsae.BaseMachine;
import com.talexs.talexessential.utils.block.TalexBlock;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class VanityDrawer extends BaseMachine {
    public VanityDrawer() {
        super("VanityDrawer", new ItemBuilder(Material.END_PORTAL_FRAME)
                .setName("&b虚尽存储机器")
                .setLore(
                        "",
                        "&8| &7虚无终端的奥秘，你至今也无法理解",
                        "&8| &7但仍能使用它来存储物品",
                        "&8| &7存储的物品将会永久保存在虚空中",
                        "&8| &7它会自动向周围吸引相同物品",
                        "",
                        "&8| &e左键取拿 | 右键存放",
                        ""
                )
                .toItemStack(), event -> {
                    if ( event instanceof PlayerInteractEvent ) {
                        return BaseDrawer.isDrawer(((PlayerInteractEvent) event).getClickedBlock());
                    }
                    return false;
                });
    }

    @Override
    public void onOpenMachineInfoViewer(Player player) {

    }

    @Override
    public void onOpenMachine(PlayerData playerData, PlayerEvent event) {
        if ( event instanceof PlayerInteractEvent ) {
            PlayerInteractEvent pie = (PlayerInteractEvent) event;
            process(playerData, new PlayerUser(event.getPlayer()), pie);
        }
    }

    private void process(PlayerData pd, PlayerUser user, PlayerInteractEvent event) {
        BaseDrawer.DrawerObject drawer = BaseDrawer.getDrawer(event.getClickedBlock());
        if ( drawer == null ) return;

        event.setCancelled(true);

        ItemStack source = drawer.getStack();

        // take items
        if ( event.getAction() == Action.LEFT_CLICK_BLOCK ) {
            if ( source == null ) {
                user.infoActionBar("存储方块为空.").playSound(Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 1);
                return;
            }

            int amount = drawer.getAmount();
            int takeAmo = event.getPlayer().isSneaking() ? 64 : 1;

            if ( amount < takeAmo ) {
                takeAmo = amount;
            }

            ItemStack take = source.clone();
            take.setAmount(1);
            drawer.setAmount(amount - takeAmo);

            user.dropItem(take, takeAmo);

            return;
        }

        if (event.getPlayer().isSneaking()) {
            TalexBlock block = AddonHolder.getINSTANCE().get(BlockAddon.class).getBlock(event.getClickedBlock());
            if (block == null) {
                user.infoActionBar("物品已被锁定.").playSound(Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 1);
                return;
            }

            SoulTechItem item = block.getItem();
            if (item == null) {
                user.infoActionBar("物品已被锁定.").playSound(Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 1);
                return;
            }

            ItemBuilder itemBuilder = item.getItemBuilder();

            String itemData = source != null ? ItemUtil.item2Str(source) : "";
            Component nameComp = source == null ? Component.text("无") : ItemUtil.translateName(source);
//            String name = source != null ? ItemUtil.getName(source, false) : "无";

            // 6 7
            itemBuilder
                    .setLoreLine(Component.text("|").color(NamedTextColor.DARK_GRAY)
                            .append(Component.text(" 物品: ").color(NamedTextColor.GRAY))
                            .append(nameComp.color(NamedTextColor.YELLOW)), 6)
                    .setLoreLine("&8| &7容量: &e" + drawer.getAmount() + "/" + drawer.getMaxAmount(), 7)

                    .addPRCData(StNameSpace.VANITY_ITEM, itemData)
                    .addPRCData(StNameSpace.VANITY_AMO, String.valueOf(drawer.getAmount()))
                    .addPRCData(StNameSpace.VANITY_AMO_MAX, String.valueOf(drawer.getMaxAmount()));

            block.getLoc().getBlock().setType(Material.AIR);

            block.unregisterSelf();

            block.getLoc().getWorld().dropItemNaturally(block.getLoc(), itemBuilder.toItemStack());

            return;
        }

        if ( drawer.getAmount() >= drawer.getMaxAmount() ) {
            user.errorActionBar("虚尽存储方块已满.").playSound(Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 1);
            event.getPlayer().knockback(1.25, 2, 1.25);
            return;
        }

        PlayerInventory inventory = event.getPlayer().getInventory();

        ItemStack item = inventory.getItemInMainHand();
        if (item.getType() == Material.AIR) {
            user.infoActionBar("手上没有物品.").playSound(Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 1);
            return;
        }

        if ( source != null && !source.isSimilar(item) ) {
            user.errorActionBar("不同的物品无法堆叠.").playSound(Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 1);
            return;
        }

        ItemStack added = drawer.merge(item);

        inventory.setItemInMainHand(added);

        user.playSound(Sound.ENTITY_ITEM_FRAME_ADD_ITEM, 1, 1);

    }

    @Override
    public boolean onOpenRecipeView(IndicateBook guiderBook) {
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
}
