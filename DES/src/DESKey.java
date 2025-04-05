public class DESKey {

    private static final int[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    private static final int[] PC2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    private static final int[] SHIFTS = {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };

    private static String permute(String input, int[] permutationTable, int resultLength) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < permutationTable.length; i++) {
            output.append(input.charAt(permutationTable[i] - 1));
        }
        return output.toString();
    }

    private static String leftShift(String input, int shiftBits) {
        return input.substring(shiftBits) + input.substring(0, shiftBits);
    }

    private static String hexToBinary(String hex) {
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < hex.length(); i++) {
            char hexChar = hex.charAt(i);
            int value;
            if (hexChar >= '0' && hexChar <= '9') {
                value = hexChar - '0';
            } else if (hexChar >= 'A' && hexChar <= 'F') {
                value = hexChar - 'A' + 10;
            } else if (hexChar >= 'a' && hexChar <= 'f') {
                value = hexChar - 'a' + 10;
            } else {
                throw new IllegalArgumentException("Kí tự không đúng dạng: " + hexChar);
            }
            
            String bin = Integer.toBinaryString(value);
            while (bin.length() < 4) {
                bin = "0" + bin;
            }
            binary.append(bin);
        }
        return binary.toString();
    }

    private static String binaryToHex(String binary) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 4) {
            int decimal = Integer.parseInt(binary.substring(i, Math.min(i + 4, binary.length())), 2);
            hex.append(Integer.toHexString(decimal).toUpperCase());
        }
        return hex.toString();
    }


    public String[] generateSubkeys(String hexKey) {
        String[] subkeys = new String[17];

        String binaryKey = hexToBinary(hexKey);
        while (binaryKey.length() < 64) {
            binaryKey = "0" + binaryKey;
        }

        String permutedKey = permute(binaryKey, PC1, 56);

        String c = permutedKey.substring(0, 28);
        String d = permutedKey.substring(28, 56);

        String[] cValues = new String[17];
        String[] dValues = new String[17];
        cValues[0] = c;
        dValues[0] = d;

        for (int i = 1; i <= 16; i++) {
            c = leftShift(c, SHIFTS[i - 1]);
            d = leftShift(d, SHIFTS[i - 1]);

            cValues[i] = c;
            dValues[i] = d;

            String combinedKey = c + d;
            subkeys[i] = permute(combinedKey, PC2, 48);
        }

        return subkeys;
    }
}
