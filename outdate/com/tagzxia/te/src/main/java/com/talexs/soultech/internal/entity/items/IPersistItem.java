package com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.items;

import org.bukkit.configuration.file.YamlConfiguration;

public interface IPersistItem {

    YamlConfiguration onItemBlocksSave();

    void onItemBlocksLoad(YamlConfiguration yaml);

    String getID();

}
