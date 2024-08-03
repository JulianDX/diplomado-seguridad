package crypnetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import publickeycipher.PublicKeyCipher;
import symmetriccipher.SymmetricCipher;
import util.Base64;
import util.Files;
import util.Util;

public class EchoServer {
	public static final int PORT = 4000;

	private ServerSocket listener;
	private Socket serverSideSocket;
	
	private int port;
	
	public EchoServer() {
		System.out.println("Julian Roa Palacio - May 20/2024");
		System.out.println("File transfer server is running on port: " + this.port);
	}

	public EchoServer(int port) {
		this.port = port;
		System.out.println("Julian Roa Palacio - May 20/2024");
		System.out.println("File transfer server is running on port: " + this.port);
	}
	
	void init() throws Exception {
		listener = new ServerSocket(PORT);

		while (true) {
			serverSideSocket = listener.accept();

			protocol(serverSideSocket);
		}
	}
    
    public static PrivateKey getPrivateKeyFromBytes(byte[] keyBytes) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Cambia "RSA" al algoritmo adecuado si es necesario
        return keyFactory.generatePrivate(keySpec);
    }
    
    public static void readFileByLine(String filePath, Key privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        BufferedReader reader = null;
        PublicKeyCipher cipher = new PublicKeyCipher("RSA");
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                byte[] message = Base64.decode(line);
                String clearText = cipher.decryptMessage(message, privateKey);
        		System.out.println("Texto recuperado: "+clearText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	public void protocol(Socket socket) throws Exception {
		 System.out.println("Current directory: " + new File(".").getAbsolutePath());
		 try {
			Files.receiveFolder("src/receiveClientServer", socket);
			SecretKey secretKey2 = (SecretKey) Util.loadObject("src/receiveClientServer/secretKey.key");
			SymmetricCipher cipher = new SymmetricCipher(secretKey2, "DES/ECB/PKCS5Padding");
			cipher.desencryptTextFile("src/sendClientServer/text.txt.encrypted", "src/receiveClientServer/text.plain.txt", secretKey2);
			cipher.desencryptFile("src/sendClientServer/pacr42-2.pdf.encrypted", "src/receiveClientServer/pacr42-2.plain.pdf", secretKey2);
			System.out.println("The secret key has been saved");
			System.out.println("El archivo ha sido desencriptado");
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public static void main(String[] args) throws Exception{

		EchoServer fts = null;
		if (args.length == 0) {
			fts = new EchoServer();
		} else {
			int port = Integer.parseInt(args[0]);
			fts = new EchoServer(port);
		}
		fts.init();	
		
		
	}
}