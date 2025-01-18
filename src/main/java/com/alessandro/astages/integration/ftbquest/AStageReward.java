package com.alessandro.astages.integration.ftbquest;

public class AStageReward {
    // extends Reward {
//    public static final String STAGE_KEY = "stage";
//    public static final String REMOVE_KEY = "remove";
//
//    private String stage;
//    private boolean remove = false;
//
//    public AStageReward(long id, Quest quest, String stage) {
//        super(id, quest);
//        this.stage = stage;
//        autoclaim = RewardAutoClaim.INVISIBLE;
//    }
//
//    public AStageReward(long id, Quest quest) {
//        this(id, quest, "");
//    }
//
//    @Override
//    public RewardType getType() {
//        return AStageRewardType.A_STAGE;
//    }
//
//    @Override
//    public void writeData(CompoundTag nbt) {
//        super.writeData(nbt);
//        nbt.putString(STAGE_KEY, stage);
//        if (remove) {
//            nbt.putBoolean(REMOVE_KEY, true);
//        }
//    }
//
//    @Override
//    public void readData(CompoundTag nbt) {
//        super.readData(nbt);
//        stage = nbt.getString(STAGE_KEY);
//        remove = nbt.getBoolean(REMOVE_KEY);
//    }
//
//    @Override
//    public void writeNetData(FriendlyByteBuf buffer) {
//        super.writeNetData(buffer);
//        buffer.writeUtf(stage);
//        buffer.writeBoolean(remove);
//    }
//
//    @Override
//    public void readNetData(FriendlyByteBuf buffer) {
//        super.readNetData(buffer);
//        stage = buffer.readUtf();
//        remove = buffer.readBoolean();
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void fillConfigGroup(ConfigGroup config) {
//        super.fillConfigGroup(config);
//        config.addString(STAGE_KEY, stage, v -> stage = v, "").setNameKey("astages.ftbquests.reward.group");
//        config.addBool(REMOVE_KEY, remove, v -> remove = v, false);
//    }
//
//    @Override
//    public void claim(ServerPlayer player, boolean notify) {
//        if (remove) {
//            player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
//                playerStage.removeStage(stage);
//                playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE, stage);
//            });
//        } else {
//            player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
//                playerStage.addStage(stage);
//                playerStage.setChangedFor(player, PlayerStage.Operation.ADD, stage);
//            });
//        }
//
//        if (notify) {
//            if (remove) {
//                player.displayClientMessage(Component.translatable("chat.astages.remove", stage), true);
//            } else {
//                player.displayClientMessage(Component.translatable("chat.astages.add", stage), true);
//            }
//        }
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public Component getAltTitle() {
//        return Component.translatable("astages.ftbquests.reward.title");
//    }
//
//    @Override
//    public boolean ignoreRewardBlocking() {
//        return true;
//    }
//
//    @Override
//    protected boolean isIgnoreRewardBlockingHardcoded() {
//        return true;
//    }
}
