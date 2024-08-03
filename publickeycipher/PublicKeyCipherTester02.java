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

import util.Util;

public class PublicKeyCipherTester02 {
	
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
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        String publicK = Util.getKey(publicKey, "PUBLIC");
        String privateK = Util.getKey(privateKey, "PRIVATE");
        
        System.out.println(publicK);
        System.out.println(privateK);
        
    }

}
