package vehicle.model;

import vehicle.interfaces.MotorVehicle;

public class Motorcycle implements MotorVehicle {
   private String make;
   private String model;
   private int year;
   private int numberOfWheels;
   private String motorcycleType;


   public Motorcycle(String make, String model, int year) {
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
   public void setNumberOfWheels(int wheels) {
       if (wheels <= 0) {
           throw new IllegalArgumentException("Number of wheels must be positive.");
       }
       this.numberOfWheels = wheels;
   }


   @Override
   public int getNumberOfWheels() {
       return numberOfWheels;
   }


   @Override
   public void setMotorcycleType(String type) {
       if (!type.equalsIgnoreCase("Sport") &&
               !type.equalsIgnoreCase("Cruiser") &&
               !type.equalsIgnoreCase("Off-Road")) {
           throw new IllegalArgumentException("Invalid motorcycle type.");
       }
       this.motorcycleType = type;
   }


   @Override
   public String getMotorcycleType() {
       return motorcycleType;
   }
}
