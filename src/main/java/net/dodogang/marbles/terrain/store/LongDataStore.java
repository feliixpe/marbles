package net.dodogang.marbles.terrain.store;

import net.minecraft.util.collection.PackedIntegerArray;

import java.util.function.DoubleConsumer;
import java.util.function.LongConsumer;

public class LongDataStore {
    public static final int STORE_BITS = 32;

    private final PackedIntegerArray store;

    public LongDataStore(PackedIntegerArray store) {
        this.store = store;
    }

    public PackedIntegerArray getStore() {
        return store;
    }

    public long get(int index) {
        int hi = store.get(index * 2);
        int lo = store.get(index * 2 + 1);
        return  (long) hi << 32 | (long) lo;
    }

    public void set(int index, long value) {
        int hi = (int)(value >>> 32 & 0xFFFFFFFFL);
        int lo = (int)(value & 0xFFFFFFFFL);
        store.set(index * 2, hi);
        store.set(index * 2 + 1, lo);
    }

    public int length() {
        return store.getSize() / 2;
    }

    public void foreach(LongConsumer consumer) {
        for(int i = 0, l = length(); i < l; i ++) {
            consumer.accept(get(i));
        }
    }
}
