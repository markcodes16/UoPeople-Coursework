package com.clock;

public class TimeUpdater implements Runnable {
  
   private final Clock clock;
  
   /**
    * Creates a new TimeUpdater for the specified clock.
    *
    * @param clock the Clock instance to update
    */
   public TimeUpdater(Clock clock) {
       this.clock = clock;
   }
  
   /**
    * Continuously updates the clock's time state while the clock is running.
    * Updates occur every 500ms. Handles exceptions gracefully to ensure
    * continuous operation.
    */
   @Override
   public void run() {
       while (clock.isRunning()) {
           try {
               clock.updateTime();
               Thread.sleep(500);
           } catch (InterruptedException e) {
               // Thread was interrupted, but continue running if clock is still active
               Thread.currentThread().interrupt();
           } catch (Exception e) {
               // Handle any other exceptions gracefully and continue operation
               System.err.println("Error in TimeUpdater: " + e.getMessage());
           }
       }
   }
}
