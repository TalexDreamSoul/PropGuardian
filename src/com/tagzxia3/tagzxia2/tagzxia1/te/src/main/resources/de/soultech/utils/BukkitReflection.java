package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.resources.de.soultech.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BukkitReflection {

    private static final String CRAFT_BUKKIT_PACKAGE;
    private static final String NET_MINECRAFT_SERVER_PACKAGE;
    private static final Class CRAFT_SERVER_CLASS;
    private static final Method CRAFT_SERVER_GET_HANDLE_METHOD;
    private static final Class PLAYER_LIST_CLASS;
    private static final Field PLAYER_LIST_MAX_PLAYERS_FIELD;
    private static final Class CRAFT_PLAYER_CLASS;
    private static final Method CRAFT_PLAYER_GET_HANDLE_METHOD;
    private static final Class ENTITY_PLAYER_CLASS;
    private static final Field ENTITY_PLAYER_PING_FIELD;
    private static final Class CRAFT_ITEM_STACK_CLASS;
    private static final Method CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD;

    static {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            CRAFT_BUKKIT_PACKAGE = "org.bukkit.craftbukkit." + version + ".";
            NET_MINECRAFT_SERVER_PACKAGE = "net.minecraft.server." + version + ".";
            CRAFT_SERVER_CLASS = Class.forName(CRAFT_BUKKIT_PACKAGE + "CraftServer");
            CRAFT_SERVER_GET_HANDLE_METHOD = CRAFT_SERVER_CLASS.getDeclaredMethod("getHandle");
            CRAFT_SERVER_GET_HANDLE_METHOD.setAccessible(true);
            PLAYER_LIST_CLASS = Class.forName(NET_MINECRAFT_SERVER_PACKAGE + "PlayerList");
            PLAYER_LIST_MAX_PLAYERS_FIELD = PLAYER_LIST_CLASS.getDeclaredField("maxPlayers");
            PLAYER_LIST_MAX_PLAYERS_FIELD.setAccessible(true);
            CRAFT_PLAYER_CLASS = Class.forName(CRAFT_BUKKIT_PACKAGE + "entity.CraftPlayer");
            CRAFT_PLAYER_GET_HANDLE_METHOD = CRAFT_PLAYER_CLASS.getDeclaredMethod("getHandle");
            CRAFT_PLAYER_GET_HANDLE_METHOD.setAccessible(true);
            ENTITY_PLAYER_CLASS = Class.forName(NET_MINECRAFT_SERVER_PACKAGE + "EntityPlayer");
            ENTITY_PLAYER_PING_FIELD = ENTITY_PLAYER_CLASS.getDeclaredField("ping");
            ENTITY_PLAYER_PING_FIELD.setAccessible(true);
            CRAFT_ITEM_STACK_CLASS = Class.forName(CRAFT_BUKKIT_PACKAGE + "inventory.CraftItemStack");
            CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD = CRAFT_ITEM_STACK_CLASS.getDeclaredMethod("asNMSCopy", ItemStack.class);
            CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD.setAccessible(true);
        } catch ( Exception var1 ) {
            var1.printStackTrace();
            throw new RuntimeException("Failed to initialize Bukkit/NMS Reflection");
        }
    }

    public static void sendLightning(Player p, Location l) {

        Class light = getNMSClass("EntityLightning");

        try {
            assert light != null;
            Constructor<?> constu = light.getConstructor(getNMSClass("World"), Double.TYPE, Double.TYPE, Double.TYPE, Boolean.TYPE, Boolean.TYPE);
            Object wh = p.getWorld().getClass().getMethod("getHandle").invoke(p.getWorld());
            Object lighobj = constu.newInstance(wh, l.getX(), l.getY(), l.getZ(), true, true);
            Object obj = getNMSClass("PacketPlayOutSpawnEntityWeather").getConstructor(getNMSClass("Entity")).newInstance(lighobj);
            sendPacket(p, obj);
            p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 100.0F, 1.0F);
        } catch ( SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchMethodException var7 ) {
            var7.printStackTrace();
        }

    }

    public static Class<?> getNMSClass(String name) {

        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch ( ClassNotFoundException var3 ) {
            var3.printStackTrace();
            return null;
        }
    }

    public static void sendPacket(Player player, Object packet) {

        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch ( Exception var4 ) {
            var4.printStackTrace();
        }

    }

    public static int getPing(Player player) {

        try {
            int ping = ENTITY_PLAYER_PING_FIELD.getInt(CRAFT_PLAYER_GET_HANDLE_METHOD.invoke(player));
            return ping > 0 ? ping : 0;
        } catch ( Exception var2 ) {
            return 1;
        }
    }

    public static void setMaxPlayers(Server server, int slots) {

        try {
            PLAYER_LIST_MAX_PLAYERS_FIELD.set(CRAFT_SERVER_GET_HANDLE_METHOD.invoke(server), slots);
        } catch ( Exception var3 ) {
            var3.printStackTrace();
        }

    }

    public static String getItemStackName(ItemStack itemStack) {

        try {
            return (String) CRAFT_ITEM_STACK_AS_NMS_COPY_METHOD.invoke(itemStack, itemStack);
        } catch ( Exception var2 ) {
            var2.printStackTrace();
            return "";
        }
    }

    public static Class<?> getClass(String name) {

        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    public static Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... parameterTypes)
            throws NoSuchMethodException {

        final Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        for ( final Constructor<?> constructor : clazz.getConstructors() ) {
            if ( DataType.compare(DataType.getPrimitive(constructor.getParameterTypes()), primitiveTypes) ) {
                return constructor;
            }
        }
        throw new NoSuchMethodException(
                "There is no such constructor in this class with the specified parameter types");
    }

    public static Constructor<?> getConstructor(final String className, final PackageType packageType, final Class<?>... parameterTypes)
            throws NoSuchMethodException, ClassNotFoundException {

        return getConstructor(packageType.getClass(className), parameterTypes);
    }

    public static Object instantiateObject(final Class<?> clazz, final Object... arguments) throws InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

        return getConstructor(clazz, DataType.getPrimitive(arguments)).newInstance(arguments);
    }

    public static Object instantiateObject(final String className, final PackageType packageType, final Object... arguments)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, ClassNotFoundException {

        return instantiateObject(packageType.getClass(className), arguments);
    }

    public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes)
            throws NoSuchMethodException {

        final Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        for ( final Method method : clazz.getMethods() ) {
            if ( method.getName().equals(methodName)
                    && DataType.compare(DataType.getPrimitive(method.getParameterTypes()), primitiveTypes) ) {
                return method;
            }
        }
        throw new NoSuchMethodException(
                "There is no such method in this class with the specified name and parameter types");
    }

    public static Method getMethod(final String className, final PackageType packageType, final String methodName,
                                   final Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {

        return getMethod(packageType.getClass(className), methodName, parameterTypes);
    }

    public static Object invokeMethod(final Object instance, final String methodName, final Object... arguments)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

        return getMethod(instance.getClass(), methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
    }

    public static Object invokeMethod(final Object instance, final Class<?> clazz, final String methodName, final Object... arguments)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {

        return getMethod(clazz, methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
    }

    public static Object invokeMethod(final Object instance, final String className, final PackageType packageType, final String methodName,
                                      final Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, ClassNotFoundException {

        return invokeMethod(instance, packageType.getClass(className), methodName, arguments);
    }

    public static Field getField(final Class<?> clazz, final boolean declared, final String fieldName)
            throws NoSuchFieldException, SecurityException {

        final Field field = declared ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static Field getField(final String className, final PackageType packageType, final boolean declared, final String fieldName)
            throws NoSuchFieldException, SecurityException, ClassNotFoundException {

        return getField(packageType.getClass(className), declared, fieldName);
    }

    public static Object getValue(final Object instance, final Class<?> clazz, final boolean declared, final String fieldName)
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

        return getField(clazz, declared, fieldName).get(instance);
    }

    public static Object getValue(final Object instance, final String className, final PackageType packageType, final boolean declared,
                                  final String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
            SecurityException, ClassNotFoundException {

        return getValue(instance, packageType.getClass(className), declared, fieldName);
    }

    public static Object getValue(final Object instance, final boolean declared, final String fieldName)
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

        return getValue(instance, instance.getClass(), declared, fieldName);
    }

    public static void setValue(final Object instance, final Class<?> clazz, final boolean declared, final String fieldName, final Object value)
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

        getField(clazz, declared, fieldName).set(instance, value);
    }

    public static void setValue(final Object instance, final String className, final PackageType packageType, final boolean declared,
                                final String fieldName, final Object value) throws IllegalArgumentException, IllegalAccessException,
            NoSuchFieldException, SecurityException, ClassNotFoundException {

        setValue(instance, packageType.getClass(className), declared, fieldName, value);
    }

    public static void setValue(final Object instance, final boolean declared, final String fieldName, final Object value)
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

        setValue(instance, instance.getClass(), declared, fieldName, value);
    }

    public static enum PackageType {
        MINECRAFT_SERVER("net.minecraft.server." + getServerVersion()), CRAFTBUKKIT("org.bukkit.craftbukkit."
                + getServerVersion()), CRAFTBUKKIT_BLOCK(CRAFTBUKKIT, "block"), CRAFTBUKKIT_CHUNKIO(CRAFTBUKKIT,
                "chunkio"), CRAFTBUKKIT_COMMAND(CRAFTBUKKIT, "command"), CRAFTBUKKIT_CONVERSATIONS(CRAFTBUKKIT,
                "conversations"), CRAFTBUKKIT_ENCHANTMENS(CRAFTBUKKIT,
                "enchantments"), CRAFTBUKKIT_ENTITY(CRAFTBUKKIT, "entity"), CRAFTBUKKIT_EVENT(
                CRAFTBUKKIT, "event"), CRAFTBUKKIT_GENERATOR(CRAFTBUKKIT,
                "generator"), CRAFTBUKKIT_HELP(CRAFTBUKKIT,
                "help"), CRAFTBUKKIT_INVENTORY(CRAFTBUKKIT,
                "inventory"), CRAFTBUKKIT_MAP(CRAFTBUKKIT,
                "map"), CRAFTBUKKIT_METADATA(
                CRAFTBUKKIT,
                "metadata"), CRAFTBUKKIT_POTION(
                CRAFTBUKKIT,
                "potion"), CRAFTBUKKIT_PROJECTILES(
                CRAFTBUKKIT,
                "projectiles"), CRAFTBUKKIT_SCHEDULER(
                CRAFTBUKKIT,
                "scheduler"), CRAFTBUKKIT_SCOREBOARD(
                CRAFTBUKKIT,
                "scoreboard"), CRAFTBUKKIT_UPDATER(
                CRAFTBUKKIT,
                "updater"), CRAFTBUKKIT_UTIL(
                CRAFTBUKKIT,
                "util");

        private final String path;

        private PackageType(final String path) {

            this.path = path;
        }

        private PackageType(final PackageType parent, final String path) {

            this(parent + "." + path);
        }

        public static String getServerVersion() {

            return Bukkit.getServer().getClass().getPackage().getName().substring(23);
        }

        public String getPath() {

            return path;
        }

        public Class<?> getClass(final String className) throws ClassNotFoundException {

            return Class.forName(this + "." + className);
        }

        @Override
        public String toString() {

            return path;
        }
    }

    public static enum DataType {
        BYTE(Byte.TYPE, Byte.class), SHORT(Short.TYPE, Short.class), INTEGER(Integer.TYPE, Integer.class), LONG(
                Long.TYPE, Long.class), CHARACTER(Character.TYPE, Character.class), FLOAT(Float.TYPE,
                Float.class), DOUBLE(Double.TYPE, Double.class), BOOLEAN(Boolean.TYPE, Boolean.class);

        private static final Map<Class<?>, DataType> CLASS_MAP;

        static {
            CLASS_MAP = new HashMap<>();
            for ( final DataType type : values() ) {
                CLASS_MAP.put(type.primitive, type);
                CLASS_MAP.put(type.reference, type);
            }
        }

        private final Class<?> primitive;
        private final Class<?> reference;

        private DataType(final Class<?> primitive, final Class<?> reference) {

            this.primitive = primitive;
            this.reference = reference;
        }

        public static DataType fromClass(final Class<?> clazz) {

            return CLASS_MAP.get(clazz);
        }

        public static Class<?> getPrimitive(final Class<?> clazz) {

            final DataType type = fromClass(clazz);
            return type == null ? clazz : type.getPrimitive();
        }

        public static Class<?> getReference(final Class<?> clazz) {

            final DataType type = fromClass(clazz);
            return type == null ? clazz : type.getReference();
        }

        public static Class<?>[] getPrimitive(final Class<?>[] classes) {

            final int length = classes == null ? 0 : classes.length;
            final Class<?>[] types = new Class[length];
            for ( int index = 0; index < length; index++ ) {
                types[index] = getPrimitive(classes[index]);
            }
            return types;
        }

        public static Class<?>[] getReference(final Class<?>[] classes) {

            final int length = classes == null ? 0 : classes.length;
            final Class<?>[] types = new Class[length];
            for ( int index = 0; index < length; index++ ) {
                types[index] = getReference(classes[index]);
            }
            return types;
        }

        public static Class<?>[] getPrimitive(final Object[] objects) {

            final int length = objects == null ? 0 : objects.length;
            final Class<?>[] types = new Class[length];
            for ( int index = 0; index < length; index++ ) {
                types[index] = getPrimitive(objects[index].getClass());
            }
            return types;
        }

        public static Class<?>[] getReference(final Object[] objects) {

            final int length = objects == null ? 0 : objects.length;
            final Class<?>[] types = new Class[length];
            for ( int index = 0; index < length; index++ ) {
                types[index] = getReference(objects[index].getClass());
            }
            return types;
        }

        public static boolean compare(final Class<?>[] primary, final Class<?>[] secondary) {

            if ( primary == null || secondary == null || primary.length != secondary.length ) {
                return false;
            }
            for ( int index = 0; index < primary.length; index++ ) {
                final Class<?> primaryClass = primary[index];
                final Class<?> secondaryClass = secondary[index];
                if ( !primaryClass.equals(secondaryClass) && !primaryClass.isAssignableFrom(secondaryClass) ) {
                    return false;
                }
            }
            return true;
        }

        public Class<?> getPrimitive() {

            return primitive;
        }

        public Class<?> getReference() {

            return reference;
        }
    }
}
