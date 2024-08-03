package symmetriccipher;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import util.Files;
import util.Util;

public class SymmetricCipher06 {

	
	public static void main(String[] args) throws Exception{
		
		SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey();
		SymmetricCipher cipher = new SymmetricCipher(secretKey, "DES/ECB/PKCS5Padding");
		
		cipher.encryptFile("src/files/pacr42-2.pdf", "src/files/pacr42-2.pdf.encrypted");
		
		System.out.println();
		Util.saveObject(secretKey, "secretKey.key");
		System.out.println("The secret key has been saved");
		
		Files.pause(150);
		
		SecretKey secretKey2 = (SecretKey) Util.loadObject("secretKey.key");
		System.out.println("La llave simétrica ha sido recuperada");
		
		cipher.desencryptFile("src/files/pacr42-2.pdf.encrypted", "src/files/pacr42-2.plain.pdf", secretKey2);
		System.out.println("El archivo ha sido desencriptado");

	}
	
}
