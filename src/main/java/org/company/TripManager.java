package org.company;

import java.io.*;
import java.util.*;

public class TripManager {
    private final static String DATA_FORMAT = "csv";
    private final static String DATA_SOURCE = "file";
    private final static String DEFAULT_INPUT_FILE = "src/main/resources/taps1.csv";
    private final static String DEFAULT_OUTPUT_FILE = "src/main/resources/trips1.csv";
    private final static String HEADER_OUTPUT = "Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status";
    private static final Map<String, Double> charges = new HashMap<>();
    static {
        charges.put("Stop1,Stop2", 3.25);
        charges.put("Stop2,Stop3", 5.5);
        charges.put("Stop1,Stop3", 7.3);
    }
    private final String inputFilename;
    private final String outputFilename;

    private ParserService parser;
    private DataSourceService dataSource;

    public TripManager() {
        this.inputFilename = DEFAULT_INPUT_FILE;
        this.outputFilename = DEFAULT_OUTPUT_FILE;
    }

    public TripManager(String inputFilename, String outputFilename) {
        this.inputFilename = inputFilename;
        this.outputFilename = outputFilename;
    }

    /**
     * Initial method that will trigger the processing of the taps file
     */
    public void processTrips() {
        parser = ParserFactory.getParser(DATA_FORMAT);
        dataSource = DataSourceFactory.getDataSource(DATA_SOURCE);

        List<Tap> tapList = importTapData();
        List<Trip> tripList = null;
        if (tapList != null && tapList.size() > 0) {
            tripList = matchTaps(tapList);
        }
        if (tripList != null && tripList.size() > 0) {
            exportTripData(tripList);
        }
    }

    /**
     * Format a list of Trips and save the result to a file
     */
    public void exportTripData(List<Trip> tripList) {
        try {
            StringBuffer outputData = parser.format(tripList, Trip.class, new String[]{HEADER_OUTPUT});
            dataSource.write(new Object[]{outputFilename, outputData});
        }
        catch (IOException io) {
            System.out.println("Error exporting file "+inputFilename);
            System.out.println(io);
        }
    }

    /**
     * Reads the taps file, parses it and creates a list of Tap
     */
    public List<Tap> importTapData() {
        try {
            InputStream tapsData = dataSource.read(new Object[] {inputFilename});
            if (tapsData != null) {
                return parser.parse(tapsData, Tap.class, new Boolean[] {true});
            }
        }
        catch (IOException io) {
            System.out.println("Error importing file "+inputFilename);
            System.out.println(io);
        }
        return new ArrayList<>();
    }

    /**
     * Receives a list of Tap, match its records and creates a list of Trip
     */
    public List<Trip> matchTaps(List<Tap> allTapsList) {
        List<Trip> tripList = new ArrayList<>();

        // group the taps by PAN
        Map<String, List<Tap>> tapsMap = new HashMap<>();
        for (Tap tap : allTapsList) {
            if (tapsMap.containsKey(tap.getPan())) {
                tapsMap.get(tap.getPan()).add(tap);
            } else {
                tapsMap.put(tap.getPan(), new ArrayList<>(List.of(tap)));
            }
        }

        // for each PAN in the map
        for (Map.Entry<String, List<Tap>> entry : tapsMap.entrySet()) {
            // order the tap list by date/time, to establish a timeline
            List<Tap> tapList = entry.getValue();
            Collections.sort(tapList, (o1, o2) -> {
                if (o1.getDateTime().isBefore(o2.getDateTime())) return -1;
                else if (o2.getDateTime().isBefore(o1.getDateTime())) return 1;
                else return 0;
            });

            // iterate the tap list to match the records
            int i = 0;
            Tap tap, nextTap;
            while (i < tapList.size()) {
                tap = tapList.get(i);
                if (tap.getTapType().equalsIgnoreCase("ON")) {

                    if (i == tapList.size()-1) {
                        // Scenario: last tap of the list is ON, there is no OFF to match it
                        tripList.add(createTrip(tap, null, "INCOMPLETE"));
                    }
                    else {
                        nextTap = tapList.get(i + 1);
                        if (nextTap.getTapType().equalsIgnoreCase("OFF")) {

                            // Scenario: current tap is ON and the next is OFF, both on the same day
                            if (tap.getDateTime().toLocalDate().equals(nextTap.getDateTime().toLocalDate())) {
                                if (tap.getStopId().equalsIgnoreCase(nextTap.getStopId()))
                                    // ON and OFF same bus stop
                                    tripList.add(createTrip(tap, nextTap, "CANCELLED"));
                                else
                                    // ON and OFF on different bus stops
                                    tripList.add(createTrip(tap, nextTap, "COMPLETED"));
                                i++; // increment the index to skip the next record (the matched OFF)
                            }
                            else {
                                // Scenario: current tap is ON and the next tap is OFF, but not on the same day
                                tripList.add(createTrip(tap, null, "INCOMPLETE"));
                            }
                        }
                        else {
                            // Scenario: current tap is ON and the next is ON
                            tripList.add(createTrip(tap, null, "INCOMPLETE"));
                        }
                    }
                }
                // Scenario: current tap is OFF, which means there is no ON matching it
                else {
                    System.out.println("Record ID "+tap.getId()+" could not be matched");
                }
                i++; // advance to the next position on the list
            }

        }
        return tripList;
    }

    public double calcCharge(String fromStopId, String toStopId) {
        // both parameters are invalid
        if ((fromStopId == null || fromStopId.isBlank()) && (toStopId == null || toStopId.isBlank()))
            return 0;

        // both parameters are valid
        if (fromStopId != null && !fromStopId.isBlank() && toStopId != null && !toStopId.isBlank()) {
            // cancelled trips
            if (fromStopId.equalsIgnoreCase(toStopId)) {
                return 0;
            }
            else { // completed trips
                if (charges.containsKey(fromStopId + "," + toStopId))
                    return charges.get(fromStopId + "," + toStopId);
                if (charges.containsKey(toStopId + "," + fromStopId))
                    return charges.get(toStopId + "," + fromStopId);
                return 0; // if it cannot find it in the map, return 0
            }
        }

        // incomplete trips
        String stopId = (fromStopId == null || fromStopId.isBlank()) ? toStopId : fromStopId;
        double charge = 0;
        for (Map.Entry<String, Double> entry : charges.entrySet()) {
            if (entry.getKey().contains(stopId) && entry.getValue() > charge)
                charge = entry.getValue();
        }
        return charge;
    }

    public Trip createTrip(Tap tap1, Tap tap2, String status) {
        switch (status) {
            case "COMPLETED", "CANCELLED" -> {
                return new Trip(tap1.getDateTime(), tap2.getDateTime(),
                        tap1.getStopId(), tap2.getStopId(),
                        calcCharge(tap1.getStopId(), tap2.getStopId()),
                        tap1.getCompanyId(), tap1.getBusId(),
                        tap1.getPan(), status);
            }
            case "INCOMPLETE" -> {
                return new Trip(tap1.getDateTime(), null,
                        tap1.getStopId(), "N/A", calcCharge(tap1.getStopId(), null), tap1.getCompanyId(), tap1.getBusId(),
                        tap1.getPan(), status);
            }
            default -> {
                return null;
            }
        }
    }
}
