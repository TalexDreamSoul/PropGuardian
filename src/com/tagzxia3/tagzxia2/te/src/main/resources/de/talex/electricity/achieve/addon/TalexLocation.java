package com.tagzxia3.tagzxia2.te.src.main.resources.de.talex.electricity.achieve.addon;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Location;
import pubsher.talexsoultech.talex.electricity.achieve.IPather;

import java.util.ArrayDeque;
import java.util.Queue;

@Getter
@Setter
public class TalexLocation implements Cloneable, IPather {

    private TalexLocation father;
    private Location loc;

    private Queue<TalexLocation> routerLocs = new ArrayDeque<>();

    public TalexLocation(TalexLocation father, Location loc) {

        this.father = father;
        this.loc = loc;

    }

    @Override
    @SneakyThrows
    public TalexLocation clone() {

        return (TalexLocation) super.clone();

    }

    public TalexLocation add(double x, double y, double z) {

        this.loc = this.loc.add(x, y, z);

        return this;

    }

    @Override
    public int hashCode() {

        return loc.hashCode();

    }

    @Override
    public boolean equals(Object obj) {

        if ( obj instanceof TalexLocation ) {

            TalexLocation target = (TalexLocation) obj;

            if ( father != null ) {

                if ( target.father == null ) {
                    return false;
                }

                return target.father.loc.equals(father.loc) && target.loc.equals(loc);

            }

            return target.loc.equals(loc);

        }

        return false;

    }

}
