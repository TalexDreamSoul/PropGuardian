package com.tagzxia3.te.src.main.java.com.talexs.soultech.machine.compressor;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.items.TalexItem;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.item.MineCraftItem;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author TalexDreamSoul
 */
public class CompressorRecipe extends RecipeObject {

    private final TalexItem[] required = new TalexItem[20];

    private final TalexItem[] loadRequired = new TalexItem[4];

    @Getter
    private TalexItem export;

    private int requiredPointer = 0, loadRequiredPointer = 0;

    @Getter
    private int amount = 1;

    @Getter
    private int processTime;

    @Setter
    @Getter
    @Accessors(chain = true)
    private int temperature = 0, pressure = 0;

    public CompressorRecipe(String recipeID, SoulTechItem displayItem) {

        super("compressor_recipe_" + recipeID, displayItem);

        displayItem.setRecipe(this);
        this.setExport(displayItem);

    }

    public CompressorRecipe(String recipeID, TalexItem displayItem) {

        super("compressor_recipe_" + recipeID, displayItem);
        this.setExport(displayItem);

    }

    public CompressorRecipe setProcessTime(int time) {

        this.processTime = time;

        return this;

    }

    public CompressorRecipe setAmount(int amo) {

        this.amount = amo;

        return this;

    }

    public Collection<TalexItem> getRequiredList() {

        return new ArrayList<>(Arrays.asList(this.required));

    }

    public Collection<TalexItem> getLoadRequiredList() {

        return new ArrayList<>(Arrays.asList(this.loadRequired));

    }

    public TalexItem getRecipeAsID(int slot) {

        return required[slot - 1];

    }

    public CompressorRecipe setExport(TalexItem item) {

        this.export = item;

        return this;

    }

    public CompressorRecipe addRequiredNull() {

        return addRequired((String) null);

    }

    public CompressorRecipe addRequired(Material material) {

        return addRequired(new MineCraftItem(material));

    }

    public CompressorRecipe addRequired(String soulTechItemID) {

        SoulTechItem item = SoulTechItem.get(soulTechItemID);

        return addRequired(item);

    }

    public CompressorRecipe addRequired(TalexItem item) {

        if ( requiredPointer < 0 ) {

            requiredPointer = 0;

        }

        if ( requiredPointer >= 20 ) {

            throw new IndexOutOfBoundsException("Required item is too much: " + requiredPointer);

        }

        required[requiredPointer] = item;

        requiredPointer++;

        return this;

    }

    public void delRequired(TalexItem item) {

        if ( requiredPointer > 20 ) {
            requiredPointer = 20;
        }

        if ( requiredPointer < 0 ) {
            throw new IndexOutOfBoundsException();
        }

        required[requiredPointer] = null;

        requiredPointer--;

    }

    public CompressorRecipe addLoadRequiredNull() {

        return addLoadRequired((String) null);

    }

    public CompressorRecipe addLoadRequired(Material material) {

        return addLoadRequired(new MineCraftItem(material));

    }

    public CompressorRecipe addLoadRequired(String soulTechItemID) {

        SoulTechItem item = SoulTechItem.get(soulTechItemID);

        return addLoadRequired(item);

    }

    public CompressorRecipe addLoadRequired(TalexItem item) {

        if ( loadRequiredPointer < 0 ) {

            loadRequiredPointer = 0;

        }

        if ( loadRequiredPointer >= 4 ) {

            throw new IndexOutOfBoundsException();

        }

        loadRequired[loadRequiredPointer] = item;

        loadRequiredPointer++;

        return this;

    }

    public void delLoadRequired(TalexItem item) {

        if ( loadRequiredPointer > 4 ) {
            loadRequiredPointer = 4;
        }

        if ( loadRequiredPointer < 0 ) {
            throw new IndexOutOfBoundsException();
        }

        loadRequired[loadRequiredPointer] = null;

        loadRequiredPointer--;

    }

}
