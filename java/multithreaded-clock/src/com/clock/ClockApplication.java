package com.clock;

public class ClockApplication {
   public static void main(String[] args) {
       // Instantiate Clock object
       Clock clock = new Clock();
      
       // Add shutdown hook for graceful termination
       Runtime.getRuntime().addShutdownHook(new Thread(() -> {
           System.out.println("\nShutting down clock system...");
           clock.stop();
           System.out.println("Clock system stopped gracefully.");
       }));
      
       // Call start() method to begin clock operation
       System.out.println("Starting clock system...");
       clock.start();
       System.out.println("Clock system started. Press Ctrl+C to stop.");
      
       // Keep the main thread alive while the clock is running
       try {
           while (clock.isRunning()) {
               Thread.sleep(1000);
           }
       } catch (InterruptedException e) {
           Thread.currentThread().interrupt();
       }
   }
}
