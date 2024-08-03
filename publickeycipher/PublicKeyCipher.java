package publickeycipher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import util.Base64;
import util.Util;

public class PublicKeyCipher {
	
	private Cipher cipher;

	public PublicKeyCipher(String algorithm) throws
		NoSuchAlgorithmException,
		NoSuchPaddingException {
		
		cipher = Cipher.getInstance(algorithm);
		
	}
	
	public byte[] encryptMessage(String input, Key key) throws
		InvalidKeyException,
		IllegalBlockSizeException,
		BadPaddingException
	{
		byte[] cipherText = null;
		byte[] clearText = input.getBytes();
		
		cipher.init(Cipher.ENCRYPT_MODE, key);
		cipherText = cipher.doFinal(clearText);
		
		return cipherText;
		
	}
	
	public String decryptMessage(byte[] input, Key key) throws
		InvalidKeyException,
		IllegalBlockSizeException,
		BadPaddingException
	{
		String output = "";
		
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] clearText = cipher.doFinal(input);
		
		output = new String(clearText);
		
		return output;
	
	}
	
	  public void encryptTextFile(String inputFilePath, Key publicKey) throws Exception {
	        BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
	        String encryptedFilePath = inputFilePath + ".rsa";
	        BufferedWriter writer = new BufferedWriter(new FileWriter(encryptedFilePath));

	        String line;
	        while ((line = reader.readLine()) != null) {
	            byte[] encryptedLine = encryptMessage(line, publicKey);
	            String encodedLine = Base64.encode(encryptedLine);
	            writer.write(encodedLine);
	            writer.newLine();
	        }

	        reader.close();
	        writer.close();
	        System.out.println("Archivo encriptado guardado como: " + encryptedFilePath);
	    }
	  
	    public void decryptTextFile(String encryptedFilePath, Key privateKey) throws Exception {
	        BufferedReader reader = new BufferedReader(new FileReader(encryptedFilePath));
	        String decryptedFilePath = encryptedFilePath.replace(".rsa", ".plain");
	        BufferedWriter writer = new BufferedWriter(new FileWriter(decryptedFilePath));

	        String line;
	        while ((line = reader.readLine()) != null) {
	            byte[] decodedLine = Base64.decode(line);
	            String decryptedLine = decryptMessage(decodedLine, privateKey);
	            writer.write(decryptedLine);
	            writer.newLine();
	        }

	        reader.close();
	        writer.close();
	        System.out.println("Archivo desencriptado guardado como: " + decryptedFilePath);
	    }
	
	public byte[] encryptObject(Object input, Key key)throws 
		InvalidKeyException,
		IOException,
		IllegalBlockSizeException,
		BadPaddingException{
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] clearObject = Util.objectToByteArray(input);
			byte[] cipherObject = cipher.doFinal(clearObject);
			
			return cipherObject;
	}
	
	private int getBlockSize(Key publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
        int keySize = publicKeySpec.getModulus().bitLength();
        
        if (keySize == 1024) {
            return 117;
        } else if (keySize == 2048) {
            return 245;
        } else {
            throw new IllegalArgumentException("Unsupported key size: " + keySize);
        }
    }
	
	public void encryptFile(String inputFilePath, Key publicKey) throws Exception {
        try (
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFilePath));
            BufferedWriter bw = new BufferedWriter(new FileWriter(inputFilePath + ".rsa"))
        ) {
            int blockSize = getBlockSize(publicKey); // Obtener el tamaño del bloque basado en la longitud de la llave
            byte[] block = new byte[blockSize];
            int bytesRead;
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            while ((bytesRead = bis.read(block)) != -1) {
                byte[] encryptedBytes = cipher.doFinal(block, 0, bytesRead);
                String encodedString = Base64.encode(encryptedBytes);
                bw.write(encodedString);
                bw.newLine(); // Agregar nueva línea entre bloques
            }
        }
    }
	
	public void decryptFile(String inputFilePath, Key privateKey) throws Exception {
        try (
            BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(inputFilePath.replace(".rsa", ".plain.pdf")))
        ) {
            String line;
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            while ((line = br.readLine()) != null) {
                byte[] lineDecoded = Base64.decode(line);
                byte[] decryptedBytes = cipher.doFinal(lineDecoded);
                bos.write(decryptedBytes);
            }
        }
    }
	
	public Object decryptObject(byte[] input, Key key)throws 
	InvalidKeyException,
	IOException,
	IllegalBlockSizeException,
	BadPaddingException, ClassNotFoundException{
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] clearText = cipher.doFinal(input);
		Object output = Util.byteArrayToObject(clearText);
		
		return output;
}

}
