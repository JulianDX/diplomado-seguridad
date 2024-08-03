package crypnetwork;

import java.io.File;
import java.net.Socket;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import integrity.Hasher;
import symmetriccipher.SymmetricCipher;
import util.Files;
import util.Util;

public class EchoClient {
	public static final int PORT = 4000;
	public static final String SERVER = "localhost";

	private Socket clientSideSocket;
	
	private String server;
	private int port;
	
	public EchoClient() {
		this.server = SERVER;
		this.port = PORT;
		System.out.println("Julian Roa Palacio - May 20/2024");
		System.out.println("File transfer client is running ... connecting the server in "+ this.server + ":" + this.port);
		System.out.println("Other usage: FileTransferClient host port. Ex: FileTransferClient localhost 3800");
	}
	
	public EchoClient(String server, int port) {
		this.server = server;
		this.port = port;
		System.out.println("Julian Roa Palacio - May 20/2024");
		System.out.println("Echo client is running ... connecting the server in "+ this.server + ":" + this.port);
	}

	public void init() throws Exception {
		clientSideSocket = new Socket(SERVER, PORT);

		protocol(clientSideSocket);
		clientSideSocket.close();
	}

	public void protocol(Socket socket) throws Exception {
		SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey();
		SymmetricCipher cipher = new SymmetricCipher(secretKey, "DES/ECB/PKCS5Padding");
		Util.saveObject(secretKey, "src/sendClientServer/secretKey.key");
		cipher.encryptTextFile("src/files/text.txt", "src/sendClientServer/text.txt.encrypted");
		cipher.encryptFile("src/files/pacr42-2.pdf", "src/sendClientServer/pacr42-2.pdf.encrypted");
		Hasher.generateIntegrityCheckerFile("src/sendClientServer", "integritycheck.txt");
		System.out.println("Current directory: " + new File(".").getAbsolutePath());
		Files.sendFolder("src/sendClientServer", socket);
	}
	
	public static void main(String[] args) throws Exception {
		EchoClient ftc = null;
		if (args.length == 0) {
			ftc = new EchoClient();
						
		} else {
			String server = args[0];
			int port = Integer.parseInt(args[1]);
			ftc = new EchoClient(server, port);
		}
		ftc.init();
	}
}
