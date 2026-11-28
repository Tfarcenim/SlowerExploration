package tfar.slowerexploration.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.slowerexploration.SlowerExplorationClient;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(method = "doAnimateTick",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;environmentAttributes()Lnet/minecraft/world/attribute/EnvironmentAttributeSystem;"))
    private void spawnCustomAmbientParticles(int xt, int yt, int zt, int r,
                                       RandomSource animateRandom, Block markerParticleTarget, BlockPos.MutableBlockPos pos, CallbackInfo ci) {
        SlowerExplorationClient.spawnAmbientParticles((ClientLevel)(Object) this,xt,yt,zt,r,animateRandom,markerParticleTarget,pos);
    }
}
