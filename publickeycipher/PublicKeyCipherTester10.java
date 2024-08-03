package publickeycipher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class PublicKeyCipherTester10 {
	
	public static void main(String[] args) throws Exception {
        // Generar claves RSA de 2048 bits
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();
        
        PublicKeyCipher cipher = new PublicKeyCipher("RSA");

        // Encriptar archivo
        cipher.encryptTextFile("src/files/text.txt", publicKey);

        // Desencriptar archivo
        cipher.decryptTextFile("src/files/text.txt.rsa", privateKey);
    }

}
