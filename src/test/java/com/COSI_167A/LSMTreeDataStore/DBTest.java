package com.COSI_167A.LSMTreeDataStore;

import com.COSI_167A.LSMTreeDataStore.Templatedb.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DBTest {

    private DB db0;
    private DB db1;
    private DB db2;

    private Value v1 = new Value(List.of(1, 2));
    private Value v2 = new Value(List.of(6, 10));
    private Value v3 = new Value(List.of(1, 1, 5, 7));
    private Value v4 = new Value(List.of(13, 176));

    @BeforeEach
    void setUp() {
        db1 = new DB();
        db2 = new DB();
        
        db1.put(2, v1);
        db1.put(5, v2);
        db2.put(1024, v3);
    }

    @Test
    void isEmptyInitially() {
        db0 = new DB();  // Instantiate db0 in the test itself to match the structure
        assertEquals(0, db0.size());
    }

    @Test
    void getFunctionality() {
        Value val = db1.get(2);
        assertEquals(v1, val);
    }

    @Test
    void putAndGetFunctionality() {
        db0 = new DB();  // Instantiate db0 in the test itself to match the structure
        db0.put(10, v4);
        Value getValue = db0.get(10);

        assertEquals(v4, getValue);
    }

    @Test
    void deleteFunctionality() {
        db1.del(2);
        assertEquals(new Value(false), db1.get(2));
        assertEquals(1, db1.size());

        db2.del(1024);
        assertEquals(new Value(false), db2.get(1024));
        assertEquals(0, db2.size());
    }

    @Test
    void scanFunctionality() {
        List<Value> vals;

        vals = db2.scan();
        assertEquals(1, vals.size());
        assertEquals(v3, vals.get(0));

        vals = db1.scan(1, 3);
        assertEquals(1, vals.size());
        assertEquals(v1, vals.get(0));

        vals = db1.scan();
        assertEquals(2, vals.size());
    }
}
