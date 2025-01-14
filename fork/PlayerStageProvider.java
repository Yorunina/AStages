package com.alessandro.astages.capability;

import com.alessandro.astages.Astages;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class PlayerStageProvider {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Astages.MODID);

    public static final Supplier<AttachmentType<PlayerStage>> PLAYER_STAGE = ATTACHMENT_TYPES.register(
        "player_stage", () -> AttachmentType.serializable(PlayerStage::new).build()
    );
}
