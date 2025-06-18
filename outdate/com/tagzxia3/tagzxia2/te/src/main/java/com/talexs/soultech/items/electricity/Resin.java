package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.items.electricity;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.electricity.BaseElectricityItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.Set;

public class Resin extends BaseElectricityItem {

    public Resin() {

        super("resin", new ItemBuilder(Material.SLIME_BALL)

                .setName("§f树脂")
                .setLore("", "§8| §a洗净的树脂..", "")

                .toItemStack());

    }

    @Override
    public boolean canUseAsOrigin() {

        return true;
    }

    @Override
    public void onInteract(PlayerData playerData, PlayerInteractEvent event) {

        new PlayerUser(event.getPlayer()).actionBar("§a请不要用这一坨像是鼻涕的东西乱摸!").playSound(Sound.ENTITY_VILLAGER_NO, 1.2F, 1.2F);

    }

    @Override
    public void throwItem(PlayerData playerData, PlayerDropItemEvent event) {

        new PlayerUser(event.getPlayer()).actionBar("§a§l你扔出了这一坨黏糊糊的东西!真恶心!").playSound(Sound.ENTITY_VILLAGER_NO, 1.1F, 1.1F);

    }

    @Override
    public void onItemHeld(PlayerData playerData, PlayerItemHeldEvent event) {

        new PlayerUser(event.getPlayer()).title("", "§a你摸着 \"鼻涕\" !", 3, 10, 2);

    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(1));
    }

}
