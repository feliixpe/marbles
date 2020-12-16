package net.dodogang.marbles.terrain.store;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.util.collection.PackedIntegerArray;

public class FloatDataStore {
    public static final int STORE_BITS = 32;

    private final PackedIntegerArray store;

    public FloatDataStore(PackedIntegerArray store) {
        this.store = store;
    }

    public PackedIntegerArray getStore() {
        return store;
    }

    public float get(int index) {
        return Float.intBitsToFloat(store.get(index));
    }

    public void set(int index, float value) {
        store.set(index, Float.floatToRawIntBits(value));
    }

    public int length() {
        return store.getSize();
    }

    public void foreach(FloatConsumer consumer) {
        for(int i = 0, l = length(); i < l; i ++) {
            consumer.accept(get(i));
        }
    }
}
