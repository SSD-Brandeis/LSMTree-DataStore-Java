package com.COSI_167A.LSMTreeDataStore.Templatedb;

import java.io.*;
import java.util.*;

public class Operation {

    public enum OpCode {
        GET, SCAN, DELETE, PUT, NO_OP
    }

    private OpCode type;
    private int key;
    private List<Integer> args;

    public Operation() {
        this.type = OpCode.NO_OP;
    }

    public Operation(String opString, int key, List<Integer> args) {
        switch (opString) {
            case "I":
                this.type = OpCode.PUT;
                break;
            case "Q":
                this.type = OpCode.GET;
                break;
            case "S":
                this.type = OpCode.SCAN;
                break;
            case "D":
                this.type = OpCode.DELETE;
                break;
            default:
                this.type = OpCode.NO_OP;
        }
        this.key = key;
        this.args = new ArrayList<>(args);
    }

    public Operation(OpCode type, int key, List<Integer> args) {
        this.type = type;
        this.key = key;
        this.args = new ArrayList<>(args);
    }

    public OpCode getType() {
        return type;
    }

    public int getKey() {
        return key;
    }

    public List<Integer> getArgs() {
        return new ArrayList<>(args);
    }

    public static List<Operation> opsFromFile(String fileName) {
        List<Operation> ops = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            line = reader.readLine(); // First line is number of operations
            if (line == null) {
                return ops; // No operations found
            }

            int numOps = Integer.parseInt(line);
            ops = new ArrayList<>(numOps);

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");
                String opString = tokens[0];
                int key = Integer.parseInt(tokens[1]);
                List<Integer> args = new ArrayList<>();
                for (int i = 2; i < tokens.length; i++) {
                    args.add(Integer.parseInt(tokens[i]));
                }
                ops.add(new Operation(opString, key, args));
            }
        } catch (IOException e) {
            System.err.println("Unable to read " + fileName);
        }

        return ops;
    }
}

