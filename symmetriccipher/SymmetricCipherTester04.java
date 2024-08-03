package symmetriccipher;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import util.Util;

public class SymmetricCipherTester04 {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args)throws 
	NoSuchAlgorithmException,
	NoSuchPaddingException, 
	InvalidKeyException, 
	IllegalBlockSizeException,
	BadPaddingException,
	IOException,
	ClassNotFoundException
	{
		
		SecretKey secretKey = null;
		
		secretKey = KeyGenerator.getInstance("DES").generateKey();
		
		SymmetricCipher cipher = new SymmetricCipher(secretKey, "DES/ECB/PKCS5Padding");
		
		ArrayList<String> clearObject = new ArrayList<String>();
		
		byte[] encryptedObject = null;
		
		clearObject.add("Julian");
		clearObject.add("Fernando");
		clearObject.add("Roa");
		clearObject.add("Palacio");
		clearObject.add("Seguridad");
		
		System.out.println(clearObject);
		
		encryptedObject = cipher.encryptObject(clearObject);
		
		System.out.println(Util.byteArrayToHexString(encryptedObject, " "));
		
		clearObject = (ArrayList<String>) cipher.decryptObject(encryptedObject);
		System.out.println(clearObject);
		
	}

}
