package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.internal.entity.tech_object;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.tech_object.TechObject;
import com.talexs.soultech.addon.BaseAddon;
import com.talexs.soultech.internal.entity.items.IPersistItem;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TechObjectAddon extends BaseAddon {

    private final Map<String, com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.tech_object.TechObject> techObjectHashMap = new HashMap<>(256);

    public Set<com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.tech_object.TechObject> getTechObject() {
        return new HashSet<>(techObjectHashMap.values());
    }

    public com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.tech_object.TechObject getTechObject(String id) {
        return techObjectHashMap.get(id);
    }

    public void addTechObject(TechObject techObject) {

        techObjectHashMap.put(techObject.getID(), techObject);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @SneakyThrows
    public YamlConfiguration getPersistItemSavedData(IPersistItem item) {
        File file = new File(plugin.getDataFolder(), "soultech/items/" + item.getID() + ".yml");
        YamlConfiguration yaml = new YamlConfiguration();
        if ( !file.exists() ) return yaml;

        yaml.load(file);

        return yaml;
    }

    @Override
    public void onDisable() {

        SoulTechItem.getItems().values().forEach(soulTechItem -> {
            if (soulTechItem instanceof IPersistItem ipi) {
                YamlConfiguration yamlConfiguration = ipi.onItemBlocksSave();

                try {
                    yamlConfiguration.save(new File(plugin.getDataFolder(), "soultech/items/" + ipi.getID() + ".yml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}
