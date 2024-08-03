package publickeycipher;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import util.Base64;
import util.Util;

public class PublicKeyCipherTester03 {
	
    public static void saveStringToFile(String content, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, ClassNotFoundException, InterruptedException {
        
        String algorithm = "RSA";
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        PublicKeyCipher cipher = new PublicKeyCipher(algorithm);
        String clearText = "In public key cryptography, the same key is used to encrypt and decrypt text";
        System.out.println(clearText);
        byte[] encryptedText = cipher.encryptMessage(clearText, publicKey);
        
        String publicK = Util.getKey(publicKey, "PUBLIC");
        String privateK = Util.getKey(privateKey, "PRIVATE");
        
        // Asegúrate de que el directorio crypfiles exista
        Path crypfilesPath = Paths.get("crypfiles");
        if (!Files.exists(crypfilesPath)) {
            Files.createDirectories(crypfilesPath);
        }
        
        saveStringToFile(publicK, "src/crypfiles/publicKey.txt");
        System.out.println("Llave pública guardada");
        
        saveStringToFile(privateK, "src/crypfiles/privateKey.txt");
        System.out.println("Llave privada guardada");
        
        saveStringToFile(Base64.encode(encryptedText), "src/crypfiles/encryptedText.txt");
        System.out.println("Texto encriptado guardado");
    }
	
}
