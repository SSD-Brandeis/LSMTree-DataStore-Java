package com.COSI_167A.LSMTreeDataStore;

import com.COSI_167A.LSMTreeDataStore.Templatedb.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersistenceTest {

    private final String fileName = "test_db.db";
    private DB db = new DB();

    @AfterEach
    void tearDown() throws Exception {
        // Clean up the test database file after each test
        Files.deleteIfExists(Paths.get(fileName));
    }

    @Test
    void basicOpenClose() {
        Value v1 = new Value(List.of(1, 2));
        
        db.open(fileName);
        db.put(5, v1);
        db.close();

        db.open(fileName);
        Value v2 = db.get(5);
        db.close();

        assertEquals(v1, v2);
    }

    @Test
    void deleteOpenClose() {
        Value v1 = new Value(List.of(1, 2));
        Value v2 = new Value(List.of(12, 21));
        
        db.open(fileName);
        db.put(5, v1);
        db.put(1, v2);
        db.del(5);
        db.close();

        db.open(fileName);
        Value v3 = db.get(5);  // Value at key 5 should be deleted
        Value v4 = db.get(1);  // Value at key 1 should still exist
        db.close();

        assertEquals(new Value(false), v3);  // Check for deleted value
        assertEquals(v2, v4);  // Check that key 1 value is intact
    }
}
