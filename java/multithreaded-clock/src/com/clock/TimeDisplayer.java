package com.clock;

public class TimeDisplayer implements Runnable {
  
   private final Clock clock;
  
   /**
    * Creates a new TimeDisplayer for the specified clock.
    *
    * @param clock the Clock instance to display
    */
   public TimeDisplayer(Clock clock) {
       this.clock = clock;
   }
  
   /**
    * Continuously displays the clock's formatted time to the console while the clock is running.
    * Displays occur every 1000ms (1 second). Handles exceptions gracefully to ensure
    * continuous operation.
    */
   @Override
   public void run() {
       while (clock.isRunning()) {
           try {
               String formattedTime = clock.getFormattedTime();
               System.out.println(formattedTime);
               Thread.sleep(1000);
           } catch (InterruptedException e) {
               // Thread was interrupted, but continue running if clock is still active
               Thread.currentThread().interrupt();
           } catch (Exception e) {
               // Handle any other exceptions gracefully and continue operation
               System.err.println("Error in TimeDisplayer: " + e.getMessage());
           }
       }
   }
}
