package signature;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;

import util.Util;

public class SignerTester01 {
	

public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException { 
	String algorithm = "RSA";
	KeyPairGenerator keypairGenerator = KeyPairGenerator.getInstance(algorithm);
	keypairGenerator.initialize(2048);
	KeyPair keyPair = keypairGenerator.generateKeyPair();
	PublicKey publicKey = keyPair.getPublic();
	PrivateKey privateKey = keyPair.getPrivate();
	String message = "Fundamentos de seguridad digital";
	byte[] digitalsignature = Signer.signMessage(message, "SHA256withRSA", privateKey);
	System.out.println(Util.byteArrayToHexString (digitalsignature, ""));
	boolean isVerified = Signer.verifyMessageSignature (message,"SHA256withRSA",publicKey , digitalsignature); 
	System.out.println("Firma verificada: " + isVerified);
}

}
