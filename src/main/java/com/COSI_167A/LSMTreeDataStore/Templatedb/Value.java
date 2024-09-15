package com.COSI_167A.LSMTreeDataStore.Templatedb;

import java.util.List;
import java.util.Objects;

public class Value {
    public List<Integer> items;
    public boolean visible = true;

    public Value() {
    }

    public Value(boolean _visible) {
        this.visible = _visible;
    }

    public Value(List<Integer> _items) {
        this.items = _items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Value)) return false;
        Value value = (Value) o;
        return visible == value.visible && Objects.equals(items, value.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, visible);
    }
}
