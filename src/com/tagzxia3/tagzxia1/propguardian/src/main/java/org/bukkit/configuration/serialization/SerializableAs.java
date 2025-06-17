package com.tagzxia3.tagzxia1.propguardian.src.main.java.org.bukkit.configuration.serialization;

import com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable;
import com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an "alias" that a {@link ConfigurationSerializable} may be
 * stored as.
 * If this is not present on a {@link ConfigurationSerializable} app, it
 * will use the fully qualified name of the app.
 * <p>
 * This value will be stored in the configuration so that the configuration
 * deserialization can determine what type it is.
 * <p>
 * Using this annotation on any other app than a {@link
 * ConfigurationSerializable} will have no effect.
 *
 * @see ConfigurationSerialization#registerClass(Class, String)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SerializableAs {
    /**
     * This is the name your app will be stored and retrieved as.
     * <p>
     * This name MUST be unique. We recommend using names such as
     * "MyPluginThing" instead of "Thing".
     *
     * @return Name to serialize the app as.
     */
    @NotNull String value();
}
