package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.utils.inventory.base;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.IInvView;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

@Getter
public class TalexInvHolder implements InventoryHolder {

    private final IInvView inv;

    public TalexInvHolder(IInvView inv) {
        this.inv = inv;
    }

    @Override
    public @NotNull Inventory getInventory() {
        InvUI nowPage = inv.getCurrentPage();

        return nowPage.getCachedInv();
    }
}
