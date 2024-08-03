package symmetriccipher;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import util.Util;

public class SymmetricCipherTester05 {
	
	public static void main(String[] args) throws NoSuchAlgorithmException,
	NoSuchPaddingException, 
	InvalidKeyException, 
	IllegalBlockSizeException,
	BadPaddingException, IOException, ClassNotFoundException, InterruptedException{
		
		SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey();
		SymmetricCipher cipher = new SymmetricCipher(secretKey, "DES/ECB/PKCS5Padding");
		
		cipher.encryptTextFile("src/crypfiles/text.txt", "src/sendClientServer/text.txt.encrypted");
		
		System.out.println();
		Util.saveObject(secretKey, "src/sendClientServer/secretKey.key");
		System.out.println("The secret key has been saved");
		
		SecretKey secretKey2 = (SecretKey) Util.loadObject("src/sendClientServer/secretKey.key");
		System.out.println("La llave simétrica ha sido recuperada");
		
		cipher.desencryptTextFile("src/sendClientServer/text.txt.encrypted", "src/files/text.plain.txt", secretKey2);
		System.out.println("El archivo ha sido desencriptado");

	}

}
