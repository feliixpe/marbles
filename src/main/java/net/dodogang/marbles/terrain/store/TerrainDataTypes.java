package net.dodogang.marbles.terrain.store;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class TerrainDataTypes {
    private static final Map<Identifier, Integer> STORE_BITS = new HashMap<>();
    private static final Map<Identifier, Integer> DATA_LENGTH = new HashMap<>();

    public static int getStoreBits(Identifier id) {
        Integer bits = STORE_BITS.get(id);
        if(bits == null) return -1;
        return bits;
    }

    public static int getDataLength(Identifier id) {
        Integer bits = DATA_LENGTH.get(id);
        if(bits == null) return -1;
        return bits;
    }

    public static void register(Identifier typeId, int storeBits, int dataLength) {
        STORE_BITS.put(typeId, storeBits);
        DATA_LENGTH.put(typeId, dataLength);
    }
}
