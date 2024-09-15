package com.COSI_167A.LSMTreeDataStore.Templatedb;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DB {
    public enum DbStatus {
        OPEN, CLOSED, ERROR_OPEN
    }

    private DbStatus status;
    private Map<Integer, Value> table = new HashMap<>();
    private File file;
    private int valueDimensions = 0;

    public DB() {
    }

    public DbStatus getStatus() {
        return status;
    }

    public Value get(int key) {
        return table.getOrDefault(key, new Value(false));
    }

    public void put(int key, Value val) {
        table.put(key, val);
    }

    public List<Value> scan() {
        return new ArrayList<>(table.values());
    }

    public List<Value> scan(int minKey, int maxKey) {
        return table.entrySet().stream()
                .filter(entry -> entry.getKey() >= minKey && entry.getKey() <= maxKey)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public void del(int key) {
        table.remove(key);
    }

    public void del(int minKey, int maxKey) {
        table.entrySet().removeIf(entry -> entry.getKey() >= minKey && entry.getKey() <= maxKey);
    }

    public int size() {
        return table.size();
    }

    public DbStatus open(String fname) {
        file = new File(fname);
        try {
            if (!file.exists()) {
                file.createNewFile();
                status = DbStatus.OPEN;
                return status;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                reader.readLine(); // Skip header
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    int key = Integer.parseInt(parts[0]);
                    List<Integer> items = Arrays.stream(parts, 1, parts.length)
                                                .map(Integer::parseInt)
                                                .collect(Collectors.toList());
                    put(key, new Value(items));
                    if (valueDimensions == 0) {
                        valueDimensions = items.size();
                    }
                }
            }

            status = DbStatus.OPEN;
        } catch (IOException e) {
            status = DbStatus.ERROR_OPEN;
        }
        return status;
    }

    public boolean close() {
        if (file != null && file.exists()) {
            writeToFile();
        }
        status = DbStatus.CLOSED;
        return true;
    }

    public boolean loadDataFile(String fname) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fname))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String opCode = parts[0];
                int key = Integer.parseInt(parts[1]);
                List<Integer> items = Arrays.stream(parts, 2, parts.length)
                                            .map(Integer::parseInt)
                                            .collect(Collectors.toList());
                put(key, new Value(items));
            }
        } catch (IOException e) {
            System.err.println("Unable to read " + fname);
            return false;
        }
        return true;
    }

    public List<Value> executeOp(Operation op) {
        List<Value> results = new ArrayList<>();
        switch (op.getType()) {
            case GET:
                results.add(get(op.getKey()));
                break;
            case PUT:
                put(op.getKey(), new Value(op.getArgs()));
                break;
            case SCAN:
                results = scan(op.getKey(), op.getArgs().get(0));
                break;
            case DELETE:
                if (op.getArgs().size() > 0) {
                    del(op.getKey(), op.getArgs().get(0));
                } else {
                    del(op.getKey());
                }
                break;
            default:
                System.out.printf("Invalid Operation: %s%n", op.getType());
        }
        return results;
    }

    private boolean writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(table.size() + "," + valueDimensions + "\n");
            for (Map.Entry<Integer, Value> entry : table.entrySet()) {
                String line = entry.getKey() + "," + entry.getValue().items.stream()
                                           .map(Object::toString)
                                           .collect(Collectors.joining(","));
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
