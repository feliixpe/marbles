package net.dodogang.marbles.terrain.store;

import net.minecraft.util.collection.PackedIntegerArray;

import java.util.function.DoubleConsumer;

public class DoubleDataStore {
    public static final int STORE_BITS = 32;

    private final PackedIntegerArray store;

    public DoubleDataStore(PackedIntegerArray store) {
        this.store = store;
    }

    public PackedIntegerArray getStore() {
        return store;
    }

    public double get(int index) {
        int hi = store.get(index * 2);
        int lo = store.get(index * 2 + 1);
        long iv = (long) hi << 32 | (long) lo;
        return Double.longBitsToDouble(iv);
    }

    public void set(int index, double value) {
        long iv = Double.doubleToRawLongBits(value);
        int hi = (int)(iv >>> 32 & 0xFFFFFFFFL);
        int lo = (int)(iv & 0xFFFFFFFFL);
        store.set(index * 2, hi);
        store.set(index * 2 + 1, lo);
    }

    public int length() {
        return store.getSize() / 2;
    }

    public void foreach(DoubleConsumer consumer) {
        for(int i = 0, l = length(); i < l; i ++) {
            consumer.accept(get(i));
        }
    }
}
