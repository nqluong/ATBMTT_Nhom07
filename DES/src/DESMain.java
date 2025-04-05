public class DESMain {
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        boolean exit = false;
        FileReader fileReader = new FileReader();

        while (!exit) {
            System.out.println("\n=== MÃ HÓA/ GIẢI MÃ DES ===");
            System.out.println("1. Mã hóa DES");
            System.out.println("2. Giải mã DES");
            System.out.println("3. Sinh khóa DES");
            System.out.println("0. Thoát");
            System.out.print("Lựa chọn của bạn: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập lại.");
                continue;
            }

            switch (choice) {
                case 1:
                    encryptFromFile(fileReader);
                    break;
                case 2:
                    decryptFromFile(fileReader);
                    break;
                case 3:
                    generateKeyFromFile(fileReader);
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập lại.");
            }
        }
        scanner.close();
    }
    
    private static void encryptFromFile(FileReader fileReader) {
        String message = fileReader.getValue("M");
        String key = fileReader.getValue("K");
        
        if (message == null || key == null) {
            System.out.println("Không tìm thấy giá trị M hoặc K trong file input.txt");
            return;
        }

        System.out.println("\n=== MÃ HÓA DES ===");
        System.out.println("M = " + message);
        System.out.println("K = " + key);
        
        System.out.println("\n1. Hiển thị chi tiết từng bước");
        System.out.println("2. Chỉ hiển thị kết quả cuối cùng");
        System.out.print("Lựa chọn của bạn: ");
        
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int choice = scanner.nextInt();
        
        switch (choice) {
            case 1:
                DES.encryptWithDetails(message, key);
                break;
            case 2:
                String result = DES.encrypt(message, key);
                System.out.println("\nKết quả mã hóa:");
                System.out.println("C = " + result);
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ.");
        }
    }
    
    private static void decryptFromFile(FileReader fileReader) {
        String ciphertext = fileReader.getValue("C");
        String key = fileReader.getValue("K");
        
        if (ciphertext == null || key == null) {
            System.out.println("Không tìm thấy giá trị C hoặc K trong file input.txt");
            return;
        }

        System.out.println("\n=== GIẢI MÃ DES ===");
        System.out.println("C = " + ciphertext);
        System.out.println("K = " + key);
        
        System.out.println("\n1. Hiển thị chi tiết từng bước");
        System.out.println("2. Chỉ hiển thị kết quả cuối cùng");
        System.out.print("Lựa chọn của bạn: ");
        
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int choice = scanner.nextInt();
        
        switch (choice) {
            case 1:
                DES.decryptWithDetails(ciphertext, key);
                break;
            case 2:
                String result = DES.decrypt(ciphertext, key);
                System.out.println("\nKết quả giải mã:");
                System.out.println("M = " + result);
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ.");
        }
    }
    
    private static void generateKeyFromFile(FileReader fileReader) {
        String masterKey = fileReader.getValue("K");
        
        if (masterKey == null) {
            System.out.println("Không tìm thấy giá trị K trong file input.txt");
            return;
        }

        System.out.println("\n=== SINH KHÓA DES ===");
        System.out.println("Khóa chính K = " + masterKey);
        
        DESKey desKey = new DESKey();
        String[] subkeys = desKey.generateSubkeys(masterKey);
        
        System.out.println("\nCác khóa con:");
        for (int i = 1; i <= 16; i++) {
            System.out.println("K" + i + " = " + binaryToHex(subkeys[i]));
        }
    }
    
    private static String binaryToHex(String binary) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 4) {
            int decimal = Integer.parseInt(binary.substring(i, Math.min(i + 4, binary.length())), 2);
            hex.append(Integer.toHexString(decimal).toUpperCase());
        }
        return hex.toString();
    }
}
