package net.dodogang.marbles.terrain.store;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;

public class DataStoreChunk {
    private final Int2ObjectMap<TerrainDataStore> stores = new Int2ObjectOpenHashMap<>();
    private final ChunkPos pos;

    public DataStoreChunk(ChunkPos pos) {
        this.pos = pos;
    }

    public TerrainDataStore getStore(int y) {
        return stores.computeIfAbsent(y, k -> new TerrainDataStore(ChunkSectionPos.from(pos, k)));
    }

    public void write(CompoundTag nbt) {
        ListTag storesNbt = new ListTag();
        for(Int2ObjectMap.Entry<TerrainDataStore> entry : stores.int2ObjectEntrySet()) {
            if(entry.getValue().isEmpty()) continue;
            CompoundTag entryNbt = new CompoundTag();
            entryNbt.putInt("y", entry.getIntKey());
            entryNbt.put("store", entry.getValue().write());
            storesNbt.add(entryNbt);
        }
        nbt.put("stores", storesNbt);
    }

    public void read(CompoundTag nbt) {
        IntSet remove = new IntOpenHashSet(stores.keySet());

        ListTag storesNbt = nbt.getList("store", 10);
        for(int i = 0, l = storesNbt.size(); i < l; i ++) {
            CompoundTag entryNbt = storesNbt.getCompound(i);
            int y = entryNbt.getInt("y");
            TerrainDataStore store = getStore(y);
            remove.remove(y);
            store.read(entryNbt.getCompound("store"));
        }

        for(int i : remove) {
            stores.remove(i);
        }
    }
}
