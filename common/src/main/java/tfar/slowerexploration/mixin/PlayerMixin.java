package tfar.slowerexploration.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.slowerexploration.SlowerExploration;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "blockActionRestricted",at = @At("HEAD"),cancellable = true)
    private void preventBlockPlacing(Level level, BlockPos pos, GameType gameType, CallbackInfoReturnable<Boolean> cir) {
        if (gameType == GameType.SURVIVAL) {
            ChunkPos chunkPos = ChunkPos.containing(pos);
            if (!SlowerExploration.isChunkModifiable(level,chunkPos.x(),chunkPos.z())) {
                cir.setReturnValue(true);
            }
        }
    }
}
