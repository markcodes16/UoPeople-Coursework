import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class books {


   /**
    * Inner Book class to represent each book in the library.
    * Holds title, author, quantity available, and number of copies lent out.
    */
   static class Book {
       String title;   // Title of the book
       String author;  // Author of the book
       int quantity;   // Number of copies currently available in library
       int lentOut;    // Number of copies currently lent out


       /**
        * Constructor to initialize a new Book object.
        *
        * @param title    Title of the book
        * @param author   Author of the book
        * @param quantity Initial number of copies added to library
        */
       Book(String title, String author, int quantity) {
           this.title = title;
           this.author = author;
           this.quantity = quantity;
           this.lentOut = 0;
       }
   }


   public static void main(String[] args) {
       // Use ArrayList to store Book objects instead of a map
       List<Book> library = new ArrayList<>();
       Scanner scanner = new Scanner(System.in);


       while (true) {
           // Display the main menu options to the user
           System.out.println("\n=== Library Management System ===");
           System.out.println("1. Add Books");
           System.out.println("2. Borrow Books");
           System.out.println("3. Return Books");
           System.out.println("4. Exit");
           System.out.print("Enter your choice (1-4): ");


           String input = scanner.nextLine();  // Read user choice as string
           int choice;


           try {
               choice = Integer.parseInt(input.trim());  // Convert to integer
           } catch (NumberFormatException e) {
               // Handle invalid number format
               System.out.println("Invalid input. Please enter a number between 1 and 4.");
               continue;  // Restart loop
           }


           switch (choice) {
               case 1:
                   // --- Add Books Section ---
                   System.out.print("Enter book title: ");
                   String addTitle = scanner.nextLine().trim();


                   System.out.print("Enter author: ");
                   String addAuthor = scanner.nextLine().trim();


                   System.out.print("Enter quantity: ");
                   int addQty;
                   try {
                       addQty = Integer.parseInt(scanner.nextLine().trim());
                   } catch (NumberFormatException e) {
                       System.out.println("Invalid quantity. Operation canceled.");
                       break;  // Return to menu
                   }


                   // Check if book already exists by title
                   Book existingBook = null;
                   for (Book b : library) {
                       if (b.title.equalsIgnoreCase(addTitle)) {
                           existingBook = b;
                           break;
                       }
                   }


                   if (existingBook != null) {
                       // Book exists: update quantity (total owned increases)
                       existingBook.quantity += addQty;
                       System.out.println("Updated quantity of '" + existingBook.title + "'. New available quantity: " + existingBook.quantity);
                   } else {
                       // New book: add to library list
                       Book newBook = new Book(addTitle, addAuthor, addQty);
                       library.add(newBook);
                       System.out.println("Added new book: '" + newBook.title + "' by " + newBook.author + ". Quantity: " + newBook.quantity);
                   }
                   break;


               case 2:
                   // --- Borrow Books Section ---
                   // Build list of available books (quantity > 0)
                   List<Book> availableBooks = new ArrayList<>();
                   for (Book b : library) {
                       if (b.quantity > 0) {
                           availableBooks.add(b);
                       }
                   }


                   if (availableBooks.isEmpty()) {
                       // No books available to borrow
                       System.out.println("\nNo books available to borrow at the moment.");
                       break;  // Return to main menu
                   }


                   // Display numbered list of available books
                   System.out.println("\nAvailable books for borrowing:");
                   for (int i = 0; i < availableBooks.size(); i++) {
                       Book b = availableBooks.get(i);
                       System.out.println((i + 1) + ". " + b.title + " by " + b.author + " (Available: " + b.quantity + ")");
                   }


                   // Prompt user to pick a book by number
                   System.out.print("Enter the number of the book to borrow: ");
                   int bookIndex;
                   try {
                       bookIndex = Integer.parseInt(scanner.nextLine().trim());
                   } catch (NumberFormatException e) {
                       System.out.println("Invalid selection. Operation canceled.");
                       break;
                   }


                   if (bookIndex < 1 || bookIndex > availableBooks.size()) {
                       System.out.println("Selection out of range. Operation canceled.");
                       break;
                   }


                   // Selected book object
                   Book selectedBook = availableBooks.get(bookIndex - 1);


                   // Prompt for number of copies to borrow
                   System.out.print("Enter number of copies to borrow: ");
                   int borrowQty;
                   try {
                       borrowQty = Integer.parseInt(scanner.nextLine().trim());
                   } catch (NumberFormatException e) {
                       System.out.println("Invalid number. Operation canceled.");
                       break;
                   }


                   // Check availability and update quantities
                   if (selectedBook.quantity >= borrowQty) {
                       selectedBook.quantity -= borrowQty; // reduce available
                       selectedBook.lentOut += borrowQty;  // increase lent out
                       System.out.println("Successfully borrowed " + borrowQty + " copy(ies) of '" + selectedBook.title + "'.");
                   } else {
                       System.out.println("Not enough copies available. Current available: " + selectedBook.quantity);
                   }
                   break;


               case 3:
                   // --- Return Books Section ---
                   System.out.print("Enter book title to return: ");
                   String returnTitle = scanner.nextLine().trim();


                   System.out.print("Enter number of copies to return: ");
                   int returnQty;
                   try {
                       returnQty = Integer.parseInt(scanner.nextLine().trim());
                   } catch (NumberFormatException e) {
                       System.out.println("Invalid number. Operation canceled.");
                       break;
                   }


                   // Find the book in library
                   Book bookToReturn = null;
                   for (Book b : library) {
                       if (b.title.equalsIgnoreCase(returnTitle)) {
                           bookToReturn = b;
                           break;
                       }
                   }


                   if (bookToReturn == null) {
                       // Book not part of library system
                       System.out.println("This book does not belong to our library system.");
                   } else if (returnQty > bookToReturn.lentOut) {
                       // Trying to return more than lent out
                       System.out.println("Return quantity exceeds number of copies lent out (" + bookToReturn.lentOut + "). Operation canceled.");
                   } else {
                       // Process return
                       bookToReturn.quantity += returnQty; // increase available
                       bookToReturn.lentOut -= returnQty;  // decrease lent out
                       System.out.println("Successfully returned " + returnQty + " copy(ies) of '" + bookToReturn.title + "'.");
                   }
                   break;


               case 4:
                   // --- Exit Section ---
                   System.out.println("Exiting the system. Goodbye!");
                   scanner.close();
                   System.exit(0);
                   break;


               default:
                   // Handle numbers outside the valid range
                   System.out.println("Invalid choice. Please select a number between 1 and 4.");
           }
       }
   }
}
