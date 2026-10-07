import java.util.Scanner;

public class quiz {


   public static void main(String[] args) {


       // defining variables


       char[] quizAnswers = {'D', 'C', 'A', 'C', 'A'};


       int score = 0;




       Scanner scan = new Scanner(System.in);


       // displaying start of quiz
       System.out.println("Welcome to the quiz!");


       System.out.println("Enter A B C or D to answer each question.");


       // displaying quiz questions


       for (int i = 0; i < 5; i++) {
           switch (i) {
               case 0:
                   System.out.println("What is 1 + 1");
                   System.out.println("A.) 0");
                   System.out.println("B). -2");
                   System.out.println("C). 3");
                   System.out.println("D.) 2");
                   break;
               case 1:
                   System.out.println("What is 1 * 1");
                   System.out.println("A.) 100");
                   System.out.println("B.) 10");
                   System.out.println("C.) 1");
                   System.out.println("D.) 2");
                   break;
               case 2:
                   System.out.println("What is 9 * 9");
                   System.out.println("A). 81");
                   System.out.println("B). -9");
                   System.out.println("C.) undefined");
                   System.out.println("D.) 100");
                   break;
               case 3:
                   System.out.println("Select a number that is greater than 10");
                   System.out.println("A). -10");
                   System.out.println("B). -11");
                   System.out.println("C.) 11");
                   System.out.println("D.) 1");
                   break;
               case 4:
                   System.out.println("If sally has 4 apples and jimmy came over and ate 3 of sally's apples, how many apples does sally have");
                   System.out.println("A). 1");
                   System.out.println("B). 4 because sally stole 3 of jimmy's apples while jimmy was sleeping");
                   System.out.println("C.) 6");
                   System.out.println("D.) 2");
                   break;
           }
           System.out.println("Please enter A , B , C , or D for your answer...");


           // allowing all casing types to be an inputs


           String input = scan.nextLine().toUpperCase();


           // checking if answers/inputs are not only valid but also checking if they are correct
           // displaying correct answer if incorrect


           if (input.length() == 1) {
               char answer = input.charAt(0);
               if (answer == 'A' || answer == 'B' || answer == 'C' || answer == 'D') {
                   if (answer == quizAnswers[i]) {
                       score++;
                   }
                   else {
                       System.out.println("Incorrect! The correct choice is " + quizAnswers[i]);
                   }
               } else {
                   System.out.println("invalid input");
               }
               System.out.println();






           }
           // calculating score in percentage and displaying the answer
       }
       double percentage = (score / 5.0) * 100;
       System.out.println(percentage + "%");


   }
}
