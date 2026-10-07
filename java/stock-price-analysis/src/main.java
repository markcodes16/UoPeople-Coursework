import java.util.ArrayList;
import java.util.Arrays;

public class main {
   public static void main(String[] args) {
       // Example: 10 days of opening stock prices (float)
       float[] stockArray = {
               100.5f, 101.2f, 102.3f, 103.8f, 104.1f,
               105.0f, 106.4f, 107.9f, 108.6f, 109.3f
       };


       // Same data as an ArrayList for cumulative sum
       ArrayList<Float> stockList = new ArrayList<>(
               Arrays.asList(
                       100.5f, 101.2f, 102.3f, 103.8f, 104.1f,
                       105.0f, 106.4f, 107.9f, 108.6f, 109.3f
               )
       );


       // 1. Calculate average
       float avgPrice = calculateAveragePrice(stockArray);
       System.out.println("Average stock price: " + avgPrice);


       // 2. Find maximum
       float maxPrice = findMaximumPrice(stockArray);
       System.out.println("Maximum stock price: " + maxPrice);


       // 3. Count occurrences of a specific price (e.g., 104.1f)
       float target = 104.1f;
       int occ = countOccurrences(stockArray, target);
       System.out.println("Occurrences of " + target + ": " + occ);


       // 4. Compute cumulative sum
       ArrayList<Float> cumSum = computeCumulativeSum(stockList);
       System.out.println("Cumulative sums: " + cumSum);
   }


   /**
    * Calculates the average of the given float array.
    */
   public static float calculateAveragePrice(float[] prices) {
       float sum = 0f;
       for (float p : prices) {
           sum += p;
       }
       return sum / prices.length;
   }


   /**
    * Finds the maximum value in the given float array.
    */
   public static float findMaximumPrice(float[] prices) {
       float max = prices[0];
       for (float p : prices) {
           if (p > max) {
               max = p;
           }
       }
       return max;
   }


   /**
    * Counts how many times target appears in the array.
    */
   public static int countOccurrences(float[] prices, float target) {
       int count = 0;
       for (float p : prices) {
           if (p == target) {
               count++;
           }
       }
       return count;
   }


   /**
    * Builds a new ArrayList containing the running (cumulative) sum at each index.
    */
   public static ArrayList<Float> computeCumulativeSum(ArrayList<Float> prices) {
       ArrayList<Float> result = new ArrayList<>();
       float runningSum = 0f;
       for (float p : prices) {
           runningSum += p;
           result.add(runningSum);
       }
       return result;
   }
}
