package net.dodogang.marbles.terrain.store;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.PackedIntegerArray;
import net.minecraft.util.math.ChunkSectionPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class TerrainDataStore {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ChunkSectionPos pos;
    private final Map<Identifier, PackedIntegerArray> data = new HashMap<>();

    public TerrainDataStore(ChunkSectionPos pos) {
        this.pos = pos;
    }

    public ChunkSectionPos getPos() {
        return pos;
    }

    public PackedIntegerArray getStore(Identifier id) {
        if(data.containsKey(id)) {
            return data.get(id);
        }
        int len = TerrainDataTypes.getDataLength(id);
        int bits = TerrainDataTypes.getStoreBits(id);
        if(len < 0 || bits < 0) {
            throw new IllegalArgumentException("Unknown data type: " + id);
        }
        PackedIntegerArray dataArr = new PackedIntegerArray(bits, len);
        data.put(id, dataArr);
        return dataArr;
    }

    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        for (Map.Entry<Identifier, PackedIntegerArray> dataEntry : data.entrySet()) {
            CompoundTag entryNbt = new CompoundTag();
            entryNbt.put("data", new LongArrayTag(dataEntry.getValue().getStorage()));
            entryNbt.putInt("bitsize", TerrainDataTypes.getStoreBits(dataEntry.getKey()));
            entryNbt.putInt("datasize", TerrainDataTypes.getDataLength(dataEntry.getKey()));
            nbt.put(dataEntry.getKey().toString(), entryNbt);
        }
        return nbt;
    }

    public void read(CompoundTag nbt) {
        for (String key : nbt.getKeys()) {
            Identifier id = new Identifier(key);
            if(TerrainDataTypes.getDataLength(id) < 0) {
                LOGGER.error("Didn't load store data '" + id + "': type unknown");
                continue;
            }
            CompoundTag entryNbt = new CompoundTag();
            long[] dataArr = entryNbt.getLongArray("data");
            int bitsize = entryNbt.getInt("bitsize");
            int datasize = entryNbt.getInt("datasize");
            if(bitsize != TerrainDataTypes.getStoreBits(id)) {
                LOGGER.error("Didn't load store data '" + id + "': known bit size and actual bit size do not match");
                continue;
            }
            if(datasize != TerrainDataTypes.getDataLength(id)) {
                LOGGER.error("Didn't load store data '" + id + "': known data size and actual data size do not match");
                continue;
            }
            data.put(id, new PackedIntegerArray(bitsize, datasize, dataArr));
        }
    }
}
