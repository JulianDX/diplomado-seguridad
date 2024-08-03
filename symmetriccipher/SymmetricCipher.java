package symmetriccipher;

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
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import util.Base64;
import util.Util;

public class SymmetricCipher {

	private SecretKey secretKey;
	private Cipher cipher;
	
	public SymmetricCipher(SecretKey secretKey, String transformation) throws NoSuchAlgorithmException, NoSuchPaddingException {
		super();
		this.secretKey = secretKey;
		cipher = Cipher.getInstance(transformation);
	}
	
	public byte[] encryptMessage(String input) throws InvalidKeyException,
	IllegalBlockSizeException, BadPaddingException{
		byte[] clearText = input.getBytes();
		byte[] cipherText = input.getBytes();
		
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		cipherText = cipher.doFinal(clearText);
		
		return cipherText;
	}
	
	public String decryptMessage(byte[] input) throws InvalidKeyException,
	IllegalBlockSizeException, BadPaddingException{
		
		String output = "";
		
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] clearText = cipher.doFinal(input);
		output = new String(clearText);
		
		return output;
	}
	
	public byte[] encryptObject(Object input) throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		byte[] cipherObject = null;
		byte[] clearObject = null;
		
		clearObject = Util.objectToByteArray(input);
		
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		
		cipherObject = cipher.doFinal(clearObject);
		
		return cipherObject;
		
	}
	
	public Object decryptObject(byte[] input) throws
	InvalidKeyException,
	IllegalBlockSizeException,
	BadPaddingException,
	ClassNotFoundException,
	IOException
	{
		Object output = null;
		
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] clearObject = cipher.doFinal(input);
		
		output = Util.byteArrayToObject(clearObject);
		
		
		return output;
	}
	
	public void encryptTextFile(String inputFilePath, String outputFilePath) 
            throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        System.out.println("Archivo encriptado:\n");

        try (
            BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))
        ) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (!firstLine) {
                    bw.newLine();
                } else {
                    firstLine = false;
                }
                
                // Encriptar la línea leída
                byte[] encryptedText = encryptMessage(line);
                String lineEncoded = Base64.encode(encryptedText);

                // Escribir la línea encriptada en el archivo de salida
                bw.write(lineEncoded);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	public void desencryptTextFile(String inputFilePath, String outputFilePath, SecretKey secretKey2) 
            throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		System.out.println("Mensaje descifrado:");

        try (
            BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))
        ) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (!firstLine) {
                    bw.newLine();
                } else {
                    firstLine = false;
                }
                
                // Encriptar la línea leída
                byte[] lineDecoded = Base64.decode(line);
                
                String decryptedLine = decryptMessage(lineDecoded);

                // Escribir la línea encriptada en el archivo de salida
                bw.write(decryptedLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void encryptFile(String inputFilePath, String outputFilePath) 
	        throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	    
	    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

	    try (
	        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFilePath));
	        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))
	    ) {
	        byte[] block = new byte[1024]; // Tamaño del bloque a leer
	        int bytesRead;
	        
	        while ((bytesRead = bis.read(block)) != -1) {
	            // Encriptar el bloque leído
	            byte[] encryptedText = cipher.doFinal(block, 0, bytesRead);
	            String lineEncoded = Base64.encode(encryptedText);
	            bw.write(lineEncoded);
	            bw.newLine(); // Agregar nueva línea entre bloques
	        }
	    }
	}

	
	public void desencryptFile(String inputFilePath, String outputFilePath, SecretKey secretkey2) 
	        throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
	    
	    cipher.init(Cipher.DECRYPT_MODE, secretKey);

	    try (
	        BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
	        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFilePath))
	    ) {
	        String line;

	        while ((line = br.readLine()) != null) {
	            // Decodificar la línea en Base64
	            byte[] lineDecoded = Base64.decode(line);

	            // Desencriptar el bloque
	            byte[] decryptedBytes = cipher.doFinal(lineDecoded);
	            bos.write(decryptedBytes); // Escribir en el archivo de salida
	        }
	    }
	}




    
	public static void pause(int miliseconds) throws Exception {
		Thread.sleep(miliseconds);
	}
	
	
	
}
