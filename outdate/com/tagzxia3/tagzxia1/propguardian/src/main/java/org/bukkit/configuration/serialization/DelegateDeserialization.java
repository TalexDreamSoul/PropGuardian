package com.tagzxia3.tagzxia1.propguardian.src.main.java.org.bukkit.configuration.serialization;

import com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Applies to a {@link ConfigurationSerializable} that will delegate all
 * deserialization to another {@link ConfigurationSerializable}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DelegateDeserialization {
    /**
     * Which app should be used as a delegate for this classes
     * deserialization
     *
     * @return Delegate app
     */
    @NotNull Class<? extends ConfigurationSerializable> value();
}
