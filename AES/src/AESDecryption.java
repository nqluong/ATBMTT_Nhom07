public class AESDecryption {
    private static final int[] INV_SBOX = {
            0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb,
            0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb,
            0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e,
            0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25,
            0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92,
            0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84,
            0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06,
            0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b,
            0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73,
            0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e,
            0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b,
            0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4,
            0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f,
            0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef,
            0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61,
            0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d
    };

    private static final int[][] INV_MIX_COLUMNS_MATRIX = {
            {0x0e, 0x0b, 0x0d, 0x09},
            {0x09, 0x0e, 0x0b, 0x0d},
            {0x0d, 0x09, 0x0e, 0x0b},
            {0x0b, 0x0d, 0x09, 0x0e}
    };

    public static byte[] performDecryption(byte[] ciphertext, byte[][] roundKeys) {
        byte[] state = addRoundKey(ciphertext, roundKeys[10]);

        for (int round = 9; round >= 1; round--) {
            state = invShiftRows(state);
            state = invSubBytes(state);
            state = addRoundKey(state, roundKeys[round]);
            state = invMixColumns(state);
        }

        state = invShiftRows(state);
        state = invSubBytes(state);
        state = addRoundKey(state, roundKeys[0]);

        return state;
    }

    public static byte[] invSubBytes(byte[] state) {
        byte[] result = new byte[16];
        for (int i = 0; i < 16; i++) {
            result[i] = (byte) INV_SBOX[state[i] & 0xFF];
        }
        return result;
    }

    public static byte[] invShiftRows(byte[] state) {
        byte[] result = new byte[16];
        result[0] = state[0];
        result[4] = state[4];
        result[8] = state[8];
        result[12] = state[12];

        result[1] = state[13];
        result[5] = state[1];
        result[9] = state[5];
        result[13] = state[9];

        result[2] = state[10];
        result[6] = state[14];
        result[10] = state[2];
        result[14] = state[6];

        result[3] = state[7];
        result[7] = state[11];
        result[11] = state[15];
        result[15] = state[3];

        return result;
    }

    public static byte[] invMixColumns(byte[] state) {
        byte[] result = new byte[16];
        for (int col = 0; col < 4; col++) {
            int column = col * 4;
            for (int row = 0; row < 4; row++) {
                int sum = 0;
                for (int i = 0; i < 4; i++) {
                    int a = INV_MIX_COLUMNS_MATRIX[row][i];
                    int b = state[column + i] & 0xFF;

                    if(a == 0x09){
                        sum ^= mul9(b);
                    } else if(a == 0x0b){
                        sum ^= mulB(b);
                    } else if(a == 0x0d){
                        sum ^= mulD(b);
                    } else if(a == 0x0e){
                        sum ^= mulE(b);
                    } else {
                        sum ^= b;
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
    public static int mul4(int a){
        return mul2(mul2(a));
    }
    public static int mul8(int a){
        return mul2(mul4(a));
    }
    public static int mul9(int a){
        return mul8(a) ^ a;
    }
    public static int mulB(int a){
        return mul8(a) ^ mul2(a) ^ a;
    }
    public static int mulD(int a){
        return mul8(a) ^ mul4(a) ^ a;
    }
    public static int mulE(int a){
        return mul8(a) ^ mul4(a) ^ mul2(a);
    }

    public static byte[] addRoundKey(byte[] state, byte[] roundKey) {
        byte[] result = new byte[16];
        for (int i = 0; i < 16; i++) {
            result[i] = (byte) (state[i] ^ roundKey[i]);
        }
        return result;
    }

    public static String decrypt(String ciphertext, String key) {
        byte[] ciphertextBytes = AESEncrytion.hexStringToByteArray(ciphertext);
        byte[] keyBytes = AESEncrytion.hexStringToByteArray(key);

        byte[][] expandedKey = AESEncrytion.expandKey(keyBytes);

        byte[] plaintextBytes = performDecryption(ciphertextBytes, expandedKey);
        return AESEncrytion.byteArrayToHexString(plaintextBytes);
    }

    public static String decryptWithSteps(String ciphertext, String key) {
        System.out.println("\n--- GIẢI MÃ AES ---");
        System.out.println("Ciphertext: " + ciphertext);
        System.out.println("Key: " + key);

        byte[] ciphertextBytes = AESEncrytion.hexStringToByteArray(ciphertext);
        byte[] keyBytes = AESEncrytion.hexStringToByteArray(key);

        byte[][] expandedKey = AESEncrytion.expandKey(keyBytes);

        System.out.println("\n--- ROUND KEYS ---");
        for (int i = 0; i <= 10; i++) {
            System.out.println("Round Key " + i + ": " + AESEncrytion.byteArrayToHexString(expandedKey[i]));
        }

        System.out.println("\n--- Round 0 ---");
        byte[] state = addRoundKey(ciphertextBytes, expandedKey[10]);
        System.out.println("Sau AddRoundKey: " + AESEncrytion.byteArrayToHexString(state));

        for (int round = 9; round >= 1; round--) {
            System.out.println("\n--- Round " + (10 - round) + " ---");

            byte[] afterInvShiftRows = invShiftRows(state);
            System.out.println("Sau InvShiftRows: " + AESEncrytion.byteArrayToHexString(afterInvShiftRows));

            byte[] afterInvSubBytes = invSubBytes(afterInvShiftRows);
            System.out.println("Sau InvSubBytes: " + AESEncrytion.byteArrayToHexString(afterInvSubBytes));

            byte[] afterAddRoundKey = addRoundKey(afterInvSubBytes, expandedKey[round]);
            System.out.println("Sau AddRoundKey: " + AESEncrytion.byteArrayToHexString(afterAddRoundKey));

            state = invMixColumns(afterAddRoundKey);
            System.out.println("Sau InvMixColumns: " + AESEncrytion.byteArrayToHexString(state));
        }

        System.out.println("\n--- Round 10 ---");
        byte[] afterInvShiftRows = invShiftRows(state);
        System.out.println("Sau InvShiftRows: " + AESEncrytion.byteArrayToHexString(afterInvShiftRows));

        byte[] afterInvSubBytes = invSubBytes(afterInvShiftRows);
        System.out.println("Sau InvSubBytes: " + AESEncrytion.byteArrayToHexString(afterInvSubBytes));

        byte[] plaintext = addRoundKey(afterInvSubBytes, expandedKey[0]);
        System.out.println("\nKẾT QUẢ GIẢI MÃ: " + AESEncrytion.byteArrayToHexString(plaintext));

        return AESEncrytion.byteArrayToHexString(plaintext);
    }
} 