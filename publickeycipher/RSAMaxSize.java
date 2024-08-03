package publickeycipher;

import util.Util;

public class RSAMaxSize {
    
    public static void main(String[] args) {
        int[] keySizes = {1024, 2048, 3072, 4096};
        for (int keySize : keySizes) {
            // RSA puede encriptar un tamaño máximo de (keySize/8) - 11 bytes (PKCS#1 v1.5)
            int maxDataSize = (keySize / 8) - 11; // Ajustar para el relleno
            String randomString = Util.generateRandomString(maxDataSize);
            System.out.println("Tamaño de llave RSA: " + keySize + " bits");
            System.out.println("Cadena aleatoria generada: " + randomString);
            System.out.println("Tamaño de la cadena: " + randomString.length());
            System.out.println();
        }
    }
  
}
