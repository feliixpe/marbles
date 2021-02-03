package net.dodogang.marbles.block.entity;

import net.dodogang.marbles.init.MarblesBlockEntities;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import net.minecraft.world.LightType;

public class SpotlightBlockEntity extends BlockEntity implements Tickable {
    private boolean cancelling;

    public SpotlightBlockEntity() {
        super(MarblesBlockEntities.SPOTLIGHT);
    }

    @Override
    public void tick() {
        assert world != null;

        if (world.isClient) {
            if (!cancelling) {
                updateLight();
            }
        }
    }

    private void updateLight() {
        assert world != null;
        if (world.getLightingProvider().get(LightType.BLOCK).getLightLevel(pos) != 15) {
            world.getLightingProvider().addLightSource(pos, 15);
        }
    }

    public void cancelLight() {
        assert world != null;
        cancelling = true;
        world.getLightingProvider().checkBlock(pos);
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        cancelling = true;
    }
}
