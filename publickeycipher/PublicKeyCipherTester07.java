package publickeycipher;

import util.Util;

public class PublicKeyCipherTester07 {
	
    public static void main(String[] args) {
        byte[] input1 = {1, 2, 3, 4};
        byte[] input2 = {5, 6, 7, 8};
        byte[] input3 = {9, 10, 11, 12};

        byte[][] chunks = {input1, input2, input3};

        byte[] joined = Util.join(chunks);

        System.out.println("Arreglo unido: ");
        for (byte b : joined) {
            System.out.print(b + " ");
        }
    }

}
