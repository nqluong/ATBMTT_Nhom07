import java.util.Scanner;

public class AESMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileReader fileReader = new FileReader();
        boolean running = true;

        while (running) {
            System.out.println("\n======== MÃ HÓA/GIẢI MÃ AES ========");
            System.out.println("1. Mã hóa ");
            System.out.println("2. Giải mã ");
            System.out.println("3. Tạo và hiển thị Round Keys");
            System.out.println("4. Thoát");
            System.out.print("Lựa chọn của bạn: ");

            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số từ 1-4!");
                continue;
            }

            switch (choice) {
                case 1:
                    encryptMenu(fileReader);
                    break;
                case 2:
                    decryptMenu(fileReader);
                    break;
                case 3:
                    generateKeysMenu(fileReader);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn từ 1-4!");
            }
        }
        scanner.close();
    }

    private static void encryptMenu(FileReader fileReader) {
        String message = fileReader.getValue("M");
        String key = fileReader.getValue("K");

        if (message == null || key == null) {
            System.out.println("Không tìm thấy giá trị M hoặc K trong file input.txt");
            return;
        }
        System.out.println("\n=== MÃ HÓA AES ===");
        System.out.println("M = " + message);
        System.out.println("K = " + key);

        System.out.println("\n1. Hiển thị chi tiết từng bước");
        System.out.println("2. Chỉ hiển thị kết quả cuối cùng");
        System.out.print("Lựa chọn của bạn: ");
        Scanner scanner = new Scanner(System.in);

        int displayMode = 0;
        try {
            displayMode = Integer.parseInt(scanner.nextLine().trim());
            if (displayMode != 1 && displayMode != 2) {
                System.out.println("Lựa chọn không hợp lệ! Chọn 1 hoặc 2.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập số 1 hoặc 2!");
            return;
        }
        
        if (displayMode == 1) {
            AESEncrytion.encryptWithSteps(message, key);
        } else {
            AESEncrytion.encryptFinalResult(message, key);
        }
    }
    
    private static void decryptMenu(FileReader fileReader) {
        String ciphertext = fileReader.getValue("C");
        String key = fileReader.getValue("K");

        if (ciphertext == null || key == null) {
            System.out.println("Không tìm thấy giá trị C hoặc K trong file input.txt");
            return;
        }

        System.out.println("\n=== GIẢI MÃ AES ===");
        System.out.println("C = " + ciphertext);
        System.out.println("K = " + key);

        System.out.println("\n1. Hiển thị chi tiết từng bước");
        System.out.println("2. Chỉ hiển thị kết quả cuối cùng");
        System.out.print("Lựa chọn của bạn: ");
        Scanner scanner = new Scanner(System.in);

        int displayMode = 0;
        try {
            displayMode = Integer.parseInt(scanner.nextLine().trim());
            if (displayMode != 1 && displayMode != 2) {
                System.out.println("Lựa chọn không hợp lệ! Chọn 1 hoặc 2.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Vui lòng nhập số 1 hoặc 2!");
            return;
        }
        
        if (displayMode == 1) {
            String plaintext = AESDecryption.decryptWithSteps(ciphertext, key);
        } else {
            String plaintext = AESDecryption.decrypt(ciphertext, key);
            System.out.println("\nKẾT QUẢ GIẢI MÃ: " + plaintext);
        }
    }
    
    private static void generateKeysMenu(FileReader fileReader) {
        String masterKey = fileReader.getValue("K");

        if (masterKey == null) {
            System.out.println("Không tìm thấy giá trị K trong file input.txt");
            return;
        }

        System.out.println("\n=== SINH KHÓA DES ===");
        System.out.println("Khóa chính K = " + masterKey);
        
        byte[] keyBytes = AESEncrytion.hexStringToByteArray(masterKey);
        int[][] roundKeys = AESEncrytion.generateRoundKeys(keyBytes);
        
        System.out.println("\nRound Keys:");
        for (int i = 0; i <= 10; i++) {
            System.out.println("Round Key " + i + ": " + AESEncrytion.wordArrayToHexString(roundKeys[i]));
        }
    }


} 