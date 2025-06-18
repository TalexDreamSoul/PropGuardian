package com.tagzxia3.tagzxia1.te.src.main.resources.de.talex.machine.compress_machine;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.inventory.guider.GuiderBook;
import pubsher.talexsoultech.inventory.machine_info.InfoWorldConstruct;
import pubsher.talexsoultech.talex.machine.BaseMachine;
import pubsher.talexsoultech.talex.machine.MachineChecker;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.TalexItem;

public class Compressor extends BaseMachine {

    public Compressor() {

        super("compressor", new ItemBuilder(Material.PISTON_BASE)

                .setName("§e压缩机")
                .setLore("", "§8> §e9 个相同物品可以压缩", "")

                .toItemStack(), new MachineChecker() {

            @Override
            public boolean onCheck(PlayerEvent event) {

                if ( !( event instanceof PlayerInteractEvent ) ) {
                    return false;
                }

                PlayerInteractEvent e = (PlayerInteractEvent) event;

                if ( e.getAction() != Action.RIGHT_CLICK_BLOCK ) {
                    return false;
                }

                Block block = e.getClickedBlock();

                if ( block == null || block.getType() != Material.WORKBENCH ) {
                    return false;
                }

                Block upBlock = block.getLocation().add(0, 1, 0).getBlock();

                if ( upBlock == null || upBlock.getType() != Material.ANVIL ) {
                    return false;
                }

                Block downBlock = block.getLocation().add(0, 1, 0).getBlock();

                return downBlock != null && downBlock.getType() == Material.PISTON_BASE;

            }
        });
    }

    @Override
    public void onOpenMachineInfoViewer(PlayerData playerData) {

        new InfoWorldConstruct(playerData, new TalexItem(new ItemBuilder(Material.PISTON_BASE)

                .setName("§e压缩台")
                .setLore("", "§8> 其构造特别简单.", "", "§e你只需要在工作台上放一个铁砧,下面放一个普通活塞即可!", "")

                .toItemStack())).open();

    }

    @Override
    public void onOpenMachine(PlayerData playerData, PlayerEvent event) {

    }

    @Override
    public boolean onOpenRecipeView(GuiderBook guiderBook) {

        return false;
    }

    @Override
    public String onSave() {

        return "";
    }

    @Override
    public void onLoad(String str) {

    }

}
