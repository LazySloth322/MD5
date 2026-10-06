import java.nio.charset.StandardCharsets;

public class MD5 {
    int A = 0;
    int B = 0;
    int C = 0;
    int D = 0;

    private byte[] paddingFiller(byte[] data){//448+64 bits = 56+8 bytes
        long len = data.length;//long type is 64bit

        byte[] lenInBytes = new byte[8];//little endian order
        lenInBytes[0] = (byte) (len);
        lenInBytes[1] = (byte) (len >> 8);
        lenInBytes[2] = (byte) (len >> 16);
        lenInBytes[3] = (byte) (len >> 24);
        lenInBytes[4] = (byte) (len >> 32);
        lenInBytes[5] = (byte) (len >> 40);
        lenInBytes[6] = (byte) (len >> 48);
        lenInBytes[7] = (byte) (len >> 56);

        int blocks = data.length/56+1; //in case if (payload)%64 more than 56
        byte[] paddedArray = new byte[blocks*64];

        System.arraycopy(data,0,paddedArray,0,(int)len);

        paddedArray[(int)len] = (byte)0x80;
        for(int i = 0; i<lenInBytes.length; i++){
            paddedArray[blocks*64-1-i]=lenInBytes[lenInBytes.length-1-i];
        }

        return paddedArray;
    }

    private void blockProcessor(){

    }

    public void run(String input){
        byte[] data = input.getBytes(StandardCharsets.UTF_8);

        data = paddingFiller(data);

        //cycle for each 16 bytes


        System.out.print("\n");
        for(int i=0;i<data.length;i++){
            System.out.printf("%02x ",(data[i] & 0xFF));
        }
    }
}
//./src/test.txt