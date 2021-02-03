package net.dodogang.marbles.mixin;

import net.dodogang.marbles.block.SpotlightBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin {
    @Inject(method = "getLightSourcesStream", at = @At("RETURN"))
    private void addSpotlightLightSources(CallbackInfoReturnable<Stream<BlockPos>> info) {
        WorldChunk self = WorldChunk.class.cast(this);

        Stream<BlockPos> curr = info.getReturnValue();
        Stream<BlockPos> spotlightSources = SpotlightBlock.findSpotlightLightSources(self);
        info.setReturnValue(Stream.concat(curr, spotlightSources));
    }
}
