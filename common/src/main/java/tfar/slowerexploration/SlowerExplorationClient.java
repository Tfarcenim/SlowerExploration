package tfar.slowerexploration;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;
import tfar.slowerexploration.attachments.DataAttachmentUtil;

public class SlowerExplorationClient {
    public static void spawnAmbientParticles(ClientLevel level,int xt, int yt, int zt, int r,
                                             RandomSource animateRandom, Block markerParticleTarget, BlockPos.MutableBlockPos pos) {
        ChunkPos chunkPos = ChunkPos.containing(pos);
        LevelChunk levelChunk = level.getChunk(chunkPos.x(),chunkPos.z());

        if (DataAttachmentUtil.getChunkExplorationState(levelChunk) == ExplorationState.LOCKED) {

            level.addParticle(
                    new DustParticleOptions(0xff0000, 1),
                    pos.getX() + level.getRandom().nextDouble(),
                    pos.getY() + level.getRandom().nextDouble(),
                    pos.getZ() + level.getRandom().nextDouble(),
                    0.0,
                    0.0,
                    0.0
            );
        }
    }
}
