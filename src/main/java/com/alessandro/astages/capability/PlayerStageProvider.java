package com.alessandro.astages.capability;

import com.alessandro.astages.Astages;
import com.alessandro.astages.networking.packet.StageDataSyncS2CPacket;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class PlayerStageProvider {
//    private static final StreamCodec<ByteBuf, PlayerStage> PLAYER_STAGE_CODEC = StreamCodec.composite(
//        ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()),
//        PlayerStage::setStages,
//        PlayerStage::new
//    );

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Astages.MODID);
    public static final Supplier<AttachmentType<PlayerStage>> PLAYER_STAGE = ATTACHMENT_TYPES.register(
        "player_stage", () -> AttachmentType.serializable(PlayerStage::new).build()
    );
}
