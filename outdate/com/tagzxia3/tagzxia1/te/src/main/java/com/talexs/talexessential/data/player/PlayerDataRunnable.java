package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential.data.player;

import lombok.Getter;

public abstract class PlayerDataRunnable {

    @Getter
    private boolean cancelled;

    public abstract void run();

    public void cancel() {

        this.cancelled = true;

    }

}
