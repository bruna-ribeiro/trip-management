package org.company;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TripManagerTest {
    private static TripManager tm;

    @BeforeAll
    static void setUp() {
        tm = new TripManager();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void matchTapsCompleted() {
        List<Tap> taps = new ArrayList<>();
        taps.add(new Tap("1", LocalDateTime.parse("22-01-2018 13:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), "ON", "Stop1", "Company1", "Bus37", "5500005555555559"));
        taps.add(new Tap("2", LocalDateTime.parse("22-01-2018 13:05:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), "OFF", "Stop2", "Company1", "Bus37", "5500005555555559"));
        List<Trip> expectedTrips = new ArrayList<>();
        expectedTrips.add(new Trip(LocalDateTime.parse("22-01-2018 13:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                LocalDateTime.parse("22-01-2018 13:05:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                "Stop1", "Stop2", 3.25, "Company1", "Bus37",
                "5500005555555559", "COMPLETED"));

        assertEquals(expectedTrips, tm.matchTaps(taps));
    }

    @Test
    void matchTapsIncomplete() {
        List<Tap> taps = new ArrayList<>();
        taps.add(new Tap("1", LocalDateTime.parse("22-01-2018 13:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), "ON", "Stop1", "Company1", "Bus37", "5500005555555559"));
        List<Trip> expectedTrips = new ArrayList<>();
        expectedTrips.add(new Trip(LocalDateTime.parse("22-01-2018 13:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                null,
                "Stop1", "N/A", 7.3, "Company1", "Bus37",
                "5500005555555559", "INCOMPLETE"));

        assertEquals(expectedTrips, tm.matchTaps(taps));
    }

    @Test
    void matchTapsCancelled() {
        List<Tap> taps = new ArrayList<>();
        taps.add(new Tap("1", LocalDateTime.parse("22-01-2018 13:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), "ON", "Stop1", "Company1", "Bus37", "5500005555555559"));
        taps.add(new Tap("2", LocalDateTime.parse("22-01-2018 13:05:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), "OFF", "Stop1", "Company1", "Bus37", "5500005555555559"));
        List<Trip> expectedTrips = new ArrayList<>();
        expectedTrips.add(new Trip(LocalDateTime.parse("22-01-2018 13:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                LocalDateTime.parse("22-01-2018 13:05:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                "Stop1", "Stop1", 0.0, "Company1", "Bus37",
                "5500005555555559", "CANCELLED"));

        assertEquals(expectedTrips, tm.matchTaps(taps));
    }

    @Test
    void calcChargeTwoStops() {
        assertEquals(3.25, tm.calcCharge("Stop1", "Stop2"));
    }

    @Test
    void calcChargeSameStop() {
        assertEquals(0, tm.calcCharge("Stop2", "Stop2"));
    }

    @Test
    void calcChargeOneStops() {
        assertEquals(7.3, tm.calcCharge("Stop1", ""));
    }

    @Test
    void calcChargeNoStops() {
        assertEquals(0, tm.calcCharge(null, ""));
    }

    @Test
    void createCompleteTrip() {
        Tap tap1 = new Tap("1", LocalDateTime.parse("22-01-2018 15:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                "ON", "Stop1", "Company1", "Bus37", "5500005555555559");
        Tap tap2 = new Tap("2", LocalDateTime.parse("22-01-2018 16:05:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                "OFF", "Stop2", "Company1", "Bus37", "5500005555555559");
        Trip expected = new Trip(LocalDateTime.parse("22-01-2018 15:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                LocalDateTime.parse("22-01-2018 16:05:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                "Stop1", "Stop2", 3.25, "Company1", "Bus37",
                "5500005555555559", "COMPLETED");

        assertEquals(expected, tm.createTrip(tap1, tap2, "COMPLETED"));
    }

    @Test
    void createIncompleteTrip() {
        Tap tap1 = new Tap("1", LocalDateTime.parse("22-01-2018 15:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                "ON", "Stop1", "Company1", "Bus37", "5500005555555559");
        Trip expected = new Trip(LocalDateTime.parse("22-01-2018 15:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),  null,
                "Stop1", "N/A", 7.3, "Company1", "Bus37",
                "5500005555555559", "INCOMPLETE");

        assertEquals(expected, tm.createTrip(tap1, null, "INCOMPLETE"));
    }

    @Test
    void createCancelledTrip() {
        Tap tap1 = new Tap("1", LocalDateTime.parse("22-01-2018 15:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                "ON", "Stop1", "Company1", "Bus37", "5500005555555559");
        Tap tap2 = new Tap("2", LocalDateTime.parse("22-01-2018 16:05:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                "OFF", "Stop1", "Company1", "Bus37", "5500005555555559");
        Trip expected = new Trip(LocalDateTime.parse("22-01-2018 15:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                LocalDateTime.parse("22-01-2018 16:05:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                "Stop1", "Stop1", 0.0, "Company1", "Bus37",
                "5500005555555559", "CANCELLED");

        assertEquals(expected, tm.createTrip(tap1, tap2, "CANCELLED"));
    }
}