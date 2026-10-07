package vehicle.model;

import vehicle.interfaces.CarVehicle;

public class Car implements CarVehicle {
   private String make;
   private String model;
   private int year;
   private int numberOfDoors;
   private String fuelType;


   public Car(String make, String model, int year) {
       this.make = make;
       this.model = model;
       this.year = year;
   }


   @Override
   public String getMake() {
       return make;
   }


   @Override
   public String getModel() {
       return model;
   }


   @Override
   public int getYear() {
       return year;
   }


   @Override
   public void setNumberOfDoors(int doors) {
       if (doors <= 0) {
           throw new IllegalArgumentException("Number of doors must be positive.");
       }
       this.numberOfDoors = doors;
   }


   @Override
   public int getNumberOfDoors() {
       return numberOfDoors;
   }


   @Override
   public void setFuelType(String fuelType) {
       if (!fuelType.equalsIgnoreCase("Petrol") &&
               !fuelType.equalsIgnoreCase("Diesel") &&
               !fuelType.equalsIgnoreCase("Electric")) {
           throw new IllegalArgumentException("Invalid fuel type.");
       }
       this.fuelType = fuelType;
   }


   @Override
   public String getFuelType() {
       return fuelType;
   }
}
