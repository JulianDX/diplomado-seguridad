package signature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import util.Base64;

public class Signer {
	

	public static byte[] signMessage(String message, String algorithm, PrivateKey privateKey) throws NoSuchAlgorithmException,
		InvalidKeyException,
		SignatureException {
		Signature signature = Signature.getInstance(algorithm);
		signature.initSign (privateKey);
		signature.update(message.getBytes());
		return signature.sign();
	}
	
	public static boolean verifyMessageSignature (String message, String algorithm, PublicKey publicKey, byte[] digitalsignature) throws
	NoSuchAlgorithmException,
	InvalidKeyException,
	SignatureException {
		Signature signature = Signature.getInstance(algorithm); 
		signature.initVerify (publicKey);
		signature.update(message.getBytes());
		return signature.verify(digitalsignature);
	}
	
	public static byte[] signFile(String filename, String algorithm, PrivateKey privateKey) throws IOException,
	NoSuchAlgorithmException,
	SignatureException,
	InvalidKeyException {
		File file = new File(filename);
		byte[] fileBytes = Files.readAllBytes (file.toPath());
		Signature signature = Signature.getInstance(algorithm); 
		signature.initSign (privateKey); 
		signature.update(fileBytes);
		return signature.sign();
	}
	
	public static boolean verifyFileSignature(String filename, String algorithm, PublicKey publicKey, byte[] digitalsignature)
		throws
		IOException,
		InvalidKeyException,
		NoSuchAlgorithmException,
		SignatureException {
			File file = new File(filename);
			byte[] fileBytes = Files.readAllBytes (file.toPath());
			Signature signature = Signature.getInstance(algorithm); 
			signature.initVerify (publicKey); 
			signature.update(fileBytes);
			return signature.verify (digitalsignature);
	}
	
	public static void generateSignaturesFile(String folderPath, String signaturesFilePath, String algorithm, PrivateKey privateKey) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        
        if (files == null || files.length == 0) {
            System.out.println("No hay archivos en la carpeta especificada.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(signaturesFilePath))) {
            for (File file : files) {
                if (file.isFile()) {
                	System.out.println(file.getAbsolutePath());
                    byte[] digitalSignature = Signer.signFile(file.getAbsolutePath(), algorithm, privateKey);
                    String signatureBase64 = Base64.encode(digitalSignature); 
                    writer.write(file.getName() + "|SEPARADOR|" + signatureBase64); 
                    writer.newLine();
                }
            }
        }
    }
	
	public static void verifySignaturesFile(String signaturesFilePath, String folderPath, String algorithm, PublicKey publicKey) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
	    try (BufferedReader reader = new BufferedReader(new FileReader(signaturesFilePath))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split("\\|SEPARADOR\\|"); 
	            if (parts.length != 2) {
	                System.out.println("Formato incorrecto en la línea: " + line);
	                continue;
	            }

	            String fileName = folderPath + File.separator + parts[0]; // Usar la ruta completa
	            byte[] signature = Base64.decode(parts[1]); 

	            boolean isVerified = Signer.verifyFileSignature(fileName, algorithm, publicKey, signature);
	            System.out.println("Firma verificada para el archivo " + fileName + ": " + isVerified);
	        }
	    }
	}


}
