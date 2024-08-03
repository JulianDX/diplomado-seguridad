package signature;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;

public class SignerTester03 {
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
		String algorithm = "RSA";
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
		keyPairGenerator.initialize(2048);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		PublicKey publicKey = keyPair.getPublic(); 
		PrivateKey privateKey = keyPair.getPrivate();
		Signer.generateSignaturesFile("src/signFiles", "src/signFiles/firma.txt", "SHA256withRSA", privateKey);
		System.out.println("Finalización correcta firmas");
		Signer.verifySignaturesFile("src/signFiles/firma.txt","src/signFiles", "SHA256withRSA", publicKey);
	}

}
