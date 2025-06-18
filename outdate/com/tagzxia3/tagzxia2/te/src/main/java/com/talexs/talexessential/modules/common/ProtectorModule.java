package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.common;//package com.talexs.talexessential.modules.common;
//
//import com.talexs.talexessential.modules.BaseModule;
//import org.bukkit.World;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.player.PlayerDropItemEvent;
//
//public class ProtectorModule extends BaseModule {
//    public ProtectorModule() {
//        super("protector");
//    }
//
////    @EventHandler
////    public void onDrop(PlayerDropItemEvent event) {
////        Player player = event.getPlayer();
////        if ( player.hasPermission("tea.protector.drop") ) return;
////
////        World world = player.getWorld();
////
////        if ( yaml.getStringList("Protectors").stream().anyMatch(str -> world.getName().equalsIgnoreCase(str)) ) {
////            event.setCancelled(true);
////        }
////
////    }
//
//}
