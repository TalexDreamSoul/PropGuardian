package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.inventory.base;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.IInvView;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvView;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Inventory view options
 * <p>This class is used to define the options of an inventory view.</p>
 * @author TalexDreamSoul
 * @since 1.1.0
 * @version 1.1.0
 * @see InvView
 * @see InvUI
 * 2023/08/20 PM 07:06:23
 */
@Getter
@Setter
@Accessors( chain = true )
public class InvOptions {

    /**
     * Define an inventory view whether closed or not.
     * If the value is true, means this inventory view is closed.
     * If the value is false, means this inventory view is opened.
     */
    private boolean closed = false;

    /**
     * Define an inventory view whether allow close or not.
     * If the value is true, the player who opened this cannot
     * close this inventory anymore, and it will automatically
     * re-open this view (but InventoryCloseEvent still occurred)
     */
    private boolean allowClose = false;

    /**
     * Define an inventory view whether allow put item or not.
     * If the value is true, the player who opened this cannot
     * click its own inventory but can click the inventory this.
     */
    private boolean allowClickPlayerInv = true;

    /**
     * Define an inventory view whether allow put item or not.
     * If the value is true, the player who opened this can
     * put any items into this inventory view.
     */
    private boolean allowPutItems = false;

    /**
     * Define an inventory view whether auto refresh or not,
     * the value which is 0 means not auto refresh, otherwise
     * the value is the refresh interval. (unit: tick)
     */
    private int autoRefresh = 0;

    /**
     * Define an inventory view click interval.
     * If the value is 0, means no interval.
     * The value is the interval time. (unit: ms)
     */
    private long clickInterval = 100;

    /**
     * Define the interval warning actionbar message when player click too quick,
     * which means the click interval is less than the click interval defined.
     */
    private String intervalWarn = "§c§lPlease wait for a moment before clicking again!";

    private IInvView view;

    public InvOptions(IInvView view) {
        this.view = view;
    }

}
