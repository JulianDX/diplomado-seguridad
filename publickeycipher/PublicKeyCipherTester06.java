package publickeycipher;

import util.Util;

public class PublicKeyCipherTester06 {
	
    public static void main(String[] args) {
        byte[] input = new byte[12]; // Ejemplo de arreglo de bytes
        int chunkSize = 7; // Tamaño del fragmento

        byte[][] chunks = Util.split(input, chunkSize);

        System.out.println("Total de fragmentos: " + chunks.length);
        for (int i = 0; i < chunks.length; i++) {
            System.out.println("Ancho fragmento " + (i + 1) + ": " + chunks[i].length);
        }
    }

}
