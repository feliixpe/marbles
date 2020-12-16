package net.dodogang.marbles.terrain.store;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.ThrowableDeliverer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.storage.RegionFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public final class RegionStorage implements AutoCloseable {
    private final Long2ObjectLinkedOpenHashMap<RegionFile> cached = new Long2ObjectLinkedOpenHashMap<>();
    private final File directory;
    private final boolean dsync;

    public RegionStorage(File directory, boolean dsync) {
        this.directory = directory;
        this.dsync = dsync;
    }

    private RegionFile getRegionFile(ChunkPos pos) throws IOException {
        long lpos = ChunkPos.toLong(pos.getRegionX(), pos.getRegionZ());
        RegionFile region = cached.getAndMoveToFirst(lpos);
        if (region == null) {
            if (cached.size() >= 256) {
                cached.removeLast().close();
            }

            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, "r." + pos.getRegionX() + "." + pos.getRegionZ() + ".mca");
            cached.putAndMoveToFirst(lpos, region = new RegionFile(file, directory, dsync));
        }
        return region;
    }

    public CompoundTag getTagAt(ChunkPos pos) throws IOException {
        RegionFile region = getRegionFile(pos);
        try (DataInputStream in = region.getChunkInputStream(pos)) {
            if (in != null)
                return NbtIo.read(in);
        }
        return null;
    }

    public void write(ChunkPos pos, CompoundTag tag) throws IOException {
        RegionFile region = this.getRegionFile(pos);
        if (tag == null) {
            region.method_31740(pos);
        } else {
            try (DataOutputStream out = region.getChunkOutputStream(pos)) {
                NbtIo.write(tag, out);
            }
        }
    }

    @Override
    public void close() throws IOException {
        ThrowableDeliverer<IOException> accumulator = new ThrowableDeliverer<>();
        for (RegionFile region : this.cached.values()) {
            try {
                region.close();
            } catch (IOException var5) {
                accumulator.add(var5);
            }
        }

        accumulator.deliver();
    }

    public void flush() throws IOException {
        for (RegionFile region : cached.values()) {
            region.method_26981();
        }
    }
}
