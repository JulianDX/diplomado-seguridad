package publickeycipher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import util.Base64;

public class PublicKeyCipherTester04 {
	
    public static String getSecondLine(String filePath) {
        String secondLine = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // Leer y descartar la primera línea
            secondLine = reader.readLine(); // Leer la segunda línea
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return secondLine;
    }
    
    public static PrivateKey getPrivateKeyFromBytes(byte[] keyBytes) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Cambia "RSA" al algoritmo adecuado si es necesario
        return keyFactory.generatePrivate(keySpec);
    }
    
    public static void readFileByLine(String filePath, Key privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        BufferedReader reader = null;
        PublicKeyCipher cipher = new PublicKeyCipher("RSA");
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                byte[] message = Base64.decode(line);
                String clearText = cipher.decryptMessage(message, privateKey);
        		System.out.println("Texto recuperado: "+clearText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String filePath = "src/crypfiles/publicKey.txt";
        String publicKey = getSecondLine(filePath);
        String filePath2 = "src/crypfiles/privateKey.txt";
        String privateKey = getSecondLine(filePath2);
        
        String filePath3 = "src/crypfiles/encryptedText.txt";
        
        if (publicKey != null) {
            System.out.println("Llave pública recuperada: " + publicKey);
            System.out.println("Llave privada recuperada: " + privateKey);
            byte[] publicKeyFormat = Base64.decode(privateKey);
            Key privateKeyFinal = (Key) getPrivateKeyFromBytes(publicKeyFormat);
            readFileByLine(filePath3, privateKeyFinal);
        } else {
            System.out.println("No se pudo leer la segunda línea.");
        }
       
        
    }
	
}
