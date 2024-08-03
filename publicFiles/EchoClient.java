package publicFiles;

import java.io.File;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import integrity.Hasher;
import publickeycipher.PublicKeyCipher;
import util.Files;
import util.Util;

public class EchoClient {
    public static final int PORT = 4000;
    public static final String SERVER = "localhost";

    private Socket clientSideSocket;
    
    private String server;
    private int port;
    
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public EchoClient() throws Exception {
        this.server = SERVER;
        this.port = PORT;
        generateKeyPair();
        System.out.println("Julian Roa Palacio - May 20/2024");
        System.out.println("File transfer client is running ... connecting the server in "+ this.server + ":" + this.port);
        System.out.println("Other usage: FileTransferClient host port. Ex: FileTransferClient localhost 3800");
    }

    public EchoClient(String server, int port) throws Exception {
        this.server = server;
        this.port = port;
        generateKeyPair();
        System.out.println("Julian Roa Palacio - May 20/2024");
        System.out.println("Echo client is running ... connecting the server in "+ this.server + ":" + this.port);
    }

    private void generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        this.publicKey = pair.getPublic();
        this.privateKey = pair.getPrivate();
        
        // Guardar las claves
        Util.saveObject(publicKey, "src/publicSend/publicKey.key");
        Util.saveObject(privateKey, "src/publicSend/privateKey.key");
    }

    public void init() throws Exception {
        clientSideSocket = new Socket(server, port);
        protocol(clientSideSocket);
        clientSideSocket.close();
    }

    public void protocol(Socket socket) throws Exception {
        PublicKeyCipher cipher = new PublicKeyCipher("RSA");

        // Encriptar archivo con la llave privada
        cipher.encryptFile("src/publicSend/pacr42-2.pdf", privateKey);
        cipher.encryptTextFile("src/publicSend/text.txt", privateKey);
        Hasher.generateIntegrityCheckerFile("src/publicSend", "integritycheck.txt");
        
        System.out.println("Current directory: " + new File(".").getAbsolutePath());
        Files.sendFolder("src/publicSend", socket);
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
