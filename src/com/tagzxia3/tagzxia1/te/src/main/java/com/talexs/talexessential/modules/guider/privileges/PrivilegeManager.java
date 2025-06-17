package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.guider.privileges;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.guider.privileges.IPrivilege;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PrivilegeManager {

    public static List<IPrivilege> list = new ArrayList<>();

    static {

        list.add(new Privilege() {
            @Override
            public String line(Player player, boolean plus, boolean pro, boolean admin) {
                if ( admin || pro ) return "&e&l超级鞘翅加速";
                if ( plus ) return "&e鞘翅加速";
                return "&7鞘翅滑翔";
            }

            @Override
            public String source(Player player) {
                return "&cPRO&7, &3PLUS";
            }
        });

        list.add(new Privilege() {
            @Override
            public String line(Player player, boolean plus, boolean pro, boolean admin) {
                if ( admin || pro ) return "&e&l传送无等待";
                if ( plus ) return "&e传送等待3S";
                return "&7传送等待5S";
            }

            @Override
            public String source(Player player) {
                return "&cPRO&7, &3PLUS";
            }
        });

        list.add(new Privilege() {
            @Override
            public String line(Player player, boolean plus, boolean pro, boolean admin) {
                if ( admin || pro ) return "&e&l死亡不掉落保护 &c&l1.5倍";
                if ( plus ) return "&e死亡不掉落保护 &c1.2倍";
                return "&7死亡不掉落保护 &c每日3次";
            }

            @Override
            public String source(Player player) {
                return "&cPRO&7, &3PLUS";
            }
        });

        list.add(new Privilege() {
            @Override
            public String line(Player player, boolean plus, boolean pro, boolean admin) {
                if ( admin || pro ) return "&e&l领域数量上限 &c&l20个";
                if ( plus ) return "&e领域数量上限 &c10个";
                return "&7领域数量上限 &c8个";
            }

            @Override
            public String source(Player player) {
                return "&cPRO&7, &3PLUS";
            }
        });

        list.add(new Privilege() {
            @Override
            public String line(Player player, boolean plus, boolean pro, boolean admin) {
                if ( admin || pro ) return "&e&l箱子商店限制 &c&l200个";
                if ( plus ) return "&e箱子商店限制 &c100个";
                return "&7箱子商店限制 &c50个";
            }

            @Override
            public String source(Player player) {
                return "&cPRO&7, &3PLUS";
            }
        });

        list.add(new Privilege() {
            @Override
            public String line(Player player, boolean plus, boolean pro, boolean admin) {
                if ( admin || pro ) return "&e&l随身工作台，末影箱";
                if ( plus ) return "&e&l随身工作台，末影箱";
                return "&7随身工作台(无)，末影箱(无)";
            }

            @Override
            public String source(Player player) {
                return "&cPRO&7, &3PLUS";
            }
        });

        list.add(new Privilege() {
            @Override
            public String line(Player player, boolean plus, boolean pro, boolean admin) {
                if ( admin ) return "&c专享称号 &7[&4ADMIN&7]";
                if ( pro ) return "&e&l专享称号 &7[&cPRO&7]";
                if ( plus ) return "&e专享称号 &7[&3PLUS&7]";
                return "&7专享称号 &7[&f玩家&7]";
            }

            @Override
            public String source(Player player) {
                return "&cPRO&7, &3PLUS";
            }
        });

        list.add(new Privilege() {
            @Override
            public String line(Player player, boolean plus, boolean pro, boolean admin) {
                if ( admin ) return "&e飞行能量 &e&l&n无限";
                if ( pro ) return "&e飞行能量 &e&l每月810,000";
                if ( plus ) return "&e飞行能量 &e每月270,000";
                return "&7飞行能量 &7自充";
            }

            @Override
            public String source(Player player) {
                return "&cPRO&7, &3PLUS";
            }
        });

        list.add(new Privilege() {
            @Override
            public String line(Player player, boolean plus, boolean pro, boolean admin) {
                if ( admin || plus || pro ) return "&e满人进服 &a✔";
                return "&7满人进服 &c✘";
            }

            @Override
            public String source(Player player) {
                return "&cPRO&7, &3PLUS";
            }
        });

        list.add(new Privilege() {
            @Override
            public String line(Player player, boolean plus, boolean pro, boolean admin) {
                if ( admin ) return "&e每月礼包 &a✔";
                if ( pro ) return "&e每月礼包 &a✔";
                if ( plus ) return "&e每月礼包 &a✔";
                return "&7每月礼包 &c✘";
            }

            @Override
            public String source(Player player) {
                return "&cPRO&7, &3PLUS";
            }
        });

        list.add(new Privilege() {
            @Override
            public String line(Player player, boolean plus, boolean pro, boolean admin) {
                if ( admin ) return "&e随机传送 &a上限10";
                if ( pro ) return "&e随机传送 &a上限8";
                if ( plus ) return "&e随机传送 &a上限5";
                return "&e随机传送 &a上限3";
            }

            @Override
            public String source(Player player) {
                return "&cPRO&7, &3PLUS";
            }
        });

        list.add(new Privilege() {
            @Override
            public String line(Player player, boolean plus, boolean pro, boolean admin) {
                if ( admin ) return "&e家庭上限 &a无限";
                if ( pro ) return "&e家庭上限 &a上限8";
                if ( plus ) return "&e家庭上限 &a上限5";
                return "&e家庭上限 &a上限3";
            }

            @Override
            public String source(Player player) {
                return "&cPRO&7, &3PLUS";
            }
        });

    }

    private abstract static class Privilege implements IPrivilege {};

}
