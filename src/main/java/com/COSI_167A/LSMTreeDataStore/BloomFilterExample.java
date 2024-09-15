package com.COSI_167A.LSMTreeDataStore;

import com.COSI_167A.LSMTreeDataStore.BloomFilter.BloomFilter;

public class BloomFilterExample {

    public static void queryTest(BloomFilter bf, String key) {
        boolean result = bf.query(key);
        String resultStr = result ? "positive" : "negative";
        System.out.println("Bloom Filter query result of " + key + " is " + resultStr);
    }

    public static void main(String[] args) {
        BloomFilter bf = new BloomFilter(1024, 10); // number of keys, bits per element

        // Empty filter is not match, at this level
        queryTest(bf, "hello");
        queryTest(bf, "world");

        // insert two strings
        bf.program("hello");
        bf.program("world");

        queryTest(bf, "hello");
        queryTest(bf, "world");
        queryTest(bf, "x");
        queryTest(bf, "foo");
    }
}

