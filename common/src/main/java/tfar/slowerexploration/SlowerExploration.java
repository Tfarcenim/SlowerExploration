package tfar.slowerexploration;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.slowerexploration.attachments.CommonDataAttachments;
import tfar.slowerexploration.attachments.DataAttachmentUtil;
import tfar.slowerexploration.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as NeoForge events
// however it will be compatible with all supported mod loaders.
public class SlowerExploration {

    public static final String MOD_ID = "slowerexploration";
    public static final String MOD_NAME = "SlowerExploration";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {



        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.

    }

    public static void register() {
        CommonDataAttachments.init();
        SEGameRules.init();
    }

    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID,name);
    }

    public static boolean isChunkModifiable(Level level, int x, int z) {
        for (int z1 = -1; z1<=1;z1++) {
            for (int x1 = -1; x1<=1;x1++) {
                LevelChunk levelChunk = level.getChunk(x1+x,z1+z);
                ExplorationState explorationState = DataAttachmentUtil.getChunkExplorationState(levelChunk);
                if (explorationState != ExplorationState.LOCKED)  {
                    return true;
                }
            }
        }
        return false;
    }

    public static void advanceExplorationProgress(Level level,int x,int z) {
        if (!level.isClientSide()) {
            int required = ((ServerLevel)level).getGameRules().get(SEGameRules.PROGRESS_THRESHOLD);
            for (int z1 = -1; z1<=1;z1++) {
                for (int x1 = -1; x1<=1;x1++) {
                    LevelChunk levelChunk = level.getChunk(x1+x,z1+z);
                    ExplorationState explorationState = DataAttachmentUtil.getChunkExplorationState(levelChunk);
                    if (explorationState == ExplorationState.LOCKED)  {
                        DataAttachmentUtil.advanceExplorationProgress(levelChunk,required);
                    }
                }
            }
        }
    }
}