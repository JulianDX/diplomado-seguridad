package publickeycipher;

import util.Util;

public class PublicKeyCipherTester08 {
	
	public static void main(String[] args) throws Exception {
        // Generar un arreglo de bytes de prueba
        byte[] original = new byte[1050];
        for (int i = 0; i < original.length; i++) {
            original[i] = (byte) i;
        }

        // Imprimir el arreglo original
        System.out.println("Arreglo original:");
        for (byte b : original) {
            System.out.print(b + " ");
        }
        System.out.println("\n");

        // Dividir el arreglo en fragmentos de 1024 bytes
        int chunkSize = 1024;
        byte[][] chunks = Util.split(original, chunkSize);

        // Imprimir los fragmentos
        System.out.println("Fragmentos:");
        for (int i = 0; i < chunks.length; i++) {
            System.out.println("Fragmento " + (i + 1) + ":");
            for (byte b : chunks[i]) {
                System.out.print(b + " ");
            }
            System.out.println("\n");
        }

        // Unir los fragmentos de nuevo en un solo arreglo
        byte[] joined = Util.join(chunks);

        // Imprimir el arreglo unido
        System.out.println("Arreglo unido:");
        for (byte b : joined) {
            System.out.print(b + " ");
        }
        System.out.println("\n");

        // Verificar si el arreglo unido es igual al original
        boolean areEqual = java.util.Arrays.equals(original, joined);
        System.out.println("¿El arreglo unido es igual al original? " + areEqual);
    }

}
