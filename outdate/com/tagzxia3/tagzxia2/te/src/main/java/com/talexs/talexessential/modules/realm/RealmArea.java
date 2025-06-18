package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.realm;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.utils.NBTsUtil;
import lombok.Getter;
import org.bukkit.Location;

@Getter
public class RealmArea {

    private Location loc1, loc2;

    private PlayerRealm pr;

    private BoundingSphere sphere;

    public RealmArea(PlayerRealm pr, Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;

        this.pr = pr;

        pr.setRealmArea(this);

        this.sphere = BoundingSphere.getBoundingSphere(loc1, loc2);

        save();
    }

    public RealmArea(PlayerRealm pr) {
        this.pr = pr;

        load();

        pr.setRealmArea(this);
        save();
    }

    public boolean isInArea(Location loc) {
        if ( loc.getWorld() != loc1.getWorld() ) return false;

        int x1 = loc1.getBlockX();
        int y1 = loc1.getBlockY();
        int z1 = loc1.getBlockZ();

        int x2 = loc2.getBlockX();
        int y2 = loc2.getBlockY();
        int z2 = loc2.getBlockZ();

        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);

        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

//        System.out.println("x: " + x + " y: " + y + " z: " + z);
//        System.out.println("minX: " + minX + " minY: " + minY + " minZ: " + minZ);
//        System.out.println("maxX: " + maxX + " maxY: " + maxY + " maxZ: " + maxZ);

        if ( x > maxX || x < minX ) return false;
        if ( y > maxY || y < minY ) return false;
        if ( z > maxZ || z < minZ ) return false;

        return true;

//        return loc.getBlockX() >= minX && loc.getBlockX() <= maxX && loc.getBlockY() >= minY && loc.getBlockY() <= maxY && loc.getBlockZ() >= minZ && loc.getBlockZ() <= maxZ;
    }

    private void load() {
        this.loc1 = NBTsUtil.String2Location(this.pr.getInfo().getString("Area.Loc1"));
        this.loc2 = NBTsUtil.String2Location(this.pr.getInfo().getString("Area.Loc2"));

        this.sphere = !this.pr.getInfo().contains("BoundingSphere.center") ? BoundingSphere.getBoundingSphere(this.loc1, this.loc2) : new BoundingSphere(NBTsUtil.String2Location(this.pr.getInfo().getString("BoundingSphere.center")), this.pr.getInfo().getDouble("BoundingSphere.radius"));
    }

    public void save() {

        this.pr.getInfo().set("Area.Loc1", NBTsUtil.Location2String(this.loc1));
        this.pr.getInfo().set("Area.Loc2", NBTsUtil.Location2String(this.loc2));

        this.pr.getInfo().set("BoundingSphere.center", NBTsUtil.Location2String(this.sphere.center));
        this.pr.getInfo().set("BoundingSphere.radius", this.sphere.radius);
    }

    public static double calcLocSize(Location loc1, Location loc2) {
        int x1 = loc1.getBlockX();
        int y1 = loc1.getBlockY();
        int z1 = loc1.getBlockZ();

        int x2 = loc2.getBlockX();
        int y2 = loc2.getBlockY();
        int z2 = loc2.getBlockZ();

        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);

        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);

        int x = maxX - minX;
        int y = maxY - minY;
        int z = maxZ - minZ;

        return x * y * z;
    }

    public double calcSize() {
        return calcLocSize(this.loc1, this.loc2);
    }

    public static class BoundingSphere {
        private Location center;
        private double radius;

        public BoundingSphere(Location center, double radius) {
            this.center = center;
            this.radius = radius;
        }

        public boolean isInSphere(Location loc) {
            return loc.distance(center) <= radius;
        }

        public static BoundingSphere getBoundingSphere(Location loc1, Location loc2) {
            int x1 = loc1.getBlockX();
            int y1 = loc1.getBlockY();
            int z1 = loc1.getBlockZ();

            int x2 = loc2.getBlockX();
            int y2 = loc2.getBlockY();
            int z2 = loc2.getBlockZ();

            int minX = Math.min(x1, x2);
            int minY = Math.min(y1, y2);
            int minZ = Math.min(z1, z2);

            int maxX = Math.max(x1, x2);
            int maxY = Math.max(y1, y2);
            int maxZ = Math.max(z1, z2);

            double x = maxX - minX;
            double y = maxY - minY;
            double z = maxZ - minZ;

            double radius = Math.sqrt(Math.pow(x / 2, 2) + Math.pow(y / 2, 2) + Math.pow(z / 2, 2)) / 2;
//            double radius = Math.sqrt(x * x + y * y + z * z) / 2;

            Location center = new Location(loc1.getWorld(), minX + x / 2, minY + y / 2, minZ + z / 2);

            return new BoundingSphere(center, radius);
        }

        public static boolean doSphereIntersect(BoundingSphere sphere1, BoundingSphere sphere2) {
            if ( sphere1.center.getWorld() != sphere2.center.getWorld() ) return true;

            return sphere1.center.distance(sphere2.center) - sphere1.radius - sphere2.radius >= 8;
        }
    }

}
