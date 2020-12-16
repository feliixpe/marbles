package net.dodogang.marbles.terrain.store;

import net.minecraft.util.collection.PackedIntegerArray;

import java.util.concurrent.locks.ReentrantLock;

public class DataStoreBuffer implements AutoCloseable {
    private final PackedIntegerArray store;
    private final ReentrantLock lock = new ReentrantLock();
    private BooleanDataStore asBoolean;
    private DoubleDataStore asDouble;
    private LongDataStore asLong;
    private FloatDataStore asFloat;
    private IntDataStore asInt;

    public DataStoreBuffer(PackedIntegerArray store) {
        this.store = store;
    }

    public BooleanDataStore booleans() {
        if(asBoolean == null) asBoolean = new BooleanDataStore(store);
        return asBoolean;
    }

    public DoubleDataStore doubles() {
        if(asDouble == null) asDouble = new DoubleDataStore(store);
        return asDouble;
    }

    public FloatDataStore floats() {
        if(asFloat == null) asFloat = new FloatDataStore(store);
        return asFloat;
    }

    public LongDataStore longs() {
        if(asLong == null) asLong = new LongDataStore(store);
        return asLong;
    }

    public IntDataStore ints() {
        if(asInt == null) asInt = new IntDataStore(store);
        return asInt;
    }

    public PackedIntegerArray store() {
        return store;
    }

    public DataStoreBuffer open() {
        if(!lock.isHeldByCurrentThread())
            lock.lock();
        return this;
    }

    @Override
    public void close() {
        if(lock.isHeldByCurrentThread())
            lock.unlock();
    }
}
