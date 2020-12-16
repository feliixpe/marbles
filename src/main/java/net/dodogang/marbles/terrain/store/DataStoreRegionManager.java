package net.dodogang.marbles.terrain.store;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.dimension.DimensionType;

import java.io.IOException;

public class DataStoreRegionManager {
    private final ServerWorld world;
    private final RegionStorage storage;
    private final Object2ObjectLinkedOpenHashMap<ChunkPos, DataStoreChunk> chunks = new Object2ObjectLinkedOpenHashMap<>();

    public DataStoreRegionManager(ServerWorld world) {
        this.world = world;
        this.storage = new RegionStorage(DimensionType.getSaveDirectory(world.getRegistryKey(), world.getServer().getRunDirectory()), false);
    }

    public ServerWorld getWorld() {
        return world;
    }

    public void close() {
        try {
            storage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveAll() {
        chunks.keySet().forEach(this::save);
    }

    public void save(ChunkPos pos) {
        DataStoreChunk chunk = chunks.get(pos);
        if(chunk == null) return;
        CompoundTag nbt = new CompoundTag();
        chunk.write(nbt);
        try {
            storage.write(pos, nbt);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public DataStoreChunk getChunk(ChunkPos pos) {
        return chunks.computeIfAbsent(pos, p -> {
            try {
                CompoundTag tag = storage.getTagAt(p);
                DataStoreChunk chunk = chunks.computeIfAbsent(p, DataStoreChunk::new);
                if (tag != null) chunk.read(tag);
                return chunk;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
