package com.alessandro.astages.api.misc;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

@NotNullParamsAndMethodsReturn
public record Twin<A, B>(@Nullable A id, @Nullable B value) {
    public Twin() {
        this(null, null);
    }

    public boolean isValid() {
        return id != null && value != null;
    }

    public static <A, B> void encode(FriendlyByteBuf buf, Twin<A, B> twin, BiConsumer<FriendlyByteBuf, A> encoderA, BiConsumer<FriendlyByteBuf, B> encoderB) {
        encoderA.accept(buf, twin.id());
        encoderB.accept(buf, twin.value());
    }

    public static <A, B> Twin<A, B> decode(FriendlyByteBuf buf, Function<FriendlyByteBuf, A> decoderA, Function<FriendlyByteBuf, B> decoderB) {
        return new Twin<>(decoderA.apply(buf), decoderB.apply(buf));
    }
}
