package net.dodogang.marbles.terrain.store;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.util.collection.PackedIntegerArray;

import java.util.function.DoubleConsumer;

public class BooleanDataStore {
    public static final int STORE_BITS = 1;

    private final PackedIntegerArray store;

    public BooleanDataStore(PackedIntegerArray store) {
        this.store = store;
    }

    public PackedIntegerArray getStore() {
        return store;
    }

    public boolean get(int index) {
        return store.get(index) > 0;
    }

    public void set(int index, boolean value) {
        store.set(index, value ? 1 : 0);
    }

    public int length() {
        return store.getSize();
    }

    public void foreach(BooleanConsumer consumer) {
        for(int i = 0, l = length(); i < l; i ++) {
            consumer.accept(get(i));
        }
    }
}
