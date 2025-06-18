package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.spec;

//import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.spec.ResSpecMoney;
import com.talexs.talexessential.modules.realm.PlayerRealm;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.GameMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Accessors(chain = true)
public class ResSpec {

    private PlayerRealm res;

    private final String resName;

    private final String owner;

    private final UUID ownerUniqueId;

    private ResSpecMoney money;

    private GameMode gameMode = GameMode.ADVENTURE;

    private double earnMoney;

    private List<String> logs = new ArrayList<>(), desc = new ArrayList<>();

    public ResSpec(PlayerRealm res, String resName, String owner, UUID ownerUniqueId, ResSpecMoney money) {
        this.res = res;
        this.resName = resName;
        this.owner = owner;
        this.ownerUniqueId = ownerUniqueId;
        this.money = money;
    }

}
