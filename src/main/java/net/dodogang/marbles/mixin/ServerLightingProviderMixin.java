package net.dodogang.marbles.mixin;

import net.dodogang.marbles.block.SpotlightBlock;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.light.LightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLightingProvider.class)
public abstract class ServerLightingProviderMixin extends LightingProvider {
//    @Shadow protected abstract void enqueue(int x, int z, ServerLightingProvider.Stage stage, Runnable task);

    public ServerLightingProviderMixin(ChunkProvider chunkProvider, boolean hasBlockLight, boolean hasSkyLight) {
        super(chunkProvider, hasBlockLight, hasSkyLight);
    }

    /**
     * @author Shadew
     */
    @Overwrite
    @Override
    public void addLightSource(BlockPos pos, int level) {
        super.addLightSource(pos, level); // Overwrites exception thrown
    }

    @Inject(method = "method_17312", at = @At(
        value = "INVOKE",
        shift = At.Shift.BEFORE,
        target = "Lnet/minecraft/world/chunk/Chunk;getLightSourcesStream()Ljava/util/stream/Stream;"
    ))
    private void onLight(Chunk chunk, ChunkPos pos, boolean exclBlocks, CallbackInfo info) {
        SpotlightBlock.findSpotlightLightSources(chunk)
                      .forEach(srcPos -> super.addLightSource(srcPos, 15));
    }
}
