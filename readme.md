# Pre-requisites
* Java  8 or higher
* JUnit 5
* Maven

# How to run the code

* `mvn clean install -DskipTests` - This will create a jar file `TripManagement-1.0.jar` in the `target` folder.
* `java -jar target/TripManagement-1.0.jar <input file> <output file>` - This will execute the jar file passing the input file as the command line argument

If no input or output files are provided, the program will use `src/main/resources/taps1.csv` as a default input file and `src/main/resources/trips1.csv` as the default output file.

If only the input file is provided, the program will create an output file based on the input file name (e.g. taps1_output.csv)

# How to execute the unit tests

`mvn clean test` will execute the unit test cases.

# General Information
The application was designed to be able to accept different data providers and data formats in the future.

(For example, a change request to receive the list of Taps in JSON via an API call and return the results in the same way).

DataSourceService is the interface that defines read/write operations. FileService is a class that implements this interface, reading and writing to text files.

ParserService is the interface that defines parse/format operations. CsvParser is a class that implements this interface, parsing and formatting data in CSV format.
- Note: the CsvParser class also contains logic specific to build and inspect the Tap and Trip objects. Ideally, I'd prefer to implement this on the entity classes
  themselves, and call them via reflection. Due to time constraint, I was not able to code this way.

The main processing of the application  happens on the TripManager class, following these steps:
- import the taps data (read and parse)
- match the taps and create trips
- export the trips (format and write)

The Main class is the start point in the application. Its test class (MainTest) contains methods to test 4 sample files (included on the resources folder).

# A few considerations
- The taps/trips resets every day. This means that an ON today can only match an OFF today.
- An OFF without a matching ON is considered a 'lost record' and printed on the console, e.g. `Record ID 8 could not be matched`
- The Bus ID was not considered in the matching logic. This means that an ON on Bus3 will match an OFF on Bus5. I consider this important and essential, but I did not have time to implement it.
- Ideally, the classes should be packaged in groups, like business, objects, etc, but I kept them all together for simplicity.

# Final notes
I hope I covered the requirements. I may have gone over the board with a few things and missed others.
Nonetheless, I had a lot of fun working on it and appreciate the opportunity :)