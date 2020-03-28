package com.company;

import java.util.*;
/***
 * author: YAHYA SEKINAT OLUWAKUNMI
 * */
public class ParkingLot {
    private final String floorName;
    public static List<Spot> spots = new LinkedList<>();

    public enum SpotSize {
        BIG_SPOT,
        SMALL_SPOT
    }

    public ParkingLot(String floorName, int numRows, int spotsPerRow) {
        this.floorName = floorName;
        int lastRow = 'A' + (numRows + 1);
        for (char row = 'A'; row <= lastRow; row++) {
            for (int spotNum = 1; spotNum <= spotsPerRow; spotNum++) {

                Spot spot = new Spot(row + String.format("%02d", spotNum), getRandom());
                spots.add(spot);
            }
        }
    }

    public static SpotSize getRandom() {
        return SpotSize.values()[(int) (Math.random() * SpotSize.values().length)];
    }

    public String getFloorName() {
        return floorName;
    }

    //    public List whereVehicleShouldGo(Vehicle vehicle) {
//        List<SpotSize> availableSpots = new ArrayList<>();
//
//        SpotSize spaceVehicleNeeds = vehicle.findWhereVehicleCanPark();
//
//        for (Spot spot : spots) {
//
//            /**loop through spots to find big_spot for car
//             * */
//            if (spot.getSize().equals(spaceVehicleNeeds)&&spot.reserved==false) {
//                availableSpots.add(spot.getSize());
//            }
//        }
//        return availableSpots;
//    }

    /**returns a list of suitable available spots where a vehicle can park
     * */



public List whereVehicleShouldGo(Vehicle vehicle) {
        String typeOfVehicle = vehicle.getName();
        SpotSize spaceVehicleNeeds = null;
        List<SpotSize> availableSpots = new LinkedList<>();
        if (typeOfVehicle.equals("car")) {
            spaceVehicleNeeds = SpotSize.BIG_SPOT;
            for (Spot spot : spots) {
                /**loop through spots to find big_spot for car*/
                if (spot.getSize().equals(spaceVehicleNeeds) && spot.reserved == false) {
                    availableSpots.add(spot.getSize());
                }
            }
        } else if (typeOfVehicle.equals("motorbike")) {
            for (Spot spot : ParkingLot.spots) {
                //System.out.print(spot.getSize()+" ");
                if (!spot.reserved) {
                    availableSpots.add(spot.getSize());
                }
            }
        } else if (typeOfVehicle.equals("bus")) {
            spaceVehicleNeeds = SpotSize.BIG_SPOT;
            List<SpotSize> spotsSizeCopy = new LinkedList<>();
            for (Spot s : spots) {
                spotsSizeCopy.add(s.getSize());
            }
            ListIterator<SpotSize> it = spotsSizeCopy.listIterator();
            SpotSize previous = null;
            if (it.hasNext()) {
                previous = it.next();
            }
            //System.out.println("this is the value of prev: "+previous);
            while (it.hasNext()) {
                SpotSize current = it.next();
                // Process previous and current here.
                int comparison = previous.compareTo(current);
                if (comparison == 0 && previous == SpotSize.BIG_SPOT) {
                    availableSpots.add(previous);
                }
                // End of loop, after processing.  Maintain previous reference.
                previous = current;

            }
        }

        return availableSpots;
    }

/**returns true after a spot is reserved
 * */
    public boolean reserveSpot(String spotNum, Vehicle vehicle) {
        //SpotSize spaceVehicleNeeds = vehicle.findWhereVehicleCanPark();
        boolean isReserve = false;
        List<SpotSize> spaceVehicleNeeds = vehicle.findWhereVehicleCanPark();
        if (spaceVehicleNeeds.size() < 2) {
            Spot requestedSpot = new Spot(spotNum, spaceVehicleNeeds.get(0));
            int foundSpot = Collections.binarySearch(spots, requestedSpot, null);
            if (foundSpot >= 0 && whereVehicleShouldGo(vehicle).contains(requestedSpot.getSize())) {
                isReserve = spots.get(foundSpot).reserve();
            } else {
                System.out.println("spot: " + spotNum + " is taken or inexistent");
                // return false;
            }
        } else if (spaceVehicleNeeds.size() == 2 && spaceVehicleNeeds.get(0) != spaceVehicleNeeds.get(1)) {
            SpotSize s1 = spaceVehicleNeeds.get(0);
            SpotSize s2 = spaceVehicleNeeds.get(1);
            switch (s1) {
                case BIG_SPOT:
                    Spot requestedSpot = new Spot(spotNum, SpotSize.BIG_SPOT);
                    int foundSpot = Collections.binarySearch(spots, requestedSpot, null);
                    if (foundSpot >= 0 && whereVehicleShouldGo(vehicle).contains(requestedSpot.getSize())) {
                        isReserve = spots.get(foundSpot).reserve();
                    } else {
                        System.out.println("spot: " + spotNum + " is taken or inexistent");
                        // return false;
                    }
                    break;
                case SMALL_SPOT:
                    requestedSpot = new Spot(spotNum, SpotSize.SMALL_SPOT);
                    foundSpot = Collections.binarySearch(spots, requestedSpot, null);
                    if (foundSpot >= 0 && whereVehicleShouldGo(vehicle).contains(requestedSpot.getSize())) {
                        isReserve = spots.get(foundSpot).reserve();
                    } else {
                        System.out.println("spot: " + spotNum + " is taken or inexistent");
                        // return false;
                    }
                    break;
                default:
                    return false;
            }

        } else if (spaceVehicleNeeds.size() == 2 && spaceVehicleNeeds.get(0) == spaceVehicleNeeds.get(1)) {
            Spot requestedSpot = new Spot(spotNum, SpotSize.BIG_SPOT);
            int foundSpot = Collections.binarySearch(spots, requestedSpot, null);
            if (foundSpot >= 0 && whereVehicleShouldGo(vehicle).contains(requestedSpot.getSize())) {
                isReserve = spots.get(foundSpot).reserve() && spots.get(foundSpot + 1).reserve();

            } else {
                System.out.println("spot: " + spotNum + "and the one after it are taken or inexistent");
                // return false;
            }
        }

        return isReserve;
    }


    public class Spot implements Comparable<Spot> {
        public final String spotNumber;
        private boolean reserved = false;
        /**
         * randomise enum elements
         */
        private SpotSize size;


        public Spot(String spotNumber, SpotSize size) {
            this.spotNumber = spotNumber;
            this.size = size;
        }

        public String getSpotNumber() {
            return spotNumber;
        }

        public SpotSize getSize() {
            return size;
        }

        @Override
        public int compareTo(Spot spot) {
            return this.spotNumber.compareToIgnoreCase(spot.getSpotNumber());
        }

        public boolean reserve() {
            if (!this.reserved) {
                this.reserved = true;
                System.out.println("spot " + spotNumber + " is reserved");
                return true;
            } else {
                System.out.println("you cannot reserve this spot, its taken");
                return false;
            }
        }
    }

    public static abstract class Vehicle {
        public List<SpotSize> whereVehicleCanPark = new LinkedList<>();
        //public SpotSize whereVehicleCanPark;
        public String name;
        public int wheels;

        public Vehicle(String name, int wheels) {
            this.name = name;
            this.wheels = wheels;
        }

        public abstract String checkVehicleType();

        public List whereVehicleShouldGo() {
            List<SpotSize> list1 = new LinkedList<>();
            for (Spot spot : ParkingLot.spots) {
                //System.out.print(spot.getSize()+" ");
                if (!spot.reserved) {
                    list1.add(spot.getSize());

                }

            }
            System.out.println(list1.size());
            return list1;

        }

/**returns a list of suitable spot sizes for each vehicle type
 * cars = big_spot ==> list contains just one element
 * buses = big_spot ==> list contains  two elements
 * motorbikes = big_spot and small_spot ==> list contains two elements
 * */
        public List findWhereVehicleCanPark() {
            String type = this.checkVehicleType();
            SpotSize foundSpot = null;
            List<SpotSize> list1 = new LinkedList<>();
            switch (type) {
                case "car":
                    whereVehicleCanPark.add(SpotSize.BIG_SPOT);
                    //foundSpot = whereVehicleCanPark.get(0);
                    break;
                case "motorbike":
                    whereVehicleCanPark.add(SpotSize.BIG_SPOT);
                    whereVehicleCanPark.add(SpotSize.SMALL_SPOT);
                    //  System.out.println("inside findWhereVehicleCanPark: " +whereVehicleCanPark);
                    break;
                case "bus":

                    whereVehicleCanPark.add(SpotSize.BIG_SPOT);
                    whereVehicleCanPark.add(SpotSize.BIG_SPOT);
                    // System.out.println("inside findWhereVehicleCanPark(BUS): " +whereVehicleCanPark);

                    break;
                default:
                    foundSpot = SpotSize.BIG_SPOT;
            }
            return whereVehicleCanPark;
        }

        public String getName() {
            return name;
        }

        public int getWheels() {
            return wheels;
        }
    }

    public static class Cars extends Vehicle {
        public Cars() {
            super("car", 4);
        }

        @Override
        public String checkVehicleType() {
            if (this.getName().equals("car")) {
                return "car";
            } else {
                return "not a car";
            }

        }
    }

    public static class Motorbikes extends Vehicle {
        public Motorbikes() {
            super("motorbike", 2);

        }

        @Override
        public String checkVehicleType() {
            if (this.getName().equals("motorbike")) {
                return "motorbike";
            } else {
                return "not a motorbike";
            }
        }
    }

    public static class Buses extends Vehicle {
        public Buses() {
            super("bus", 8);
        }

        @Override
        public String checkVehicleType() {
            if (this.getName().equals("bus")) {
                return "bus";
            } else {
                return "not a bus";
            }
        }
    }


}
