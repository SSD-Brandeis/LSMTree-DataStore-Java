package com.COSI_167A.LSMTreeDataStore;

import com.COSI_167A.LSMTreeDataStore.Templatedb.*;
import org.apache.commons.cli.*;

import java.util.List;

public class SimpleBenchmark {

    public static class Config {
        public String dbname;
        public String fname;
        public String wlname;
    }

    public static Config cmdlineSettings(String[] args) {
        Config cfg = new Config();
        Options options = new Options();

        options.addOption(Option.builder("f")
                .longOpt("file")
                .hasArg()
                .desc("(ingest data file)")
                .build());
        options.addOption(Option.builder("w")
                .longOpt("work")
                .required()
                .hasArg()
                .desc("(input workload file)")
                .build());

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        
        try {
            cmd = parser.parse(options, args);

            List<String> argList = cmd.getArgList();
            if (argList.isEmpty()) {
                System.err.println("Database name is required as the first argument.");
                formatter.printHelp("simple_benchmark", options);
                System.exit(1);
            }

            cfg.dbname = cmd.getArgList().get(0);  // First argument is the database name
            cfg.fname = cmd.getOptionValue("file", "");
            cfg.wlname = cmd.getOptionValue("work");
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("simple_benchmark", options);
            System.exit(1);
        }

        return cfg;
    }

    public static void main(String[] args) {
        Config cfg = cmdlineSettings(args);

        DB db = new DB();
        if (db.open(cfg.dbname) != DB.DbStatus.OPEN) {
            System.err.println("Unable to load DB " + cfg.dbname);
            System.exit(1);
        }

        if (!cfg.fname.isEmpty()) {
            if (!db.loadDataFile(cfg.fname)) {
                System.err.println("Unable to load data file " + cfg.fname + " into DB");
                if (db.getStatus() == DB.DbStatus.OPEN) {
                    db.close();
                }
                System.exit(1);
            }
        }

        List<Operation> ops = Operation.opsFromFile(cfg.wlname);

        long start = System.currentTimeMillis();
        for (Operation op : ops) {
            db.executeOp(op);
        }
        long end = System.currentTimeMillis();
        long duration = end - start;

        System.out.printf("Workload Time %d ms%n", duration);

        if (db.getStatus() == DB.DbStatus.OPEN) {
            db.close();
        }
    }
}

