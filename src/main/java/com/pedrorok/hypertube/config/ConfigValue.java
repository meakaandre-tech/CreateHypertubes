package com.pedrorok.hypertube.config;

/**
 * A single config entry. Stands in for NeoForge's ModConfigSpec values, keeping the same get() call.
 */
public final class ConfigValue<T> {
    private T value;

    public ConfigValue(T defaultValue) {
        this.value = defaultValue;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
