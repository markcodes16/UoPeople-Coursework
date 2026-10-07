package vehicle.interfaces;

public interface MotorVehicle extends Vehicle {
   /**
    * @param wheels number of wheels (must be positive)
    */
   void setNumberOfWheels(int wheels);


   /**
    * @return number of wheels
    */
   int getNumberOfWheels();


   /**
    * @param type motorcycle type: Sport, Cruiser, or Off-Road
    */
   void setMotorcycleType(String type);


   /**
    * @return motorcycle type
    */
   String getMotorcycleType();
}
