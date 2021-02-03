package net.dodogang.marbles.init;

import net.dodogang.marbles.block.entity.SpotlightBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashSet;

public class MarblesBlockEntities {

    public static final BlockEntityType<?> SPOTLIGHT = register("spotlight", new BlockEntityType<>(
        SpotlightBlockEntity::new,
        new HashSet<Block>() {
            @Override
            public boolean contains(Object o) {
                return true;
            }
        },
        null
    ));

    private static BlockEntityType<?> register(String id, BlockEntityType<?> type) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("marbles", id), type);
        return type;
    }
}
