package analysis;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import publickeycipher.PublicKeyCipher;

public class Test {
	
	public static void main(String[] args) throws Exception {
		    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
	        keyGen.initialize(1024);
	        KeyPair pair = keyGen.generateKeyPair();
	        PublicKey publicKey = pair.getPublic();
	        PrivateKey privateKey = pair.getPrivate();
	        
	        PublicKeyCipher cipher2 = new PublicKeyCipher("RSA");

	        long startTime = System.nanoTime();
	        cipher2.encryptFile("src/performanceTest/1mb.pdf", publicKey);
			long endtime = System.nanoTime() - startTime;
			
			System.out.println(endtime);

	        startTime = System.nanoTime();
	        cipher2.decryptFile("src/performanceTest/1mb.pdf.rsa", privateKey);
	        endtime = System.nanoTime() - startTime;
	        
			System.out.println(endtime);
	}

}
