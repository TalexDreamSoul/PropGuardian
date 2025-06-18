package com.tagzxia3.te.src.main.java.com.talexs.talexessential.utils.inventory.base;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.IInvView;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvOptions;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI;
import com.talexs.talexessential.utils.inventory.item.IInvClickableItem;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Setter
@Accessors(chain = true)
public abstract class InvView implements IInvView {

    private final List<com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI> uis = new ArrayList<>();

    private final String defaultTitle;

    private final int rows;

    private int nowPage, offset = 0;

    private final InvOptions invOptions = new InvOptions(this);

    @Override
    public List<com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI> getUis() {
        return uis;
    }

    @Override
    public String getDefaultTitle() {
        return defaultTitle;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getNowPage() {
        return nowPage;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public InvOptions getInvOptions() {
        return invOptions;
    }

    public InvView(String defaultTitle, int rows) {
        this.defaultTitle = defaultTitle;
        this.rows = rows;
    }

    @Override
    public @Nullable com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI getPage(int page) {
        this.checkInvUis();

        if ( page < 0 || page > uis.size() ) return null;

        return uis.get(page);
    }

    @Override
    public @Nullable com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI getCurrentPage() {
        return getPage(nowPage);
    }

    @Override
    public int getViewSize() {
        return this.rows * 9;
    }

    private void checkInvUis() {
        if (this.uis.isEmpty()) {
            this.createNewInvUI();
        }
    }

    @Override
    public @Nullable IInvClickableItem getItem(int page, int slot) {
        this.checkInvUis();

        com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI currentPage = getPage(page);

        if ( currentPage != null ) {
            return currentPage.getItem(slot);
        }

        return null;
    }

    @Override
    public @Nullable IInvClickableItem getItem(int slot) {
        return getItem(nowPage, slot);
    }

    @Override
    public IInvView setItem(int x, int y, IInvClickableItem item) {
        this.checkInvUis();

        com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI currentPage = getCurrentPage();

        if ( currentPage != null ) {
            currentPage.setItem(x - 1, y - 1, item);
        }

        return this;
    }

    @Override
    public IInvView setItem(int slot, IInvClickableItem item) {
        this.checkInvUis();

        com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI currentPage = getCurrentPage();

        if ( currentPage != null ) {
            currentPage.setItem(slot, item);
        }

        return this;
    }

    @Override
    public IInvView addItem(IInvClickableItem item) {
        this.checkInvUis();

        com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI lastInventory = uis.get(this.uis.size() - 1);

        if ( lastInventory.getCurY() == this.rows - 1 && lastInventory.getCurX() >= 7 - this.offset ) {

            this.createNewInvUI();
            this.addItem(item);

        } else {

            lastInventory.setItem(lastInventory.setCurX(lastInventory.getCurX() + 1).getCurX() + this.offset, lastInventory.getCurY(), item);

        }

        if ( lastInventory.getCurX() >= 8 - this.offset ) {
            lastInventory.setCurX(this.offset - 1);
            lastInventory.setCurY(lastInventory.getCurY() + 1);
        }

        return this;
    }

    @Override
    public IInvView removeItem(int slot) {
        this.checkInvUis();

        com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI lastInventory = uis.get(this.uis.size() - 1);

        this.setItem(slot, null);

        for ( int i = slot + 1; i < this.getViewSize(); i++ ) {
            IInvClickableItem item = this.getItem(i);

            this.setItem(i - 1, item);
            this.setItem(i, null);
        }

        if ( lastInventory.getCurX() >= 0 ) {
            lastInventory.setCurX(lastInventory.getCurX() - 1);
        } else {
            if ( lastInventory.getCurY() > 0 ) {
                lastInventory.setCurY(lastInventory.getCurY() - 1);
                lastInventory.setCurX(7);
            }
        }

        return this;
    }

    public IInvView createNewInvUI() {
        com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI ui = new com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI(this, this.defaultTitle, this.rows);

        onNewInv(ui);

        this.uis.add(ui);

        return this;
    }

    /**
     * Call on before invUi add to list.
     * @param ui The inventory to be added
     */
    @Override
    public void onNewInv(com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI ui) {

    }

    @Override
    public boolean onCloseInventory(InventoryCloseEvent event) {
        com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI ui = getCurrentPage();

        return onClose(ui, event);
    }

    /**
     * Define an inventory that will be closed
     * @param ui The inventory to be closed
     * @param event The event that triggered the closure
     * @return Whether to close the inventory (true for deny)
     */
    @Override
    public boolean onClose(InvUI ui, InventoryCloseEvent event) {
        return true;
    }

    /**
     * Define an inventory that will be clicked
     * @param event The event that triggered the click
     * @return Whether to close the inventory (true for cancelled)
     */
    @Override
    public boolean onClick(InventoryClickEvent event) {
        return event.isCancelled();
    }

    @Override
    public void refresh() {
        // TODO: refresh logic
        onRefresh();
    }

    @Override
    public void onRefresh() {}

}
