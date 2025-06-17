package com.tagzxia3.te.src.main.java.com.talexs.talexessential.modules.souvenir;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.souvenir.SouvenirType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Material;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
//@AllArgsConstructor
public class Souvenir {

    private String key;

    private SouvenirType type;

    private String title;

    private List<String> desc;

    private String headId;

    private Material material;

}
