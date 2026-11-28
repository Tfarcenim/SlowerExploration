package tfar.slowerexploration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.LevelData;
import tfar.slowerexploration.attachments.DataAttachmentUtil;

public class ExplorationData extends SavedData {

    private static final Identifier FILE_ID = SlowerExploration.id("exploration_data");
    public static final Codec<ExplorationData> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                            Codec.BOOL.fieldOf("first_launch").forGetter(r -> r.firstLaunch)
                    )
                    .apply(i, ExplorationData::new)
    );
    public static final SavedDataType<ExplorationData> TYPE = new SavedDataType<>(FILE_ID, ExplorationData::new, CODEC,
            null);

    private boolean firstLaunch = true;

    public ExplorationData() {
        setDirty();
    }

    public ExplorationData(boolean firstLaunch) {
        this.firstLaunch = firstLaunch;
    }

    public void markStartingChunks(ServerLevel level) {
    if (!firstLaunch) return;

    int r = level.getGameRules().get(SEGameRules.STARTING_RADIUS);
    LevelData.RespawnData respawnData = level.getRespawnData();

    BlockPos pos = respawnData.pos();
    ChunkPos chunkPos = ChunkPos.containing(pos);

    for (int z = -r ; z <= r;z++) {
        for (int x = -r ; x <= r;x++) {
            LevelChunk levelChunk = level.getChunk(chunkPos.x() +x, chunkPos.z() +z);
            DataAttachmentUtil.markChunkExplorationState(levelChunk,ExplorationState.UNLOCKED);
        }
    }
    firstLaunch = false;
        setDirty();
    }
}
