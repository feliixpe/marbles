package net.dodogang.marbles.terrain.store;

import net.minecraft.util.collection.PackedIntegerArray;

import java.util.function.IntConsumer;

public class IntDataStore {
    public static final int STORE_BITS = 32;

    private final PackedIntegerArray store;

    public IntDataStore(PackedIntegerArray store) {
        this.store = store;
    }

    public PackedIntegerArray getStore() {
        return store;
    }

    public int get(int index) {
        return store.get(index);
    }

    public void set(int index, int value) {
        store.set(index, value);
    }

    public int length() {
        return store.getSize();
    }

    public void foreach(IntConsumer consumer) {
        for(int i = 0, l = length(); i < l; i ++) {
            consumer.accept(get(i));
        }
    }
}
