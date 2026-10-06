import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public class MD5 {
    private static final int blockSize = 64;
    private static final int wordSize = 4;
    private static final int a0 = 0x67452301;
    private static final int b0 = 0xefcdab89;
    private static final int c0 = 0x98badcfe;
    private static final int d0 = 0x10325476;
    private static final int[] K = {
            0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee,
            0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501,
            0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be,
            0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821,//16
            0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa,
            0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8,
            0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed,
            0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a,//32
            0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c,
            0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70,
            0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05,
            0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665,//48
            0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039,
            0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1,
            0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1,
            0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391//64
    };

    private static final int[] S ={
            7, 12, 17, 22,  7, 12, 17, 22,  7, 12, 17, 22,  7, 12, 17, 22,
            5,  9, 14, 20,  5,  9, 14, 20,  5,  9, 14, 20,  5,  9, 14, 20,
            4, 11, 16, 23,  4, 11, 16, 23,  4, 11, 16, 23,  4, 11, 16, 23,
            6, 10, 15, 21,  6, 10, 15, 21,  6, 10, 15, 21,  6, 10, 15, 21
    };

    private static byte[] paddingFiller(byte[] data){//448+64 bits = 56+8 bytes
        long bitLen = data.length * 8L;//long type is 64bit

        byte[] lenInBytes = new byte[8];//little endian order
        lenInBytes[0] = (byte) (bitLen);
        lenInBytes[1] = (byte) (bitLen >> 8);
        lenInBytes[2] = (byte) (bitLen >> 16);
        lenInBytes[3] = (byte) (bitLen >> 24);
        lenInBytes[4] = (byte) (bitLen >> 32);
        lenInBytes[5] = (byte) (bitLen >> 40);
        lenInBytes[6] = (byte) (bitLen >> 48);
        lenInBytes[7] = (byte) (bitLen >> 56);

        int blocks;
        if(data.length%blockSize < 56){    //in case if (payload)%64 more than 56
            blocks = data.length/blockSize + 1;
        }else{
            blocks = data.length/blockSize + 2;
        }

        byte[] paddedArray = new byte[blocks*blockSize];

        System.arraycopy(data,0,paddedArray,0,data.length);

        paddedArray[data.length] = (byte)0x80;
        for(int i = 0; i<lenInBytes.length; i++){
            paddedArray[blocks*blockSize-1-i]=lenInBytes[lenInBytes.length-1-i];//little endian
        }

        return paddedArray;
    }

    private static int makeWord(byte[] bytes, int offset) {//word as INT
        return ((bytes[offset] & 0xFF)) |
                ((bytes[offset + 1] & 0xFF) << 8) |
                ((bytes[offset + 2] & 0xFF) << 16)|
                ((bytes[offset + 3] & 0xFF) << 24);
    }

    private int[] blockProcessor(byte[] data,int blockIndex,int[] finalState){
        int[] M = new int[16];
        for(int i=0;i<M.length;i++){
            M[i] = makeWord(data,blockSize*blockIndex+i*wordSize);
        }

        int A = finalState[0];
        int B = finalState[1];
        int C = finalState[2];
        int D = finalState[3];

        for(int i=0;i<64;i++){
            int F,g;
            if(i < 16){
                F = (B & C) | ((~B) & D);
                g = i;
            } else if (i < 32) {
                F = (D & B) | (~D & C);
                g = (5*i + 1)%16;
            } else if (i < 48){
                F = B^C^D;
                g = (3*i+5)%16;
            }else {
                F = C^(B | ~D);
                g = (7*i)%16;
            }
            F = F + A + K[i] + M[g];
            A = D;
            D = C;
            C = B;
            B = B + Integer.rotateLeft(F, S[i]);
        }

        return new int[] {
                a0 + A,
                b0 + B,
                c0 + C,
                d0 + D
        };
    }

    private static String toHexLE(int word) {
        StringBuilder result = new StringBuilder(8);

        // Output word in little-endian byte order
        for (int i = 0; i < 4; i++) {
            int value = (word >>> (8 * i)) & 0xFF;
            result.append(String.format("%02x", value));
        }

        return result.toString();
    }

    public String run(byte[] input){
        if(Objects.equals(input,null)){throw new NullPointerException("Null input.");}

        byte[] data = paddingFiller(input);

        int[] finalState = {a0,b0,c0,d0};

        for(int i=0;i<(data.length/blockSize);i++){
            int[] state = blockProcessor(data,i,finalState);
            finalState[0] = state[0];
            finalState[1] = state[1];
            finalState[2] = state[2];
            finalState[3] = state[3];
            //System.out.println(Arrays.toString(finalState));
        }

        return toHexLE(finalState[0])
                + toHexLE(finalState[1])
                + toHexLE(finalState[2])
                + toHexLE(finalState[3]);
    }
}
//./src/test.txt