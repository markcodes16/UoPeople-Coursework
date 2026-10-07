package vehicle.app;

import vehicle.interfaces.Vehicle;
import vehicle.interfaces.CarVehicle;
import vehicle.interfaces.MotorVehicle;
import vehicle.interfaces.TruckVehicle;
import vehicle.model.Car;
import vehicle.model.Motorcycle;
import vehicle.model.Truck;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VehicleApp {
   private static final Scanner scanner = new Scanner(System.in);
   private static final List<Vehicle> fleet = new ArrayList<>();


   public static void main(String[] args) {
       boolean running = true;
       while (running) {
           System.out.println("\n--- Vehicle Management ---");
           System.out.println("1. Create Car");
           System.out.println("2. Create Motorcycle");
           System.out.println("3. Create Truck");
           System.out.println("4. View Fleet Summary");
           System.out.println("5. Exit");
           System.out.print("Select an option: ");


           int choice = Integer.parseInt(scanner.nextLine());
           switch (choice) {
               case 1 -> createCar();
               case 2 -> createMotorcycle();
               case 3 -> createTruck();
               case 4 -> displaySummary();
               case 5 -> {
                   System.out.println("Exiting. Goodbye!");
                   running = false;
               }
               default -> System.out.println("Invalid option. Try again.");
           }
       }
       scanner.close();
   }


   private static void createCar() {
       System.out.print("Enter make: ");
       String make = scanner.nextLine();
       System.out.print("Enter model: ");
       String model = scanner.nextLine();
       System.out.print("Enter year: ");
       int year = Integer.parseInt(scanner.nextLine());
       CarVehicle car = new Car(make, model, year);


       car.setNumberOfDoors(promptInt("number of doors"));
       car.setFuelType(promptEnum("fuel type (Petrol, Diesel, Electric)", new String[]{"Petrol","Diesel","Electric"}));


       fleet.add(car);
       System.out.println("Car added to fleet.");
   }


   private static void createMotorcycle() {
       System.out.print("Enter make: ");
       String make = scanner.nextLine();
       System.out.print("Enter model: ");
       String model = scanner.nextLine();
       System.out.print("Enter year: ");
       int year = Integer.parseInt(scanner.nextLine());
       MotorVehicle bike = new Motorcycle(make, model, year);


       bike.setNumberOfWheels(promptInt("number of wheels"));
       bike.setMotorcycleType(promptEnum("motorcycle type (Sport, Cruiser, Off-Road)", new String[]{"Sport","Cruiser","Off-Road"}));


       fleet.add(bike);
       System.out.println("Motorcycle added to fleet.");
   }


   private static void createTruck() {
       System.out.print("Enter make: ");
       String make = scanner.nextLine();
       System.out.print("Enter model: ");
       String model = scanner.nextLine();
       System.out.print("Enter year: ");
       int year = Integer.parseInt(scanner.nextLine());
       TruckVehicle truck = new Truck(make, model, year);


       truck.setCargoCapacity(promptDouble("cargo capacity in tons"));
       truck.setTransmissionType(promptEnum("transmission type (Manual, Automatic)", new String[]{"Manual","Automatic"}));


       fleet.add(truck);
       System.out.println("Truck added to fleet.");
   }


   private static int promptInt(String field) {
       while (true) {
           try {
               System.out.print("Enter " + field + ": ");
               int val = Integer.parseInt(scanner.nextLine());
               if (val < 0) throw new NumberFormatException();
               return val;
           } catch (NumberFormatException e) {
               System.out.println("Invalid input. Please enter a non-negative integer.");
           }
       }
   }


   private static double promptDouble(String field) {
       while (true) {
           try {
               System.out.print("Enter " + field + ": ");
               double val = Double.parseDouble(scanner.nextLine());
               if (val < 0) throw new NumberFormatException();
               return val;
           } catch (NumberFormatException e) {
               System.out.println("Invalid input. Please enter a non-negative number.");
           }
       }
   }


   private static String promptEnum(String prompt, String[] valid) {
       while (true) {
           System.out.print("Enter " + prompt + ": ");
           String input = scanner.nextLine();
           for (String option : valid) {
               if (option.equalsIgnoreCase(input)) {
                   return option;
               }
           }
           System.out.println("Invalid option. Valid values: " + String.join(", ", valid));
       }
   }


   private static void displaySummary() {
       if (fleet.isEmpty()) {
           System.out.println("No vehicles in fleet.");
           return;
       }
       System.out.println("\n--- Fleet Summary ---");
       for (Vehicle v : fleet) {
           System.out.println("Make: " + v.getMake() + ", Model: " + v.getModel() + ", Year: " + v.getYear());
           if (v instanceof CarVehicle car) {
               System.out.println("  Doors: " + car.getNumberOfDoors() + ", Fuel: " + car.getFuelType());
           } else if (v instanceof MotorVehicle bike) {
               System.out.println("  Wheels: " + bike.getNumberOfWheels() + ", Type: " + bike.getMotorcycleType());
           } else if (v instanceof TruckVehicle truck) {
               System.out.println("  Capacity: " + truck.getCargoCapacity() + " tons, Transmission: " + truck.getTransmissionType());
           }
       }
   }
}
