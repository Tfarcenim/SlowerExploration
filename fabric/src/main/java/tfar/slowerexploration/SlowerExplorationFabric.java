package tfar.slowerexploration;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.ItemEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

public class SlowerExplorationFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        SlowerExploration.init();
        SlowerExploration.register();
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
           ServerLevel overworld = server.overworld();
            ExplorationData explorationData = overworld.getDataStorage().computeIfAbsent(ExplorationData.TYPE);
            explorationData.markStartingChunks(overworld);
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext,_) -> {
            SECommands.register(dispatcher,buildContext);
        });
        ItemEvents.USE_ON.register(useOnContext -> {
            Player player = useOnContext.getPlayer();
            Level level = useOnContext.getLevel();
            if (player.gameMode() == GameType.SURVIVAL) {
                BlockPos pos = useOnContext.getClickedPos();
                ChunkPos chunkPos = ChunkPos.containing(pos);
                if (!SlowerExploration.isChunkModifiable(level,chunkPos.x(),chunkPos.z())) {
                    return InteractionResult.PASS;
                } else {
                    SlowerExploration.advanceExplorationProgress(level,chunkPos.x(),chunkPos.z());
                }
            }
            return null;
        });
        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
            if (player.gameMode() == GameType.SURVIVAL) {
                ChunkPos chunkPos = ChunkPos.containing(pos);
                SlowerExploration.advanceExplorationProgress(level, chunkPos.x(), chunkPos.z());
            }
        });
    }
}
