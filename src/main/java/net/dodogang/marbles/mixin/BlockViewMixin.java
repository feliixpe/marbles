package net.dodogang.marbles.mixin;

import net.dodogang.marbles.block.entity.SpotlightBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockView.class)
public interface BlockViewMixin {
    @Shadow
    BlockState getBlockState(BlockPos pos);

    /**
     * @author Shadew
     */
    @Overwrite
    default int getLuminance(BlockPos pos) {
        BlockView self = (BlockView) this;
        BlockEntity be = self.getBlockEntity(pos);

        if (be instanceof SpotlightBlockEntity) {
            return 15;
        }

        return getBlockState(pos).getLuminance();
    }
}
