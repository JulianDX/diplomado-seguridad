package publickeycipher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import util.Util;

public class PublicKeyCipherTester09 {
	
	public static void main(String[] args) throws Exception {
        // Supongamos que tenemos el tamaño máximo permitido para RSA de 1024 bits (127 bytes + 50 bytes de padding)
        int maxSize = 150; // mayor que el tamaño máximo permitido
        byte[] largeData = new byte[maxSize]; // Generamos un byte[] mayor al límite
        // Llenamos el byte[] con datos aleatorios (esto se haría en la práctica, pero aquí se hace estáticamente)
        for (int i = 0; i < largeData.length; i++) {
            largeData[i] = (byte) (i % 256); // Solo un ejemplo de llenado
        }
        // Dividir el byte[] en fragmentos de tamaño manejable
        int chunkSize = 50; // El tamaño de los fragmentos a encriptar
        byte[][] chunks = Util.split(largeData, chunkSize); // Utilizando el método split() de la clase Util
        // Generamos claves para cifrado
        String algorithm = "RSA";
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        // Encriptar cada fragmento
        PublicKeyCipher cipher = new PublicKeyCipher(algorithm);
        byte[][] encryptedChunks = new byte[chunks.length][];
        for (int i = 0; i < chunks.length; i++) {
            encryptedChunks[i] = cipher.encryptObject(chunks[i], publicKey); // Encriptar el fragmento
        }
        // Desencriptar cada fragmento
        byte[][] decryptedChunks = new byte[encryptedChunks.length][];
        for (int i = 0; i < encryptedChunks.length; i++) {
            decryptedChunks[i] = (byte[]) cipher.decryptObject(encryptedChunks[i], privateKey); // Desencriptar el fragmento
        }
        // Unir los fragmentos desencriptados
        byte[] originalData = Util.join(decryptedChunks); // Utilizando el método join() de la clase Util
        // Comprobamos si se obtuvo el byte[] original
        System.out.println("El tamaño de los datos originales es: " + largeData.length);
        System.out.println("El tamaño de los datos obtenidos es: " + originalData.length);
        
     // Verificar si el byte[] desencriptado es igual al original
        boolean areEqual = java.util.Arrays.equals(originalData, largeData);
        System.out.println("¿El byte[] desencriptado es igual al original? " + areEqual);
    }

}
