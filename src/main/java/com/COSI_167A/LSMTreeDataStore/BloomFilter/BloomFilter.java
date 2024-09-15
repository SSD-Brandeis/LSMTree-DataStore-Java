package com.COSI_167A.LSMTreeDataStore.BloomFilter;

import java.util.ArrayList;
import java.util.List;

public class BloomFilter {

    private int numElement;
    private int bitsPerElement;
    private int numIndex;
    private int size;
    private List<Boolean> bfVec;

    public BloomFilter() {
        this.numElement = 1024;
        this.bitsPerElement = 10;
        this.numIndex = (int) Math.floor(0.693 * bitsPerElement + 0.5);
        this.size = numElement * bitsPerElement;
        this.bfVec = new ArrayList<>(size);
        makeBloomFilter();
    }

    public BloomFilter(int numElement, int bitsPerElement) {
        this.numElement = numElement;
        this.bitsPerElement = bitsPerElement;
        this.numIndex = (int) Math.floor(0.693 * bitsPerElement + 0.5);
        this.size = numElement * bitsPerElement;
        this.bfVec = new ArrayList<>(size);
        makeBloomFilter();
    }

    private void makeBloomFilter() {
        // Initialize all elements in bfVec to false
        for (int i = 0; i < size; i++) {
            bfVec.add(false);
        }
    }

    public void program(String key) {
        List<Integer> index = new ArrayList<>(numIndex);
        for (int i = 0; i < numIndex; i++) {
            index.add(0);
        }
        getIndex(key, index);

        for (int i = 0; i < numIndex; i++) {
            bfVec.set(index.get(i), true);
        }
    }

    public boolean query(String key) {
        List<Integer> index = new ArrayList<>(numIndex);
        for (int i = 0; i < numIndex; i++) {
            index.add(0);
        }
        getIndex(key, index);

        for (int i = 0; i < numIndex; i++) {
            if (!bfVec.get(index.get(i))) {
                return false;
            }
        }

        return true; // positive
    }

    private void getIndex(String key, List<Integer> index) {
        int h = MurmurHash2.hash(key, 0xbc9f1d34);

        final int delta = (h >> 17) | (h << 15); // Rotate right 17 bits
        for (int i = 0; i < numIndex; i++) {
            index.set(i, Math.floorMod(h, size));
            h += delta;
        }
    }

    public int getIndexNum() {
        return numIndex;
    }

    public int getSize() {
        return size;
    }
}
