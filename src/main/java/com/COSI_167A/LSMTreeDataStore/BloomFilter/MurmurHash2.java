package com.COSI_167A.LSMTreeDataStore.BloomFilter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MurmurHash2 {

    public static int hash(String key, int seed) {
        // Get bytes of the input key
        byte[] data = key.getBytes();
        int len = data.length;

        // 'm' and 'r' are mixing constants generated offline.
        // They're not really 'magic', they just happen to work well.
        final int m = 0x5bd1e995;
        final int r = 24;

        // Initialize the hash to a 'random' value
        int h = seed ^ len;

        // Mix 4 bytes at a time into the hash
        int index = 0;
        while (len >= 4) {
            int k = ByteBuffer.wrap(data, index, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h *= m;
            h ^= k;

            index += 4;
            len -= 4;
        }

        // Handle the last few bytes of the input array
        switch (len) {
            case 3:
                h ^= (data[index + 2] & 0xFF) << 16;
            case 2:
                h ^= (data[index + 1] & 0xFF) << 8;
            case 1:
                h ^= (data[index] & 0xFF);
                h *= m;
        }

        // Do a few final mixes of the hash to ensure the last few bytes are well-incorporated
        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;

        return h;
    }

    public static int hashNeutral(String key, int seed) {
        byte[] data = key.getBytes();
        int len = data.length;
        final int m = 0x5bd1e995;
        final int r = 24;

        int h = seed ^ len;
        int index = 0;

        while (len >= 4) {
            int k = (data[index] & 0xFF)
                  | ((data[index + 1] & 0xFF) << 8)
                  | ((data[index + 2] & 0xFF) << 16)
                  | ((data[index + 3] & 0xFF) << 24);

            k *= m;
            k ^= k >>> r;
            k *= m;

            h *= m;
            h ^= k;

            index += 4;
            len -= 4;
        }

        switch (len) {
            case 3:
                h ^= (data[index + 2] & 0xFF) << 16;
            case 2:
                h ^= (data[index + 1] & 0xFF) << 8;
            case 1:
                h ^= (data[index] & 0xFF);
                h *= m;
        }

        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;

        return h;
    }
}

