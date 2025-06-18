package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.machine.bsae;

import com.talexs.talexessential.utils.inventory.MenuBasic;

public abstract class MachineGUI extends MenuBasic {

    public MachineGUI(String title) {

        super("机器 > " + title, 6);

    }

    @Override
    public boolean allowPutItem(String inventorySymbol) {
        return true;
    }

}
