public class AESEncrytion {

    private static final int[] SBOX = {
            0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76,
            0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0,
            0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15,
            0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75,
            0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84,
            0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf,
            0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8,
            0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2,
            0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73,
            0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb,
            0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79,
            0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08,
            0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a,
            0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e,
            0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf,
            0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16
    };

    private static final int[] RCON = {
            0x01000000, 0x02000000, 0x04000000, 0x08000000, 0x10000000,
            0x20000000, 0x40000000, 0x80000000, 0x1b000000, 0x36000000
    };

    private static final int[][] MIX_COLUMNS_MATRIX = {
            {0x02, 0x03, 0x01, 0x01},
            {0x01, 0x02, 0x03, 0x01},
            {0x01, 0x01, 0x02, 0x03},
            {0x03, 0x01, 0x01, 0x02}
    };

    public static byte[] performKeyExpansion(byte[] input, byte[][] roundKeys) {
        byte[] state = addRoundKey(input, roundKeys[0]);
        for(int i = 1; i < 10; i++){
            state = subBytes(state);
            state = shiftRows(state);
            state = mixColumns(state);
            state = addRoundKey(state, roundKeys[i]);
        }

        state = subBytes(state);
        state = shiftRows(state);
        state = addRoundKey(state, roundKeys[10]);

        return state;
    }

    public static int[] splitKeyIntoWords(byte[] keyBytes) {
        int[] words = new int[4];
        for (int i = 0; i < 4; i++) {
            words[i] = ((keyBytes[4*i] & 0xFF) << 24) |
                    ((keyBytes[4*i+1] & 0xFF) << 16) |
                    ((keyBytes[4*i+2] & 0xFF) << 8) |
                    (keyBytes[4*i+3] & 0xFF);
        }
        return words;
    }

    public static int rotWord(int word) {
        return (word << 8) | ((word >>> 24) & 0xFF);
    }

    public static int subWord(int word) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            int byteVal = (word >>> (24 - 8 * i)) & 0xFF;
            result |= (SBOX[byteVal] << (24 - 8 * i));
        }
        return result;
    }

    public static int xorWords(int word1, int roundIndex) {
        return word1 ^ RCON[roundIndex];
    }

    public static int xorBit(int word1, int word2) {
        return word1 ^ word2;
    }

    public static int[][] generateRoundKeys(byte[] keyBytes) {
        int[][] roundKeys = new int[11][4];

        roundKeys[0] = splitKeyIntoWords(keyBytes);
        for(int i = 1; i <= 10; i++){
            int [] prevKey = roundKeys[i-1];
            int [] newKey = new int[4];

            int rw = rotWord(prevKey[3]);

            int sw = subWord(rw);

            int xcsw = xorWords(sw, i-1);

            newKey[0] = xorBit(xcsw, prevKey[0]);
            newKey[1] = xorBit(newKey[0], prevKey[1]);
            newKey[2] = xorBit(newKey[1], prevKey[2]);
            newKey[3] = xorBit(newKey[2], prevKey[3]);

            roundKeys[i] = newKey;
        }
        return roundKeys;
    }

    public static byte[] addRoundKey(byte[] state, byte[] roundKey) {
        byte[] result = new byte[16];
        for (int i = 0; i < 16; i++) {
            result[i] = (byte) (state[i] ^ roundKey[i]);
        }
        return result;
    }

    public static byte[] subBytes(byte[] state) {
        byte[] result = new byte[16];
        for (int i = 0; i < 16; i++) {
            result[i] = (byte) SBOX[state[i] & 0xFF];
        }
        return result;
    }

    public static byte[] shiftRows(byte[] state) {
        byte[] result = new byte[16];
        result[0] = state[0];
        result[4] = state[4];
        result[8] = state[8];
        result[12] = state[12];

        result[1] = state[5];
        result[5] = state[9];
        result[9] = state[13];
        result[13] = state[1];

        result[2] = state[10];
        result[6] = state[14];
        result[10] = state[2];
        result[14] = state[6];

        result[3] = state[15];
        result[7] = state[3];
        result[11] = state[7];
        result[15] = state[11];

        return result;
    }

    public static byte[] mixColumns(byte[] state) {
        byte[] result = new byte[16];
        for(int col = 0; col < 4; col++){
            int column = col * 4;
            for(int row = 0; row < 4; row++){
                int sum = 0;
                for(int i = 0; i < 4 ;i ++){
                    int a = MIX_COLUMNS_MATRIX[row][i];
                    int b = state[column+i] & 0xFF;
                    if(a == 1){
                        sum ^= b;
                    } else if(a == 2){
                        sum ^= mul2(b);
                    } else if(a == 3){
                        sum ^= mul2(b) ^ b;
                    }
                }
                result[column + row] = (byte) sum;
            }
        }
        return result;
    }

    public static int mul2(int a) {
        return (a << 1) ^ ((a & 0x80) != 0 ? 0x1B : 0);
    }

    public static byte[][] expandKey(byte[] key) {
        byte[][] expandedKey = new byte[11][16];

        System.arraycopy(key, 0, expandedKey[0], 0, 16);

        int[][] roundKeys = generateRoundKeys(key);

        for (int round = 1; round <= 10; round++) {
            for (int i = 0; i < 4; i++) {
                expandedKey[round][i*4] = (byte) ((roundKeys[round][i] >>> 24) & 0xFF);
                expandedKey[round][i*4+1] = (byte) ((roundKeys[round][i] >>> 16) & 0xFF);
                expandedKey[round][i*4+2] = (byte) ((roundKeys[round][i] >>> 8) & 0xFF);
                expandedKey[round][i*4+3] = (byte) (roundKeys[round][i] & 0xFF);
            }
        }

        return expandedKey;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String wordToHex(int[] word) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < word.length; i++) {
            hex.append(String.format("%08X", word[i]));
        }
        return hex.toString();
    }

    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02X", b & 0xFF));
        }
        return hex.toString();
    }

    public static String wordArrayToHexString(int[] words) {
        StringBuilder sb = new StringBuilder();
        for (int word : words) {
            sb.append(String.format("%08X", word));
        }
        return sb.toString();
    }
    public static void encryptWithSteps(String plaintext, String key) {
        byte[] inputBytes = hexStringToByteArray(plaintext);
        byte[] keyBytes = hexStringToByteArray(key);

        byte[][] expandedKey = expandKey(keyBytes);

        System.out.println("\n--- ROUND KEYS ---");
        for (int i = 0; i <= 10; i++) {
            System.out.println("Round Key " + i + ": " + byteArrayToHexString(expandedKey[i]));
        }

        System.out.println("\n--- MÃ HÓA ---");
        System.out.println("Plaintext: " + plaintext);
        System.out.println("Key: " + key);

        System.out.println("\nRound 0:");
        byte[] state = addRoundKey(inputBytes, keyBytes);
        System.out.println("Sau AddRoundKey: " + byteArrayToHexString(state));

        for (int round = 1; round <= 9; round++) {
            System.out.println("\n--- Round " + round + " ---");

            byte[] afterSubBytes = subBytes(state);
            System.out.println("Sau SubBytes: " + byteArrayToHexString(afterSubBytes));

            byte[] afterShiftRows = shiftRows(afterSubBytes);
            System.out.println("Sau ShiftRows: " + byteArrayToHexString(afterShiftRows));

            byte[] afterMixColumns = mixColumns(afterShiftRows);
            System.out.println("Sau MixColumns: " + byteArrayToHexString(afterMixColumns));

            state = addRoundKey(afterMixColumns, expandedKey[round]);
            System.out.println("Sau AddRoundKey: " + byteArrayToHexString(state));
        }

        System.out.println("\n--- Round 10 ---");
        byte[] afterSubBytes = subBytes(state);
        System.out.println("Sau SubBytes: " + byteArrayToHexString(afterSubBytes));

        byte[] afterShiftRows = shiftRows(afterSubBytes);
        System.out.println("Sau ShiftRows: " + byteArrayToHexString(afterShiftRows));

        byte[] finalState = addRoundKey(afterShiftRows, expandedKey[10]);
        System.out.println("\nKẾT QUẢ MÃ HÓA: " + byteArrayToHexString(finalState));
    }

    public static void encryptFinalResult(String plaintext, String key) {
        byte[] inputBytes = hexStringToByteArray(plaintext);
        byte[] keyBytes = hexStringToByteArray(key);

        byte[][] expandedKey = expandKey(keyBytes);
        byte[] state = performKeyExpansion(inputBytes, expandedKey);

        System.out.println("\n--- MÃ HÓA ---");
        System.out.println("Plaintext: " + plaintext);
        System.out.println("Key: " + key);
        System.out.println("\nKẾT QUẢ MÃ HÓA: " + byteArrayToHexString(state));
    }
}
