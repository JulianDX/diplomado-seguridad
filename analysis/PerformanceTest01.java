package analysis;

import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import publickeycipher.PublicKeyCipher;
import symmetriccipher.SymmetricCipher;
import util.Util;

public class PerformanceTest01 {
	
	public static void main(String[] args) throws Exception {
		// Variables to store times
		long time1mbEncDes, time1mbDecDes, time10mbEncDes, time10mbDecDes, time100mbEncDes, time100mbDecDes;
		long time1mbEncRSA1024, time1mbDecRSA1024, time10mbEncRSA1024, time10mbDecRSA1024, time100mbEncRSA1024, time100mbDecRSA1024;
		long time1mbEncRSA2048, time1mbDecRSA2048, time10mbEncRSA2048, time10mbDecRSA2048, time100mbEncRSA2048, time100mbDecRSA2048;
		
		SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey();
		SymmetricCipher cipher = new SymmetricCipher(secretKey, "DES/ECB/PKCS5Padding");

		// Encr DES 1 MB
		long startTime = System.nanoTime();
		cipher.encryptFile("src/performanceTest/1mb.pdf", "src/performanceTest/1mb.pdf.encrypted");
		time1mbEncDes = System.nanoTime() - startTime;

		Util.saveObject(secretKey, "src/performanceTest/secretKey.key");

		// Desencr DES 1 MB
		startTime = System.nanoTime();
		SecretKey secretKey2 = (SecretKey) Util.loadObject("src/performanceTest/secretKey.key");
		cipher.desencryptFile("src/performanceTest/1mb.pdf.encrypted", "src/performanceTest/1mb.plain.pdf", secretKey2);
		time1mbDecDes = System.nanoTime() - startTime;

		// Encr DES 10 MB
		startTime = System.nanoTime();
		cipher.encryptFile("src/performanceTest/10mb.pdf", "src/performanceTest/10mb.pdf.encrypted");
		time10mbEncDes = System.nanoTime() - startTime;

		Util.saveObject(secretKey, "src/performanceTest/secretKey.key");

		// Desencr DES 10 MB
		startTime = System.nanoTime();
		cipher.desencryptFile("src/performanceTest/10mb.pdf.encrypted", "src/performanceTest/10mb.plain.pdf", secretKey2);
		time10mbDecDes = System.nanoTime() - startTime;

		// Encr DES 100 MB
		startTime = System.nanoTime();
		cipher.encryptFile("src/performanceTest/100mb.pdf", "src/performanceTest/100mb.pdf.encrypted");
		time100mbEncDes = System.nanoTime() - startTime;

		Util.saveObject(secretKey, "src/performanceTest/secretKey.key");

		// Desencr DES 100 MB
		startTime = System.nanoTime();
		cipher.desencryptFile("src/performanceTest/100mb.pdf.encrypted", "src/performanceTest/100mb.plain.pdf", secretKey2);
		time100mbDecDes = System.nanoTime() - startTime;

		// Encr RSA 1024 1 MB
	    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();
        
        PublicKeyCipher cipher2 = new PublicKeyCipher("RSA");

        startTime = System.nanoTime();
        cipher2.encryptFile("src/performanceTest/1mb.pdf", publicKey);
        time1mbEncRSA1024 = System.nanoTime() - startTime;

        // Desencr RSA 1024 1 MB
        startTime = System.nanoTime();
        cipher2.decryptFile("src/performanceTest/1mb.pdf.rsa", privateKey);
        time1mbDecRSA1024 = System.nanoTime() - startTime;

        // Encr RSA 1024 10 MB
        pair = keyGen.generateKeyPair();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();
        
        cipher2 = new PublicKeyCipher("RSA");

        startTime = System.nanoTime();
        cipher2.encryptFile("src/performanceTest/10mb.pdf", publicKey);
        time10mbEncRSA1024 = System.nanoTime() - startTime;

        // Desencr RSA 1024 10 MB
        startTime = System.nanoTime();
        cipher2.decryptFile("src/performanceTest/10mb.pdf.rsa", privateKey);
        time10mbDecRSA1024 = System.nanoTime() - startTime;

        // Encr RSA 1024 100 MB
        pair = keyGen.generateKeyPair();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();
        
        cipher2 = new PublicKeyCipher("RSA");

        startTime = System.nanoTime();
        cipher2.encryptFile("src/performanceTest/100mb.pdf", publicKey);
        time100mbEncRSA1024 = System.nanoTime() - startTime;

        // Desencr RSA 1024 100 MB
        startTime = System.nanoTime();
        cipher2.decryptFile("src/performanceTest/100mb.pdf.rsa", privateKey);
        time100mbDecRSA1024 = System.nanoTime() - startTime;

        // Encr RSA 2048 1 MB
        keyGen.initialize(2048);
        pair = keyGen.generateKeyPair();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();
        
        cipher2 = new PublicKeyCipher("RSA");

        startTime = System.nanoTime();
        cipher2.encryptFile("src/performanceTest/1mb.pdf", publicKey);
        time1mbEncRSA2048 = System.nanoTime() - startTime;

        // Desencr RSA 2048 1 MB
        startTime = System.nanoTime();
        cipher2.decryptFile("src/performanceTest/1mb.pdf.rsa", privateKey);
        time1mbDecRSA2048 = System.nanoTime() - startTime;

        // Encr RSA 2048 10 MB
        pair = keyGen.generateKeyPair();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();
        
        cipher2 = new PublicKeyCipher("RSA");

        startTime = System.nanoTime();
        cipher2.encryptFile("src/performanceTest/10mb.pdf", publicKey);
        time10mbEncRSA2048 = System.nanoTime() - startTime;

        // Desencr RSA 2048 10 MB
        startTime = System.nanoTime();
        cipher2.decryptFile("src/performanceTest/10mb.pdf.rsa", privateKey);
        time10mbDecRSA2048 = System.nanoTime() - startTime;

        // Encr RSA 2048 100 MB
        pair = keyGen.generateKeyPair();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();
        
        cipher2 = new PublicKeyCipher("RSA");

        startTime = System.nanoTime();
        cipher2.encryptFile("src/performanceTest/100mb.pdf", publicKey);
        time100mbEncRSA2048 = System.nanoTime() - startTime;

        // Desencr RSA 2048 100 MB
        startTime = System.nanoTime();
        cipher2.decryptFile("src/performanceTest/100mb.pdf.rsa", privateKey);
        time100mbDecRSA2048 = System.nanoTime() - startTime;

        // Write results to CSV
        try (FileWriter writer = new FileWriter("performance_results.csv")) {
            writer.append("tamaño,encr des,desencr des,encr rsa1024, desencr rsa1024,Encr rsa 2048,Desencr RSA 2048\n");
            writer.append("1mb,").append(String.valueOf(time1mbEncDes / 1000000)).append(',')
                  .append(String.valueOf(time1mbDecDes / 1000000)).append(',')
                  .append(String.valueOf(time1mbEncRSA1024 / 1000000)).append(',')
                  .append(String.valueOf(time1mbDecRSA1024 / 1000000)).append(',')
                  .append(String.valueOf(time1mbEncRSA2048 / 1000000)).append(',')
                  .append(String.valueOf(time1mbDecRSA2048 / 1000000)).append('\n');
            writer.append("10mb,").append(String.valueOf(time10mbEncDes / 1000000)).append(',')
                  .append(String.valueOf(time10mbDecDes / 1000000)).append(',')
                  .append(String.valueOf(time10mbEncRSA1024 / 1000000)).append(',')
                  .append(String.valueOf(time10mbDecRSA1024 / 1000000)).append(',')
                  .append(String.valueOf(time10mbEncRSA2048 / 1000000)).append(',')
                  .append(String.valueOf(time10mbDecRSA2048 / 1000000)).append('\n');
            writer.append("100mb,").append(String.valueOf(time100mbEncDes / 1000000)).append(',')
                  .append(String.valueOf(time100mbDecDes / 1000000)).append(',')
                  .append(String.valueOf(time100mbEncRSA1024 / 1000000)).append(',')
                  .append(String.valueOf(time100mbDecRSA1024 / 1000000)).append(',')
                  .append(String.valueOf(time100mbEncRSA2048 / 1000000)).append(',')
                  .append(String.valueOf(time100mbDecRSA2048 / 1000000)).append('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
