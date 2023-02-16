package org.company;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @BeforeAll
    static void setUp() {
    }

    @Test
    void mainOriginalSample() {
        try {
            String taps = "src/test/resources/taps1.csv";
            String actual = "src/test/resources/taps1_output.csv";
            String expected = "src/test/resources/trips1.csv";

            TripManager tm = new TripManager(taps, actual);
            tm.processTrips();
            assertEquals(-1, Files.mismatch(Paths.get(expected), Paths.get(actual)));
            Files.deleteIfExists(new File(actual).toPath());
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Test
    void mainComplexOrderedTimeline() {
        try {
            String taps = "src/test/resources/taps2.csv";
            String actual = "src/test/resources/taps2_output.csv";
            String expected = "src/test/resources/trips2.csv";

            TripManager tm = new TripManager(taps, actual);
            tm.processTrips();
            assertEquals(-1, Files.mismatch(Paths.get(expected), Paths.get(actual)));
            Files.deleteIfExists(new File(actual).toPath());
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Test
    void mainComplexNotOrderedTimeline() {
        try {
            String taps = "src/test/resources/taps3.csv";
            String actual = "src/test/resources/taps3_output.csv";
            String expected = "src/test/resources/trips3.csv";

            TripManager tm = new TripManager(taps, actual);
            tm.processTrips();
            assertEquals(-1, Files.mismatch(Paths.get(expected), Paths.get(actual)));
            Files.deleteIfExists(new File(actual).toPath());
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Test
    void mainComplexNotOrderedTimelineMultiplePan() {
        try {
            String taps = "src/test/resources/taps4.csv";
            String actual = "src/test/resources/taps4_output.csv";
            String expected = "src/test/resources/trips4.csv";

            TripManager tm = new TripManager(taps, actual);
            tm.processTrips();
            assertEquals(-1, Files.mismatch(Paths.get(expected), Paths.get(actual)));
            Files.deleteIfExists(new File(actual).toPath());
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }
}