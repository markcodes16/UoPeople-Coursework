package com.clock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Clock {
   private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
  
   private LocalDateTime currentTime;
   private volatile boolean running;
   private Thread updateThread;
   private Thread displayThread;
  
   /**
    * Creates a new Clock instance with the current system time.
    */
   public Clock() {
       this.currentTime = LocalDateTime.now();
       this.running = false;
   }
  
   /**
    * Updates the internal time state to the current system time.
    * This method is thread-safe and synchronized.
    */
   public synchronized void updateTime() {
       this.currentTime = LocalDateTime.now();
   }
  
   /**
    * Returns the current time formatted as "HH:mm:ss dd-MM-yyyy".
    * This method is thread-safe and synchronized.
    *
    * @return formatted time string
    */
   public synchronized String getFormattedTime() {
       return currentTime.format(FORMATTER);
   }
  
   /**
    * Returns the running state of the clock.
    *
    * @return true if the clock is running, false otherwise
    */
   public boolean isRunning() {
       return running;
   }
  
   /**
    * Sets the running state of the clock.
    *
    * @param running the new running state
    */
   public void setRunning(boolean running) {
       this.running = running;
   }
  
   /**
    * Starts the clock by creating and starting the update and display threads.
    * The update thread has lower priority (NORM_PRIORITY - 1) and the display
    * thread has higher priority (NORM_PRIORITY + 1).
    * Both threads are set as daemon threads so they will terminate when the main thread exits.
    */
   public void start() {
       this.running = true;
      
       // Create Update Thread with TimeUpdater
       TimeUpdater timeUpdater = new TimeUpdater(this);
       updateThread = new Thread(timeUpdater);
       updateThread.setName("Clock-Updater");
       updateThread.setPriority(Thread.NORM_PRIORITY - 1);
       updateThread.setDaemon(true);  // Allow JVM to exit even if this thread is running
      
       // Create Display Thread with TimeDisplayer
       TimeDisplayer timeDisplayer = new TimeDisplayer(this);
       displayThread = new Thread(timeDisplayer);
       displayThread.setName("Clock-Display");
       displayThread.setPriority(Thread.NORM_PRIORITY + 1);
       displayThread.setDaemon(true);  // Allow JVM to exit even if this thread is running
      
       // Start both threads
       updateThread.start();
       displayThread.start();
   }
  
   /**
    * Stops the clock by setting the running flag to false and waiting for
    * both threads to complete gracefully.
    */
   public void stop() {
       this.running = false;
      
       try {
           // Wait for threads to complete gracefully
           if (updateThread != null) {
               updateThread.join();
           }
           if (displayThread != null) {
               displayThread.join();
           }
       } catch (InterruptedException e) {
           Thread.currentThread().interrupt();
       }
   }
  
   /**
    * Returns the update thread.
    *
    * @return the update thread
    */
   public Thread getUpdateThread() {
       return updateThread;
   }
  
   /**
    * Returns the display thread.
    *
    * @return the display thread
    */
   public Thread getDisplayThread() {
       return displayThread;
   }
}
