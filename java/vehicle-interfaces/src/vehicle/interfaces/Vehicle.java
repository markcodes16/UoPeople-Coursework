package vehicle.interfaces;

public interface Vehicle {
   /**
    * @return the make of the vehicle (e.g., Toyota)
    */
   String getMake();


   /**
    * @return the model of the vehicle (e.g., Camry)
    */
   String getModel();


   /**
    * @return the year of manufacture
    */
   int getYear();
}
