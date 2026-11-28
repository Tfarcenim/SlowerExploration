package tfar.slowerexploration;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import tfar.slowerexploration.attachments.DataAttachmentUtil;

public class SECommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(Commands.literal(SlowerExploration.MOD_ID)
                .then(Commands.literal("exploration_state")
                        .executes(SECommands::explorationState)
                )
        );
    }


    public static int explorationState(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        LevelChunk chunk = source.getLevel().getChunkAt(player.blockPosition());
        ExplorationState explorationState = DataAttachmentUtil.getChunkExplorationState(chunk);
        int explorationProgress = DataAttachmentUtil.getExplorationProgress(chunk);
        source.sendSuccess(() -> Component.literal(explorationState.getSerializedName()),false);
        source.sendSuccess(() -> Component.literal(""+explorationProgress),false);
        return 1;
    }
}
