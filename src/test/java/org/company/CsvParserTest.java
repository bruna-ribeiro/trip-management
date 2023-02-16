package org.company;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvParserTest {
    private static CsvParser cp;
    private final static String HEADER_OUTPUT = "Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status";

    @BeforeAll
    static void setUp() {
        cp  = new CsvParser();
    }

    @AfterAll
    static void tearDown() {
    }

    @Test
    void parseNormalDataWithHeader() {
        try {
            StringBuffer text = new StringBuffer("ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN");
            text.append(System.lineSeparator());
            text.append("1, 22-01-2018 15:00:00, ON, Stop3, Company1, Bus37, 5500005555555559");
            text.append(System.lineSeparator());
            text.append("2, 22-01-2018 16:05:00, OFF, Stop1, Company1, Bus37, 5500005555555559");
            InputStream sampleData = new ByteArrayInputStream(text.toString().getBytes());
            List<Tap> expectedList = new ArrayList<>();
            expectedList.add(new Tap("1", LocalDateTime.parse("22-01-2018 15:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), "ON", "Stop3", "Company1", "Bus37", "5500005555555559"));
            expectedList.add(new Tap("2", LocalDateTime.parse("22-01-2018 16:05:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), "OFF", "Stop1", "Company1", "Bus37", "5500005555555559"));

            List<Tap> list = cp.parse(sampleData, Tap.class, new Boolean[]{true});
            assertEquals(expectedList, list);
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Test
    void parseNormalDataNoHeader() {
        try {
            StringBuffer text = new StringBuffer("1, 22-01-2018 15:00:00, ON, Stop3, Company1, Bus37, 5500005555555559");
            text.append(System.lineSeparator());
            text.append("2, 22-01-2018 16:05:00, OFF, Stop1, Company1, Bus37, 5500005555555559");
            InputStream sampleData = new ByteArrayInputStream(text.toString().getBytes());
            List<Tap> expectedList = new ArrayList<>();
            expectedList.add(new Tap("1", LocalDateTime.parse("22-01-2018 15:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), "ON", "Stop3", "Company1", "Bus37", "5500005555555559"));
            expectedList.add(new Tap("2", LocalDateTime.parse("22-01-2018 16:05:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), "OFF", "Stop1", "Company1", "Bus37", "5500005555555559"));

            List<Tap> list = cp.parse(sampleData, Tap.class, new Boolean[]{false});
            assertEquals(expectedList, list);
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Test
    void parseOneLineCorrupted() {
        try {
            StringBuffer text = new StringBuffer("ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN");
            text.append(System.lineSeparator());
            text.append("1, 22-01-2018 15:00:00, ON, Stop3, Company1, Bus37"); // missing last field
            text.append(System.lineSeparator());
            text.append("2, 22-01-2018 16:05:00, OFF, Stop1, Company1, Bus37, 5500005555555559");
            InputStream sampleData = new ByteArrayInputStream(text.toString().getBytes());
            List<Tap> expectedList = new ArrayList<>();
            expectedList.add(new Tap("2", LocalDateTime.parse("22-01-2018 16:05:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), "OFF", "Stop1", "Company1", "Bus37", "5500005555555559"));

            List<Tap> list = cp.parse(sampleData, Tap.class, new Boolean[]{true});
            assertEquals(expectedList, list);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Test
    void parseNoDataOnlyHeader() {
        try {
            StringBuffer text = new StringBuffer("ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN");
            InputStream sampleData = new ByteArrayInputStream(text.toString().getBytes());
            List<Tap> list = cp.parse(sampleData, Tap.class, new Boolean[]{true});
            assertEquals(0, list.size());
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Test
    void parseEmptyFile() {
        try {
            InputStream sampleData = new ByteArrayInputStream("".getBytes());
            List<Tap> list = cp.parse(sampleData, Tap.class, new Boolean[]{true});
            assertEquals(0, list.size());
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @Test
    void formatNormalData() {
        List<Trip> list = new ArrayList<>();
        list.add(new Trip(LocalDateTime.parse("22-01-2018 15:00:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                LocalDateTime.parse("22-01-2018 16:05:00", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                "Stop1", "Stop2", 3.25, "Company1", "Bus37",
                "5500005555555559", "COMPLETED"));
        StringBuffer expectedOutput = new StringBuffer("Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status");
        expectedOutput.append(System.lineSeparator());
        expectedOutput.append("22-01-2018 15:00:00, 22-01-2018 16:05:00, 3900, Stop1, Stop2, 3.25, Company1, Bus37, 5500005555555559, COMPLETED");
        expectedOutput.append(System.lineSeparator());

        StringBuffer actualOutput = cp.format(list, Trip.class, new String[]{HEADER_OUTPUT});

        assertEquals(expectedOutput.toString(), actualOutput.toString());
    }

    @Test
    void formatEmptyData() {
        List<Trip> list = new ArrayList<>();
        StringBuffer expectedOutput = new StringBuffer("Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status");
        expectedOutput.append(System.lineSeparator());

        StringBuffer actualOutput = cp.format(list, Trip.class, new String[]{HEADER_OUTPUT});

        assertEquals(expectedOutput.toString(), actualOutput.toString()); // only header
    }
}