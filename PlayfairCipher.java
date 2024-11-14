import java.util.Scanner;

/** Project:Lab5
 * Purpose Details:Security assignment
 * Course:IST242
 * Author:Aidan Ramirez
 * Date Developed: 11/3
 * Last Date Changed:11/3
 * Rev:

 */


//createKeySquare: Generates a 5x5 grid based on the keyword.
//formatText: Formats plaintext into digraphs (pairs), adding "X" for repeated letters or at the end if needed.
//encryptPair and decryptPair: Follow the Playfair cipher rules for encrypting and decrypting pairs of characters.
//findPosition: Finds the position of a letter in the key square.

public class PlayfairCipher {

    private static final int SIZE = 5; // Size of the Playfair cipher key square

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the plaintext: ");
        String plaintext = scanner.nextLine().toUpperCase().replaceAll("[^A-Z]", "");

        System.out.print("Enter the keyword: ");
        String keyword = scanner.nextLine().toUpperCase().replaceAll("[^A-Z]", "");

        char[][] keySquare = createKeySquare(keyword);

        String encryptedText = encrypt(plaintext, keySquare);
        System.out.println("Encrypted text: " + encryptedText);

        String decryptedText = decrypt(encryptedText, keySquare);
        System.out.println("Decrypted text: " + decryptedText);

        scanner.close();
    }

    // Method to create the key square
    private static char[][] createKeySquare(String keyword) {
        char[][] keySquare = new char[SIZE][SIZE];
        StringBuilder sb = new StringBuilder();
        boolean[] used = new boolean[26];

        for (char ch : keyword.toCharArray()) {
            if (ch == 'J') ch = 'I'; // Combining I and J
            if (!used[ch - 'A']) {
                sb.append(ch);
                used[ch - 'A'] = true;
            }
        }

        for (char ch = 'A'; ch <= 'Z'; ch++) {
            if (ch == 'J') continue; // Skip J to fit the 5x5 grid
            if (!used[ch - 'A']) {
                sb.append(ch);
                used[ch - 'A'] = true;
            }
        }

        // Fill key square
        int index = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                keySquare[i][j] = sb.charAt(index++);
            }
        }
        return keySquare;
    }

    // Encrypt method
    public static String encrypt(String plaintext, char[][] keySquare) {
        StringBuilder encryptedText = new StringBuilder();
        String formattedText = formatText(plaintext);

        for (int i = 0; i < formattedText.length(); i += 2) {
            char firstChar = formattedText.charAt(i);
            char secondChar = formattedText.charAt(i + 1);
            encryptedText.append(encryptPair(firstChar, secondChar, keySquare));
        }
        return encryptedText.toString();
    }

    // Decrypt method
    public static String decrypt(String ciphertext, char[][] keySquare) {
        StringBuilder decryptedText = new StringBuilder();

        for (int i = 0; i < ciphertext.length(); i += 2) {
            char firstChar = ciphertext.charAt(i);
            char secondChar = ciphertext.charAt(i + 1);
            decryptedText.append(decryptPair(firstChar, secondChar, keySquare));
        }
        return decryptedText.toString();
    }

    // Format text by pairing letters and adding padding if needed
    private static String formatText(String text) {
        StringBuilder formattedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            formattedText.append(ch);

            // If the next character is the same, add 'X' to split
            if (i < text.length() - 1 && text.charAt(i) == text.charAt(i + 1)) {
                formattedText.append('X');
            }
        }
        if (formattedText.length() % 2 != 0) {
            formattedText.append('X'); // Padding if the final length is odd
        }
        return formattedText.toString();
    }

    // Encrypt a pair of letters
    private static String encryptPair(char first, char second, char[][] keySquare) {
        int[] pos1 = findPosition(first, keySquare);
        int[] pos2 = findPosition(second, keySquare);

        if (pos1[0] == pos2[0]) { // Same row
            return "" + keySquare[pos1[0]][(pos1[1] + 1) % SIZE] + keySquare[pos2[0]][(pos2[1] + 1) % SIZE];
        } else if (pos1[1] == pos2[1]) { // Same column
            return "" + keySquare[(pos1[0] + 1) % SIZE][pos1[1]] + keySquare[(pos2[0] + 1) % SIZE][pos2[1]];
        } else { // Rectangle rule
            return "" + keySquare[pos1[0]][pos2[1]] + keySquare[pos2[0]][pos1[1]];
        }
    }

    // Decrypt a pair of letters
    private static String decryptPair(char first, char second, char[][] keySquare) {
        int[] pos1 = findPosition(first, keySquare);
        int[] pos2 = findPosition(second, keySquare);

        if (pos1[0] == pos2[0]) { // Same row
            return "" + keySquare[pos1[0]][(pos1[1] + SIZE - 1) % SIZE] + keySquare[pos2[0]][(pos2[1] + SIZE - 1) % SIZE];
        } else if (pos1[1] == pos2[1]) { // Same column
            return "" + keySquare[(pos1[0] + SIZE - 1) % SIZE][pos1[1]] + keySquare[(pos2[0] + SIZE - 1) % SIZE][pos2[1]];
        } else { // Rectangle rule
            return "" + keySquare[pos1[0]][pos2[1]] + keySquare[pos2[0]][pos1[1]];
        }
    }

    // Find the row and column of a letter in the key square
    private static int[] findPosition(char ch, char[][] keySquare) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (keySquare[i][j] == ch) {
                    return new int[] { i, j };
                }
            }
        }
        return null;
    }
}
