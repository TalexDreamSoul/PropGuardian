package com.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public abstract class ChatFunction {

    private long timeOut = 15 * 1000;

    private final long created = System.currentTimeMillis();

    public ChatFunction(long timeOut) {
        this.timeOut = timeOut;
    }

    public ChatFunction() {
        this(15 * 1000);
    }

    // TODO: big range move -> auto cancel

    private ChatFuncStatus status = ChatFuncStatus.PENDING;

    public void onBefore() {}

    public abstract void execute(String msg);

    public abstract void rejected();

    // call on both before rejected & execute
    public void onBeforeCall() {}

    public Map<String, Object> dataMap = new HashMap<>();

    public static enum ChatFuncStatus {
        /**
         * The function is waiting player for input
         */
        PENDING(),

        /**
         * The function is auto rejected.
         */
        REJECT(),

        /**
         * The function is cancelled.
         */
        CANCEL()
    }

}
