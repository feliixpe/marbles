package net.dodogang.marbles.world.lightext;

import net.minecraft.util.math.BlockPos;

import java.util.stream.Stream;

public interface ExtendedLightChunkSection {
    Stream<BlockPos> getAffectingSpotlights();
    void addAffectingSpotlight(BlockPos affector);
    void removeAffectingSpotlight(BlockPos affector);
}
