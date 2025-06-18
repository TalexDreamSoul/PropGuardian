package com.tagzxia3.propguardian.src.main.java.org.bukkit.configuration.serialization;

import com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable;
import com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.DelegateDeserialization;
import com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.SerializableAs;
import org.apache.commons.lang3.Validate;
import org.bukkit.configuration.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility app for storing and retrieving classes for {@link Configuration}.
 */
public class ConfigurationSerialization {
    public static final String SERIALIZED_TYPE_KEY = "==";
    private static final Map<String, Class<? extends com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable>> aliases = new HashMap<String, Class<? extends com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable>>();
    private final Class<? extends com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable> clazz;

    protected ConfigurationSerialization(@NotNull Class<? extends com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable> clazz) {
        this.clazz = clazz;
    }

    /**
     * Attempts to deserialize the given arguments into a new instance of the
     * given app.
     * <p>
     * The app must implement {@link com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable}, including
     * the extra methods as specified in the javadoc of
     * ConfigurationSerializable.
     * <p>
     * If a new instance could not be made, an example being the app not
     * fully implementing the interface, null will be returned.
     *
     * @param args  Arguments for deserialization
     * @param clazz Class to deserialize into
     * @return New instance of the specified app
     */
    @Nullable
    public static com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable deserializeObject(@NotNull Map<String, ?> args, @NotNull Class<? extends com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable> clazz) {
        return new ConfigurationSerialization(clazz).deserialize(args);
    }

    /**
     * Attempts to deserialize the given arguments into a new instance of the
     * given app.
     * <p>
     * The app must implement {@link com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable}, including
     * the extra methods as specified in the javadoc of
     * ConfigurationSerializable.
     * <p>
     * If a new instance could not be made, an example being the app not
     * fully implementing the interface, null will be returned.
     *
     * @param args Arguments for deserialization
     * @return New instance of the specified app
     */
    @Nullable
    public static com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable deserializeObject(@NotNull Map<String, ?> args) {
        Class<? extends com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable> clazz = null;

        if (args.containsKey(SERIALIZED_TYPE_KEY)) {
            try {
                String alias = (String) args.get(SERIALIZED_TYPE_KEY);

                if (alias == null) {
                    throw new IllegalArgumentException("Cannot have null alias");
                }
                clazz = getClassByAlias(alias);
                if (clazz == null) {
                    throw new IllegalArgumentException("Specified app does not exist ('" + alias + "')");
                }
            } catch (ClassCastException ex) {
                ex.fillInStackTrace();
                throw ex;
            }
        } else {
            throw new IllegalArgumentException("Args doesn't contain type key ('" + SERIALIZED_TYPE_KEY + "')");
        }

        return new ConfigurationSerialization(clazz).deserialize(args);
    }

    /**
     * Registers the given {@link com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable} app by its
     * alias
     *
     * @param clazz Class to register
     */
    public static void registerClass(@NotNull Class<? extends com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable> clazz) {
        com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.DelegateDeserialization delegate = clazz.getAnnotation(com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.DelegateDeserialization.class);

        if (delegate == null) {
            registerClass(clazz, getAlias(clazz));
            registerClass(clazz, clazz.getName());
        }
    }

    /**
     * Registers the given alias to the specified {@link
     * com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable} app
     *
     * @param clazz Class to register
     * @param alias Alias to register as
     * @see com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.SerializableAs
     */
    public static void registerClass(@NotNull Class<? extends com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable> clazz, @NotNull String alias) {
        aliases.put(alias, clazz);
    }

    /**
     * Unregisters the specified alias to a {@link com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable}
     *
     * @param alias Alias to unregister
     */
    public static void unregisterClass(@NotNull String alias) {
        aliases.remove(alias);
    }

    /**
     * Unregisters any aliases for the specified {@link
     * com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable} app
     *
     * @param clazz Class to unregister
     */
    public static void unregisterClass(@NotNull Class<? extends com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable> clazz) {
        while (aliases.values().remove(clazz)) {
        }
    }

    /**
     * Attempts to get a registered {@link com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable} app by
     * its alias
     *
     * @param alias Alias of the serializable
     * @return Registered app, or null if not found
     */
    @Nullable
    public static Class<? extends com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable> getClassByAlias(@NotNull String alias) {
        return aliases.get(alias);
    }

    /**
     * Gets the correct alias for the given {@link com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable}
     * app
     *
     * @param clazz Class to get alias for
     * @return Alias to use for the app
     */
    @NotNull
    public static String getAlias(@NotNull Class<? extends com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable> clazz) {
        com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);

        if (delegate != null) {
            if ((delegate.value() == null) || (delegate.value() == clazz)) {
                delegate = null;
            } else {
                return getAlias(delegate.value());
            }
        }

        if (delegate == null) {
            com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.SerializableAs alias = clazz.getAnnotation(SerializableAs.class);

            if ((alias != null) && (alias.value() != null)) {
                return alias.value();
            }
        }

        return clazz.getName();
    }

    @Nullable
    protected Method getMethod(@NotNull String name, boolean isStatic) {
        try {
            Method method = clazz.getDeclaredMethod(name, Map.class);

            if (!com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable.class.isAssignableFrom(method.getReturnType())) {
                return null;
            }
            if (Modifier.isStatic(method.getModifiers()) != isStatic) {
                return null;
            }

            return method;
        } catch (NoSuchMethodException ex) {
            return null;
        } catch (SecurityException ex) {
            return null;
        }
    }

    @Nullable
    protected Constructor<? extends com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable> getConstructor() {
        try {
            return clazz.getConstructor(Map.class);
        } catch (NoSuchMethodException ex) {
            return null;
        } catch (SecurityException ex) {
            return null;
        }
    }

    @Nullable
    protected com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable deserializeViaMethod(@NotNull Method method, @NotNull Map<String, ?> args) {
        try {
            com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable result = (com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable) method.invoke(null, args);

            if (result == null) {
                Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call method '" + method.toString() + "' of " + clazz + " for deserialization: method returned null");
            } else {
                return result;
            }
        } catch (Throwable ex) {
            Logger.getLogger(ConfigurationSerialization.class.getName()).log(
                    Level.SEVERE,
                    "Could not call method '" + method.toString() + "' of " + clazz + " for deserialization",
                    ex instanceof InvocationTargetException ? ex.getCause() : ex);
        }

        return null;
    }

    @Nullable
    protected com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable deserializeViaCtor(@NotNull Constructor<? extends com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable> ctor, @NotNull Map<String, ?> args) {
        try {
            return ctor.newInstance(args);
        } catch (Throwable ex) {
            Logger.getLogger(ConfigurationSerialization.class.getName()).log(
                    Level.SEVERE,
                    "Could not call constructor '" + ctor.toString() + "' of " + clazz + " for deserialization",
                    ex instanceof InvocationTargetException ? ex.getCause() : ex);
        }

        return null;
    }

    @Nullable
    public com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable deserialize(@NotNull Map<String, ?> args) {
        Validate.notNull(args, "Args must not be null");

        com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable result = null;
        Method method = null;

        if (result == null) {
            method = getMethod("deserialize", true);

            if (method != null) {
                result = deserializeViaMethod(method, args);
            }
        }

        if (result == null) {
            method = getMethod("valueOf", true);

            if (method != null) {
                result = deserializeViaMethod(method, args);
            }
        }

        if (result == null) {
            Constructor<? extends ConfigurationSerializable> constructor = getConstructor();

            if (constructor != null) {
                result = deserializeViaCtor(constructor, args);
            }
        }

        return result;
    }
}
