import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class TextAnalysisTool {


    /**
     * The entry point of the program. Handles user interaction and
     * coordinates calls to helper methods that perform the text analysis.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        String inputText = readNonEmptyText(scanner);


        int characterCount = countCharacters(inputText);
        int wordCount = countWords(inputText);


        System.out.println();
        System.out.println("=== Basic Text Statistics ===");
        System.out.println(
                "Total characters (including spaces): " + characterCount
        );
        System.out.println("Total words: " + wordCount);


        char mostCommonCharacter = findMostCommonCharacter(inputText);
        System.out.println(
                "Most common character (ignoring spaces, case-insensitive): '"
                        + mostCommonCharacter + "'"
        );


        System.out.println();
        System.out.print("Enter a character to analyze its frequency: ");
        String characterInput = readSingleCharacter(scanner);
        char characterToCheck = characterInput.charAt(0);
        int characterFrequency =
                countCharacterFrequency(inputText, characterToCheck);
        System.out.println(
                "Frequency of '" + characterToCheck
                        + "' (case-insensitive): " + characterFrequency
        );


        System.out.println();
        System.out.print("Enter a word to analyze its frequency: ");
        String wordToCheck = readNonEmptyWord(scanner);
        int wordFrequency = countWordFrequency(inputText, wordToCheck);
        System.out.println(
                "Frequency of \"" + wordToCheck
                        + "\" (case-insensitive): " + wordFrequency
        );


        int uniqueWordCount = countUniqueWords(inputText);
        System.out.println();
        System.out.println(
                "Number of unique words (case-insensitive): "
                        + uniqueWordCount
        );


        scanner.close();
    }


    /**
     * Repeatedly prompts the user to enter a non-empty block of text.
     *
     * Preconditions:
     * The scanner must be open and not null.
     *
     * @param scanner the Scanner used to read user input
     * @return a non-empty string of text
     */
    private static String readNonEmptyText(Scanner scanner) {
        String text;


        while (true) {
            System.out.println("Please enter a paragraph or a lengthy text:");
            text = scanner.nextLine();


            if (text.trim().isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
                System.out.println();
            } else {
                break;
            }
        }


        return text;
    }


    /**
     * Counts the total number of characters in the given text.
     *
     * Preconditions:
     * The text parameter must not be null.
     *
     * @param text the text to analyze
     * @return the total number of characters
     */
    private static int countCharacters(String text) {
        return text.length();
    }


    /**
     * Counts the total number of words in the given text.
     * Words are assumed to be separated by whitespace.
     *
     * Preconditions:
     * The text parameter must not be null.
     *
     * @param text the text to analyze
     * @return the total number of words
     */
    private static int countWords(String text) {
        String trimmed = text.trim();


        if (trimmed.isEmpty()) {
            return 0;
        }


        String[] words = trimmed.split("\\s+");
        return words.length;
    }


    /**
     * Finds the most common character in the text.
     * The count is case-insensitive and spaces are ignored.
     * In case of a tie, any of the most frequent characters may be returned.
     *
     * Preconditions:
     * The text parameter must not be null.
     *
     * @param text the text to analyze
     * @return the most common character, or a space character if
     *         there are no non-space characters
     */
    private static char findMostCommonCharacter(String text) {
        // Maps each character (lowercased) to its frequency in the text.
        Map<Character, Integer> frequencyMap = new HashMap<>();


        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);


            if (current == ' ') {
                // Ignore spaces for this particular analysis.
                continue;
            }


            char lowerCurrent = Character.toLowerCase(current);
            Integer count = frequencyMap.get(lowerCurrent);


            if (count == null) {
                frequencyMap.put(lowerCurrent, 1);
            } else {
                frequencyMap.put(lowerCurrent, count + 1);
            }
        }


        char mostCommon = ' ';
        int maxCount = 0;


        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            char character = entry.getKey();
            int count = entry.getValue();


            if (count > maxCount) {
                maxCount = count;
                mostCommon = character;
            }
        }


        return mostCommon;
    }


    /**
     * Ensures that the user enters exactly one character.
     *
     * Preconditions:
     * The scanner must be open and not null.
     *
     * @param scanner the Scanner used to read user input
     * @return a String containing exactly one character
     */
    private static String readSingleCharacter(Scanner scanner) {
        String input;


        while (true) {
            input = scanner.nextLine();


            if (input.length() != 1) {
                System.out.print(
                        "Please enter exactly one character: "
                );
            } else {
                break;
            }
        }


        return input;
    }


    /**
     * Ensures that the user enters a non-empty word.
     *
     * Preconditions:
     * The scanner must be open and not null.
     *
     * @param scanner the Scanner used to read user input
     * @return a non-empty word
     */
    private static String readNonEmptyWord(Scanner scanner) {
        String word;


        while (true) {
            word = scanner.nextLine().trim();


            if (word.isEmpty()) {
                System.out.print(
                        "Word cannot be empty. Please enter a word: "
                );
            } else {
                break;
            }
        }


        return word;
    }


    /**
     * Counts the frequency of the given character in the text,
     * ignoring case.
     *
     * Preconditions:
     * The text parameter must not be null.
     *
     * @param text       the text to analyze
     * @param targetChar the character to count
     * @return the number of occurrences of the character in the text
     */
    private static int countCharacterFrequency(String text, char targetChar) {
        int count = 0;
        char lowerTarget = Character.toLowerCase(targetChar);


        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            if (Character.toLowerCase(current) == lowerTarget) {
                count++;
            }
        }


        return count;
    }


    /**
     * Counts the frequency of a given word in the text, ignoring case.
     * Words are assumed to be separated by whitespace.
     *
     * Preconditions:
     * The text parameter must not be null. The targetWord parameter
     * should not be null, but an empty or blank string will simply
     * result in a frequency of zero.
     *
     * @param text       the text to analyze
     * @param targetWord the word to count
     * @return the number of occurrences of the word in the text
     */
    private static int countWordFrequency(String text, String targetWord) {
        if (targetWord == null || targetWord.trim().isEmpty()) {
            return 0;
        }


        String trimmedText = text.trim();


        if (trimmedText.isEmpty()) {
            return 0;
        }


        String[] words = trimmedText.split("\\s+");
        int count = 0;
        String lowerTarget = targetWord.toLowerCase();


        for (String word : words) {
            if (word.toLowerCase().equals(lowerTarget)) {
                count++;
            }
        }


        return count;
    }


    /**
     * Counts the number of unique words in the text, ignoring case.
     * Words are assumed to be separated by whitespace.
     *
     * Preconditions:
     * The text parameter must not be null.
     *
     * @param text the text to analyze
     * @return the number of unique words
     */
    private static int countUniqueWords(String text) {
        String trimmed = text.trim();


        if (trimmed.isEmpty()) {
            return 0;
        }


        String[] words = trimmed.split("\\s+");


        // Holds each distinct word in lowercase form.
        Set<String> uniqueWords = new HashSet<>();


        for (String word : words) {
            String lowerWord = word.toLowerCase();
            uniqueWords.add(lowerWord);
        }


        return uniqueWords.size();
    }
}
