import java.util.*;
import java.io.*;

public class EncryptionAlgorithms {

    public static String caesarCipher(String text, int key) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                result.append((char) ((ch - base + key) % 26 + base));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    public static String vigenereCipher(String text, String key, boolean autoKey) {
        text = text.toUpperCase();
        key = key.toUpperCase();
        StringBuilder result = new StringBuilder();
        String extendedKey = autoKey ? key + text : key;

        for (int i = 0; i < text.length(); i++) {
            int shift = extendedKey.charAt(i % extendedKey.length()) - 'A';
            char encryptedChar = (char) ((text.charAt(i) - 'A' + shift) % 26 + 'A');
            result.append(encryptedChar);
        }
        return result.toString();
    }

    public static String monoalphabeticCipher(String text, String key) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Map<Character, Character> mapping = new HashMap<>();
        for (int i = 0; i < 26; i++) {
            Character from = Character.valueOf(alphabet.charAt(i));
            Character to = Character.valueOf(key.charAt(i));
            mapping.put(from, to);
        }
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            Character mappedChar = mapping.getOrDefault(Character.valueOf(ch), Character.valueOf(ch));
            result.append(mappedChar);
        }
        return result.toString();
    }

    public static String playfairCipher(String text, String key) {
        key = key.toUpperCase().replace("J", "I");
        String alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
        StringBuilder keySquare = new StringBuilder();
        for (char c : (key + alphabet).toCharArray()) {
            if (keySquare.indexOf(String.valueOf(c)) == -1) {
                keySquare.append(c);
            }
        }
        String formattedText = text.toUpperCase().replace("J", "I");

        for (int i = 0; i < formattedText.length(); i += 2) {
            if (i + 1 == formattedText.length() || formattedText.charAt(i) == formattedText.charAt(i + 1)) {
                formattedText = formattedText.substring(0, i + 1) + 'X' + formattedText.substring(i + 1);
            }
        }

        StringBuilder encryptedText = new StringBuilder();

        for (int i = 0; i < formattedText.length(); i += 2) {
            int a = keySquare.indexOf(String.valueOf(formattedText.charAt(i)));
            int b = keySquare.indexOf(String.valueOf(formattedText.charAt(i + 1)));
            int rowA = a / 5, colA = a % 5;
            int rowB = b / 5, colB = b % 5;
            if (rowA == rowB) {
                encryptedText.append(keySquare.charAt(rowA * 5 + (colA + 1) % 5));
                encryptedText.append(keySquare.charAt(rowB * 5 + (colB + 1) % 5));
            } else if (colA == colB) {
                encryptedText.append(keySquare.charAt(((rowA + 1) % 5) * 5 + colA));
                encryptedText.append(keySquare.charAt(((rowB + 1) % 5) * 5 + colB));
            } else {
                encryptedText.append(keySquare.charAt(rowA * 5 + colB));
                encryptedText.append(keySquare.charAt(rowB * 5 + colA));
            }
        }
        return encryptedText.toString();
    }

    public static String transpositionCipher(String text, int key) {
        char[][] grid = new char[key][text.length()];
        int row = 0, col = 0;
        boolean goingDown = true;

        for (int i = 0; i < text.length(); i++) {
            grid[row][col] = text.charAt(i);
            if (goingDown) {
                row++;
                col++;
                if (row == key) {
                    row = key - 2;
                    goingDown = false;
                }
            } else {
                row--;
                col++;
                if (row == -1) {
                    row = 1;
                    goingDown = true;
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < text.length(); j++) {
                if (grid[i][j] != 0) {
                    result.append(grid[i][j]);
                }
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(new File("input.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(" ");
                int choice = Integer.parseInt(parts[0]);
                String text, keyText;
                int key;

                switch (choice) {
                    case 1:
                        text = parts[1];
                        key = Integer.parseInt(parts[2]);
                        System.out.println("Caesar Cipher: " + caesarCipher(text, key));
                        break;
                    case 2:
                        text = parts[1];
                        keyText = parts[2];
                        System.out.println("Vigenere Cipher (Lặp khóa): " + vigenereCipher(text, keyText, false));
                        break;
                    case 3:
                        text = parts[1];
                        keyText = parts[2];
                        System.out.println("Vigenere Cipher (Auto-key): " + vigenereCipher(text, keyText, true));
                        break;
                    case 4:
                        text = parts[1];
                        keyText = parts[2];
                        System.out.println("Monoalphabetic Cipher: " + monoalphabeticCipher(text, keyText));
                        break;
                    case 5:
                        text = parts[1];
                        keyText = parts[2];
                        System.out.println("Playfair Cipher: " + playfairCipher(text, keyText));
                        break;
                    case 6:
                        text = parts[1];
                        key = Integer.parseInt(parts[2]);
                        System.out.println("Transposition Cipher: " + transpositionCipher(text, key));
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ: " + choice);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Không tìm thấy file input.txt");
        } catch (Exception e) {
            System.err.println("Lỗi xử lý dữ liệu: " + e.getMessage());
        }
    }
}
