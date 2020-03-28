package com.company;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	ParkingLot floor1 = new ParkingLot("floor 1", 10,10);
        List<ParkingLot.Spot> spotCopy = new ArrayList<>(floor1.spots);
        printList(spotCopy);
        System.out.println(spotCopy.size());
        ParkingLot.Vehicle car1 = new ParkingLot.Cars();
        ParkingLot.Vehicle car2 = new ParkingLot.Cars();
        ParkingLot.Vehicle bike1 = new ParkingLot.Motorbikes();
        ParkingLot.Vehicle bike2 = new ParkingLot.Motorbikes();
        ParkingLot.Vehicle bus1 = new ParkingLot.Buses();
        ParkingLot.Vehicle bus2 = new ParkingLot.Buses();
      // ParkingLot.SpotSize car1Spot= car1.findWhereVehicleCanPark();

       List<ParkingLot.SpotSize> suitableAndAvailableSpots = floor1.whereVehicleShouldGo(car1);
        System.out.println("here are the spots available for you: "+suitableAndAvailableSpots);
        //spot J07 is reserved
       floor1.reserveSpot("J07",car1);
       //J07 cannot be reserved twice
       floor1.reserveSpot("J07",car2);
       // suitableAndAvailableSpots = bike1.whereVehicleShouldGo();
        suitableAndAvailableSpots = floor1.whereVehicleShouldGo(bike1);
       // System.out.println("here are the spots available for bike: "+suitableAndAvailableSpots);
        floor1.reserveSpot("K04",bike1);
        suitableAndAvailableSpots = floor1.whereVehicleShouldGo(bike2);
      //  System.out.println("here are the spots available for bike: "+suitableAndAvailableSpots);
        floor1.reserveSpot("K04",bike2);
        suitableAndAvailableSpots = floor1.whereVehicleShouldGo(bus1);
       System.out.println("here are the spots available for bus: "+suitableAndAvailableSpots);
        floor1.reserveSpot("L07",bus1);
        suitableAndAvailableSpots = floor1.whereVehicleShouldGo(bus2);
        System.out.println("here are the spots available for bus: "+suitableAndAvailableSpots);
        //reserves spot L07 and L08 because buses require two consecutive big_spots
        floor1.reserveSpot("L07",bus2);

    }
    /**
     * prints out the total list of spots
     * */
    public static void printList(Collection<ParkingLot.Spot> list){
        for(ParkingLot.Spot spot : list){
            System.out.println(spot.getSpotNumber() + " "+spot.getSize());
        }
        System.out.println();
        System.out.println("====================================================");
    }

}
