package com.tagzxia3.te.src.main.java.com.talexs.soultech.machine.machine_info;

import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.entity.items.TalexItem;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InfoWorldConstruct extends MenuBasic {

    private final TalexItem display;

    public InfoWorldConstruct(Player player, TalexItem display) {

        super(player, "§5灵魂§b科技 §8> 世界构造", 5);

        this.display = display;

        new PlayerUser(player).playSound(Sound.BLOCK_NOTE_BLOCK_FLUTE, 1.1F, 1.1F);

    }

    @Override
    public void SetupForPlayer(Player player) {

        new InventoryPainter(this).drawFull().drawBorder();

        inventoryUI.setItem(22, new InventoryUI.EmptyClickableItem(this.display.getItemBuilder().toItemStack()));

        PlayerData pd = PlayerData.g(player);
        if ( pd.getPlayerSoul().getIndicateBook() != null ) {
            inventoryUI.setItem(40, new InventoryUI.AbstractSuperClickableItem() {

                @Override
                public ItemStack getItemStack() {

                    return new ItemBuilder(Material.ARROW).setName("§b◀ 返回上个界面").toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {

                    new PlayerUser(player).playSound(Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1.2F, 1.2F);

                    pd.getPlayerSoul().getIndicateBook()
                            .lastLevel()
                            .open(true);

                    return false;
                }
            });
        }

    }

}
