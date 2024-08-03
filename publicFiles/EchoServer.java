package publicFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

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

    public void protocol(Socket socket) throws Exception {
        System.out.println("Current directory: " + new File(".").getAbsolutePath());
        try {
            Files.receiveFolder("src/publicReceive", socket);

            // Cargar la llave privada y pública
            PrivateKey privateKey = (PrivateKey) Util.loadObject("src/publicReceive/privateKey.key");
            PublicKey publicKey = (PublicKey) Util.loadObject("src/publicReceive/publicKey.key");

            // Desencriptar el archivo utilizando la llave pública
            PublicKeyCipher cipher = new PublicKeyCipher("RSA");
            cipher.decryptFile("src/publicReceive/pacr42-2.pdf.rsa", publicKey);
            cipher.decryptTextFile("src/publicReceive/text.txt.rsa", publicKey);

            System.out.println("Archivos desencriptados");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) throws Exception {
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