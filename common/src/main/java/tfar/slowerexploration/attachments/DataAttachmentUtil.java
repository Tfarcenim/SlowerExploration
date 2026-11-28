package tfar.slowerexploration.attachments;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import tfar.slowerexploration.ExplorationState;
import tfar.slowerexploration.platform.Services;

public class DataAttachmentUtil {

    public static <T> void clearValue(LivingEntity entity,CommonDataAttachment<T> attachment) {
        Services.PLATFORM.setAttachedValue(entity,attachment, attachment.defaultValueSupplier.apply(entity));
    }

    public static void markChunkExplorationState(LevelChunk chunk, ExplorationState state) {
        Services.PLATFORM.setAttachedValue(chunk,CommonDataAttachments.EXPLORATION_STATE,state);
    }

    public static ExplorationState getChunkExplorationState(LevelChunk chunk) {
        return Services.PLATFORM.getOrCreateAttachedValue(chunk,CommonDataAttachments.EXPLORATION_STATE);
    }

    public static void advanceExplorationProgress(LevelChunk chunk, int required) {
        int progress = getExplorationProgress(chunk);
        progress++;
        if (progress >= required) {
            markChunkExplorationState(chunk,ExplorationState.UNLOCKED);
        }
        Services.PLATFORM.setAttachedValue(chunk,CommonDataAttachments.EXPLORATION_PROGRESS,progress);
    }

    public static int getExplorationProgress(LevelChunk chunk) {
        return Services.PLATFORM.getOrCreateAttachedValue(chunk,CommonDataAttachments.EXPLORATION_PROGRESS);
    }
}
