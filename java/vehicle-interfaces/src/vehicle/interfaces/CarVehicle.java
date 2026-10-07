package vehicle.interfaces;

public interface CarVehicle extends Vehicle {
   /**
    * @param doors number of doors (must be positive)
    */
   void setNumberOfDoors(int doors);


   /**
    * @return number of doors
    */
   int getNumberOfDoors();


   /**
    * @param fuelType fuel type: Petrol, Diesel, or Electric
    */
   void setFuelType(String fuelType);


   /**
    * @return the fuel type of the car
    */
   String getFuelType();
}
