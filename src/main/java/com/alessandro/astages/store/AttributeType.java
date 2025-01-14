package com.alessandro.astages.store;

import com.google.common.reflect.TypeToken;
import net.minecraft.MethodsReturnNonnullByDefault;
import org.jetbrains.annotations.Contract;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AttributeType<T> {
    private final Class<T> type;

    private AttributeType(Class<T> type) {
        this.type = type;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <T> AttributeType<T> create(Class<T> type) {
        return new AttributeType<>(type);
    }

    @Contract("_ -> new")
    @SuppressWarnings("unchecked")
    public static <T> AttributeType<T> create(TypeToken<T> token) {
        return new AttributeType<>((Class<T>) token.getRawType());
    }

    public Class<T> getType() {
        return type;
    }
}
