# LSMTree-DataStore-Java

# COSI-167A: Advanced Data Systems - TemplateDB

## About

TemplateDB is a simple template for you, the student, to use for the systems
implementation project in COSI-167A. Note that this is a simple template, it is
not extensive, rather it is meant to help guide you on what we expect when
you implement the LSM tree. You can use this as base code or start from
scratch.

## Requirements

To build and run this project, you need the following:
   - The project uses **Java 17** features, so you'll need JDK 17 or a compatible version.
   - The project uses **Apache Maven** for building and managing dependencies.
   - The project uses **JUnit 5** for unit testing. The required JUnit dependencies are managed through Maven and will be downloaded automatically when you build the project.
   - The project uses **Apache Commons CLI** for command-line parsing. This library is included as a Maven dependency and will be downloaded automatically during the build.

These requirements ensure that the project will build, run, and be tested correctly using the tools and libraries specified in `pom.xml`.

## Build the Project Using Maven
Navigate to the root directory (where the `pom.xml` file is located), and run the following command to build the project:
```bash
mvn clean install
```
This will:
- Download all dependencies listed in the `pom.xml`.
- Compile the Java source files.
- Run any tests located in the `src/test` directory.

---

## Generate Data and Workloads

Before running the benchmarks or examples, you will need to generate the datasets and workloads. We provide sample data in `src/main/resources/` folder.

### Generate Data
The `gen_data.py` script generates data files. The command-line arguments specify the number of rows, the number of dimensions per value, and the output folder. Example:

```bash
python tools/gen_data.py <rows> <dim_per_value> <output_folder>
```

Example:
```bash
python tools/gen_data.py 100 2 src/main/resources/
```
This generates a data file in the `src/main/resources/` folder, where each row will have 2 dimensions per value.

### Generate Workloads
The `gen_workload.py` script generates workloads. Example:

```bash
python tools/gen_workload.py <rows> <dim_per_value> <max_key> <output_folder>
```

Example:
```bash
python tools/gen_workload.py 100 2 200 src/main/resources/
```
This generates a workload file in the `src/main/resources/` folder.


## Example Java Programs to use DB

To run the `SimpleBenchmark` program with a generated workload file, use:

```bash
mvn exec:java -Dexec.mainClass="com.COSI_167A.LSMTreeDataStore.SimpleBenchmark" -Dexec.args="test_db -w src/main/resources/test_25_2_200.wl"
```

This runs the benchmark located in `SimpleBenchmark.java`, using the workload file `test_25_2_200.wl`. You can replace `test_db` with any name you choose for the database file.


## Run Tests

The project also contains unit tests located in the `src/test/java` folder, such as `DBTest.java` and `PersistenceTest.java`. To run all the tests, use the following Maven command:

```bash
mvn test
```

---

> **These tests are only for checking the current codebase functionality. Once you add more implementation, you will need to add more tests for your codebase.**


## Contact

If you have any questions please feel free to see Subhadeep in office hours, or
email me at subhadeep@brandeis.edu.
