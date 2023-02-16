package org.company;

public class Main {
    public static void main(String[] args) {
        try {
            TripManager tripManager;
            if (args.length == 0)
                tripManager = new TripManager();
            else if (args.length == 1)
                tripManager = new TripManager(args[0], args[0].replace(".csv", "_output.csv"));
            else tripManager = new TripManager(args[0], args[1]);

            tripManager.processTrips();
        } catch (RuntimeException re) {
            System.out.println("An error has occurred: ");
            System.out.println(re);
            System.out.println("at "+re.getStackTrace()[0].toString());
        }
    }
}