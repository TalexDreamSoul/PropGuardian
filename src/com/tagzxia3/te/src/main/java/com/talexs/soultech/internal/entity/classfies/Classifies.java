package com.tagzxia3.te.src.main.java.com.talexs.soultech.internal.entity.classfies;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.classfies.ClassifiesAddon;
import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.entity.preposition.IPreposition;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
@Accessors(chain = true)
public class Classifies {

    @Setter
    private int priority = 0;

    private final String ID, name;

    private ItemStack displayStack;

    private final Set<Classifies> children = new HashSet<>();

    @Setter
    private Classifies fatherCategory;

    private Set<IPreposition> preposition = new HashSet<>();

    private Set<IUnlockCondition> unlockConditions = new HashSet<>();

    public Classifies(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public Classifies(String ID, String name, ItemStack displayStack) {
        this.ID = ID;
        this.name = name;
        this.displayStack = displayStack;
    }

    public Classifies addUnlockCondition(IUnlockCondition condition) {

        unlockConditions.add(condition);

        return this;
    }

    public List<Classifies> getChildren() {

        List<Classifies> list = new ArrayList<>(children);

        list.sort((o1, o2) -> {

            if ( o1.getPriority() == o2.getPriority() ) {

                return o1.getID().compareTo(o2.getID());

            }

            return Integer.compare(o1.getPriority(), o2.getPriority());

        });

        return list;

    }

    public Classifies addPreposition(IPreposition preposition) {

        this.preposition.add(preposition);

        return this;

    }

    public Classifies delPreposition(IPreposition preposition) {

        if ( preposition != null ) {
            this.preposition.remove(preposition);
        }

        return this;

    }

    public Classifies addChild(/*Class<? extends BaseClassifiesCreator> clz, */Classifies classifies) {

        classifies.setFatherCategory(this);

        children.add(classifies);

//        ClassifiesAddon classifiesAddon = AddonHolder.getINSTANCE().get(ClassifiesAddon.class);
//        classifiesAddon.addClassifies(clz, classifies);

        return this;

    }

    public Classifies delChild(Classifies categoryObject) {

        ClassifiesAddon classifiesAddon = AddonHolder.getINSTANCE().get(ClassifiesAddon.class);

        categoryObject.setFatherCategory(classifiesAddon.getRoot());
        children.remove(categoryObject);

        return this;

    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Classifies target) {

            return Objects.equals(target.getID(), getID());

        }

        return false;

    }

}
