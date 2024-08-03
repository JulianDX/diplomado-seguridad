package publickeycipher;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import util.Person;
import util.Util;

public class PublicKeyCipherTester05 {
	
	public static void main(String[] args) throws 
	NoSuchAlgorithmException,
	NoSuchPaddingException, 
	InvalidKeyException, 
	IllegalBlockSizeException,
	BadPaddingException, IOException, ClassNotFoundException, InterruptedException{
		
		String algorithm = "RSA";
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
		keyPairGenerator.initialize(1024);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		PublicKeyCipher cipher = new PublicKeyCipher(algorithm);
		
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		
		
		Person person = new Person("Julian", 25, 1.78);
		
		byte[] encryptedObject = cipher.encryptObject(person, publicKey);
		System.out.println("Objeto encriptado:");
		System.out.println(Util.byteArrayToHexString(encryptedObject, " "));
		System.out.println();
		System.out.println("Objeto desencriptado:");
		Person personDecrypted = (Person) cipher.decryptObject(encryptedObject, privateKey);
		System.out.println(personDecrypted);
		
		
		
	}

}
