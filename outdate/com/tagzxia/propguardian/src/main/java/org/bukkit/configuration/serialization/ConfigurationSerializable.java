package com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.serialization;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Represents an object that may be serialized.
 * <p>
 * These objects MUST implement one of the following, in addition to the
 * methods as defined by this interface:
 * <ul>
 * <li>A static method "deserialize" that accepts a single {@link Map}&lt;
 * {@link String}, {@link Object}&gt; and returns the app.</li>
 * <li>A static method "valueOf" that accepts a single {@link Map}&lt;{@link
 * String}, {@link Object}&gt; and returns the app.</li>
 * <li>A constructor that accepts a single {@link Map}&lt;{@link String},
 * {@link Object}&gt;.</li>
 * </ul>
 * In addition to implementing this interface, you must register the app
 * with {@link ConfigurationSerialization#registerClass(Class)}.
 *
 * @see DelegateDeserialization
 * @see SerializableAs
 */
public interface ConfigurationSerializable {

    /**
     * Creates a Map representation of this app.
     * <p>
     * This app must provide a method to restore this app, as defined in
     * the {@link ConfigurationSerializable} interface javadocs.
     *
     * @return Map containing the current state of this app
     */
    @NotNull Map<String, Object> serialize();
}
