package com.alessandro.astages.mixin.item;

import mezz.jei.library.render.ItemStackRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ItemStackRenderer.class, remap = false)
public class AItemStackRender {
    // @Inject(method = "getTooltip(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;", at = @At("HEAD"))
    /**
     * @author Alessandro
     * @reason Lock JEI from displaying tooltip at startup
     */
//    @Overwrite
//    public List<Component> getTooltip(@NotNull ItemStack ingredient, TooltipFlag tooltipFlag) {
//        Minecraft minecraft = Minecraft.getInstance();
//        Player player = minecraft.player;
//        ClientEventHandler.jeiGetter = true;
//        AStages.LOGGER.debug("GET TOOLTIP FROM METHOD 1");
//        var toReturn = ingredient.getTooltipLines(player, tooltipFlag);
//        ClientEventHandler.jeiGetter = false;
//        return toReturn;
//    }

    /**
     * @author Alessandro
     * @reason Lock JEI from displaying tooltip at startup
     */
//    @Overwrite
//    public void getTooltip(@NotNull ITooltipBuilder tooltip, @NotNull ItemStack ingredient, TooltipFlag tooltipFlag) {
//        Minecraft minecraft = Minecraft.getInstance();
//        Player player = minecraft.player;
//        ClientEventHandler.jeiGetter = true;
//        AStages.LOGGER.debug("GET TOOLTIP FROM METHOD 2");
//        List<Component> components = ingredient.getTooltipLines(player, tooltipFlag);
//        ClientEventHandler.jeiGetter = false;
//        tooltip.addAll(components);
//    }
}
