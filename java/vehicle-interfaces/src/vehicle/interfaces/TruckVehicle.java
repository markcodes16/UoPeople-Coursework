package vehicle.interfaces;

public interface TruckVehicle extends Vehicle {
   /**
    * @param tons cargo capacity in tons (must be >= 0)
    */
   void setCargoCapacity(double tons);


   /**
    * @return cargo capacity in tons
    */
   double getCargoCapacity();


   /**
    * @param transmission transmission type: Manual or Automatic
    */
   void setTransmissionType(String transmission);


   /**
    * @return transmission type
    */
   String getTransmissionType();
}
