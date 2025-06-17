package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.realm.cmd;

import com.talexs.talexessential.modules.realm.RealmModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RealmCmdCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if ( !(sender instanceof Player) ) return null;
        List<String> result = new ArrayList<>();

        Player player = (Player) sender;

        if (args != null) {

            if ( args.length == 1 ) {
                result.add("set");
                result.add("tp");
                result.add("list");
                result.add("flag");
            }

            if ( args.length == 2 && args[0].equalsIgnoreCase("flag") ) {
                String name = args[1];
                RealmModule.realms.forEach(realm -> {
                    if ( !player.hasPermission("talexessential.realm.set." + realm.getName()) /*|| Objects.equals(realm.getOwner(), player.getName())*/ ) return;

                    if ( name.isEmpty() || realm.getName().contains(name) || realm.getName().startsWith(name) || realm.getName().endsWith(name) )
                        result.add(realm.getName());

                });
            }

            if ( args.length == 2 && args[0].equalsIgnoreCase("set") ) {
                String name = args[1];
                RealmModule.realms.forEach(realm -> {
                    if ( !player.hasPermission("talexessential.realm.set." + realm.getName()) /*|| Objects.equals(realm.getOwner(), player.getName())*/ ) return;

                    if ( name.isEmpty() || realm.getName().contains(name) || realm.getName().startsWith(name) || realm.getName().endsWith(name) )
                        result.add(realm.getName());

                });
            }

            if ( args.length == 2 && args[0].equalsIgnoreCase("tp") ) {
                String name = args[1];
                RealmModule.realms.forEach(realm -> {
                    if ( realm.allowPlayerTeleport(player) )
                        if ( name.isEmpty() || realm.getName().contains(name) || realm.getName().startsWith(name) || realm.getName().endsWith(name) )
                            result.add(realm.getName());
                });
            }

        }

        return result;
    }
}
