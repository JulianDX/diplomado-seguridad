package publickeycipher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class PublicKeyCipherTester11 {
	
	public static void main(String[] args) throws Exception {
        // Generar claves RSA de 2048 bits
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();

        PublicKeyCipher cipher = new PublicKeyCipher("RSA");

        // Ruta del archivo de texto a encriptar
        String inputFilePath = "src/performanceTest/10mb.pdf";

        // Encriptar el archivo
        cipher.encryptFile(inputFilePath, publicKey);
        System.out.println("El archivo ha sido encriptado y guardado como " + inputFilePath + ".rsa");

        // Desencriptar el archivo
        String encryptedFilePath = inputFilePath + ".rsa";
        cipher.decryptFile(encryptedFilePath, privateKey);
        System.out.println("El archivo ha sido desencriptado y guardado como " + inputFilePath.replace(".pdf", ".plain.pdf"));
    }

}
