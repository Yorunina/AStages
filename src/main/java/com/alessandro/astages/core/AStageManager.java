package com.alessandro.astages.core;

import com.alessandro.astages.api.stage.TemporaryStage;
import com.alessandro.astages.api.time.AMutableTime;
import com.alessandro.astages.api.time.ATime;
import com.alessandro.astages.core.stage.manager.AGenericManager;
import com.alessandro.astages.core.stage.manager.APermanentManager;
import com.alessandro.astages.core.stage.manager.ATemporaryManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class AStageManager {
    public static final AGenericManager GENERIC_INSTANCE = new AGenericManager();
    public static final APermanentManager PERMANENT_INSTANCE = new APermanentManager();
    public static final ATemporaryManager TEMPORARY_INSTANCE = new ATemporaryManager();

    public static void reloadBeforeScripts() {
//        GENERIC_INSTANCE.reloadBeforeScripts();
//        PERMANENT_INSTANCE.reloadBeforeScripts();
//        TEMPORARY_INSTANCE.reloadBeforeScripts();
    }

    public static void reloadAfterScripts() {
        GENERIC_INSTANCE.reloadAfterScripts();
//        PERMANENT_INSTANCE.reloadAfterScripts();
//        TEMPORARY_INSTANCE.reloadAfterScripts();
    }

    static {
        var buff = new TemporaryStage("stage", AMutableTime.fromFixed(new ATime("10s")))
            .whenGranted(event -> {
                event.getServer().sendSystemMessage(Component.literal("New Buff Unlocked!"));
                if (event.getPlayer() == null) { return; }
                event.getPlayer().sendSystemMessage(Component.literal("New Buff Unlocked!"));
                event.getPlayer().addEffect(new MobEffectInstance(MobEffects.REGENERATION, 10000, 255, false, false, false));
            })
            .whenExpired(event -> {
                event.getServer().sendSystemMessage(Component.literal("Buff Expired!"));
                if (event.getPlayer() == null) { return; }
                event.getPlayer().sendSystemMessage(Component.literal("Buff Expired!"));
                event.getPlayer().removeEffect(MobEffects.REGENERATION);
            });

        TEMPORARY_INSTANCE.addStage(buff);
    }
}
