package net.dodogang.marbles.terrain.store;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.PackedIntegerArray;
import net.minecraft.util.math.ChunkSectionPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class TerrainDataStore {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ChunkSectionPos pos;
    private final Map<Identifier, DataStoreBuffer> data = new HashMap<>();

    private final ReentrantLock lock = new ReentrantLock();

    public TerrainDataStore(ChunkSectionPos pos) {
        this.pos = pos;
    }

    public ChunkSectionPos getPos() {
        return pos;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public void removeStoreData(Identifier id) {
        data.remove(id);
    }

    public DataStoreBuffer getStoreData(Identifier id) {
        if (data.containsKey(id)) {
            return data.get(id).open();
        }
        int len = TerrainDataTypes.getDataLength(id);
        int bits = TerrainDataTypes.getStoreBits(id);
        if (len < 0 || bits < 0) {
            throw new IllegalArgumentException("Unknown data type: " + id);
        }
        PackedIntegerArray dataArr = new PackedIntegerArray(bits, len);
        DataStoreBuffer store = new DataStoreBuffer(dataArr);
        data.put(id, store);
        return store.open();
    }

    public CompoundTag write() {
        lock.lock();
        CompoundTag nbt = new CompoundTag();
        for (Map.Entry<Identifier, DataStoreBuffer> dataEntry : data.entrySet()) {
            try (DataStoreBuffer store = dataEntry.getValue().open()) {
                CompoundTag entryNbt = new CompoundTag();
                entryNbt.put("data", new LongArrayTag(store.store().getStorage()));
                entryNbt.putInt("bitsize", TerrainDataTypes.getStoreBits(dataEntry.getKey()));
                entryNbt.putInt("datasize", TerrainDataTypes.getDataLength(dataEntry.getKey()));
                nbt.put(dataEntry.getKey().toString(), entryNbt);
            }
        }
        return nbt;
    }

    public void read(CompoundTag nbt) {
        Set<Identifier> remove = new HashSet<>(data.keySet());
        for (String key : nbt.getKeys()) {
            Identifier id = new Identifier(key);

            if (TerrainDataTypes.getDataLength(id) < 0) {
                LOGGER.error("Didn't load store data '" + id + "': type unknown");
                continue;
            }

            CompoundTag entryNbt = nbt.getCompound(key);
            long[] dataArr = entryNbt.getLongArray("data");
            int bitsize = entryNbt.getInt("bitsize");
            int datasize = entryNbt.getInt("datasize");

            if (bitsize != TerrainDataTypes.getStoreBits(id)) {
                LOGGER.error("Didn't load store data '" + id + "': known bit size and actual bit size do not match");
                continue;
            }

            if (datasize != TerrainDataTypes.getDataLength(id)) {
                LOGGER.error("Didn't load store data '" + id + "': known data size and actual data size do not match");
                continue;
            }

            try(DataStoreBuffer store = getStoreData(id)) {
                long[] actualStore = store.store().getStorage();
                System.arraycopy(dataArr, 0, actualStore, 0, Math.min(dataArr.length, actualStore.length));
            }

            remove.add(id);
        }
        for(Identifier rem : remove) {
            // No need to lock, if anyone's writing to this store then he just writes to the discarded store
            data.remove(rem);
        }
    }
}
