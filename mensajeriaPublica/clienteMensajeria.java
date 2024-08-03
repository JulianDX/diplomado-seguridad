package mensajeriaPublica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import publickeycipher.PublicKeyCipher;
import util.Base64;

public class clienteMensajeria {
    public static final String SERVER = "localhost";
    public static final int PORT = 3400;
    private static final String PRIVATE_KEY_FILE = "privateKey.txt";
    private static final String PUBLIC_KEY_FILE = "publicKey.txt";

    private static final Scanner SCANNER = new Scanner(System.in);

    private PrintWriter toNetwork;
    private BufferedReader fromNetwork;

    private Socket clientSideSocket;

    private String server;
    private int port;

    private KeyPair keyPair;

    public clienteMensajeria() {
        this.server = SERVER;
        this.port = PORT;
        this.keyPair = loadOrGenerateKeyPair(); // Cargar o generar claves
        System.out.println("Julian Roa - Jul 20/2024");
        System.out.println("Echo client is running ... connecting the server in " + this.server + ":" + this.port);
    }

    public clienteMensajeria(String server, int port) {
        this.server = server;
        this.port = port;
        this.keyPair = loadOrGenerateKeyPair(); // Cargar o generar claves
        System.out.println("Julian Roa - Jul 20/2024");
        System.out.println("Echo client is running ... connecting the server in " + this.server + ":" + this.port);
    }

    private void createStreams(Socket socket) throws IOException {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private String decryptMessage(String mensajeCifrado, PrivateKey privateKey) throws Exception {
        PublicKeyCipher publicKeyCipher = new PublicKeyCipher("RSA");
        String mensajeDesencriptado = publicKeyCipher.decryptMessage(Base64.decode(mensajeCifrado), privateKey);
        return mensajeDesencriptado;
    }

    private KeyPair loadOrGenerateKeyPair() {
        try {
            File privateKeyFile = new File(PRIVATE_KEY_FILE);
            File publicKeyFile = new File(PUBLIC_KEY_FILE);

            if (privateKeyFile.exists() && publicKeyFile.exists()) {
                // Cargar claves desde archivos
                PrivateKey privateKey = loadPrivateKey();
                PublicKey publicKey = loadPublicKey();
                return new KeyPair(publicKey, privateKey);
            } else {
                // Generar nuevas claves
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(1024);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
                saveKeys(keyPair); // Guardar las claves generadas
                return keyPair;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveKeys(KeyPair keyPair) throws IOException {
        try (FileWriter privateKeyWriter = new FileWriter(PRIVATE_KEY_FILE);
             FileWriter publicKeyWriter = new FileWriter(PUBLIC_KEY_FILE)) {
            privateKeyWriter.write(Base64.encode(keyPair.getPrivate().getEncoded()));
            publicKeyWriter.write(Base64.encode(keyPair.getPublic().getEncoded()));
        }
    }

    private PrivateKey loadPrivateKey() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(PRIVATE_KEY_FILE))) {
            String privateKeyBase64 = br.readLine();
            byte[] keyBytes = Base64.decode(privateKeyBase64);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            return keyFactory.generatePrivate(spec);
        }
    }

    private PublicKey loadPublicKey() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(PUBLIC_KEY_FILE))) {
            String publicKeyBase64 = br.readLine();
            byte[] keyBytes = Base64.decode(publicKeyBase64);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return keyFactory.generatePublic(spec);
        }
    }

    private List<String> dividirMensaje(String mensaje, int tamañoMaximo) {
        List<String> partes = new ArrayList<>();
        for (int i = 0; i < mensaje.length(); i += tamañoMaximo) {
            partes.add(mensaje.substring(i, Math.min(i + tamañoMaximo, mensaje.length())));
        }
        return partes;
    }

    public void protocol(Socket socket) throws Exception {
        createStreams(socket);

        System.out.println();
        System.out.println("Bienvenid@ al sistema de mensajería, los comandos son los siguientes: ");
        System.out.println("REGISTRAR (usuario)");
        System.out.println("OBTENER_LLAVE_PUBLICA (usuario)");
        System.out.println("ENVIAR (usuario) (mensaje)");
        System.out.println("LEER (usuario)");
        System.out.println();
        System.out.println("Escriba una opción:");
        String opcion = SCANNER.nextLine();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        if (opcion.startsWith("REGISTRAR")) {
            String publicKeyBase64 = Base64.encode(publicKey.getEncoded());
            toNetwork.println(opcion + " " + publicKeyBase64);

            String fromServer = fromNetwork.readLine();
            System.out.println("[Client] From server: " + fromServer);

            System.out.println("[Client] Finished.");
        }

        if (opcion.startsWith("OBTENER_LLAVE_PUBLICA")) {
            toNetwork.println(opcion);

            String fromServer = fromNetwork.readLine();
            System.out.println("[Client] From server: " + fromServer);

            System.out.println("[Client] Finished.");
        }

        if (opcion.startsWith("ENVIAR")) {
            System.out.println("Ingrese el mensaje a enviar:");
            String mensaje = SCANNER.nextLine(); // Mensaje a enviar

            // Dividir el mensaje en partes adecuadas
            List<String> partesMensaje = dividirMensaje(mensaje, 100); // Cambia 100 por el tamaño máximo adecuado

            for (String parte : partesMensaje) {
                toNetwork.println(opcion + " " + parte); // Envía cada parte del mensaje
            }

            String fromServer = fromNetwork.readLine();
            System.out.println("[Client] From server: " + fromServer);

            System.out.println("[Client] Finished.");
        }

        if (opcion.startsWith("LEER")) {
            toNetwork.println(opcion);
            String fromServer = fromNetwork.readLine(); // Lee el primer mensaje
            System.out.println("[Client] From server: " + fromServer);

            // Procesar mensajes
            // Si hay mensajes, sigue leyendo del servidor
            while (true) {
                fromServer = fromNetwork.readLine(); // Lee la siguiente línea
                if (fromServer == null || !fromServer.startsWith("Mensaje: ")) {
                    break; // Salir del bucle si no hay más mensajes
                }
                String mensajeCifrado = fromServer.substring("Mensaje: ".length());
                System.out.println(mensajeCifrado);
                try {
                    String mensajeDesencriptado = decryptMessage(mensajeCifrado, privateKey);
                    System.out.println("[Client] Mensaje recibido: " + mensajeDesencriptado);
                } catch (Exception e) {
                    System.out.println("[Client] Error al desencriptar el mensaje: " + e.getMessage());
                }
            }
        }
    }

    public void init() throws Exception {
        clientSideSocket = new Socket(this.server, this.port);

        protocol(clientSideSocket);

        clientSideSocket.close();
    }

    public static void main(String args[]) throws Exception {
        clienteMensajeria ec = null;
        if (args.length == 0) {
            ec = new clienteMensajeria();
        } else {
            String server = args[0];
            int port = Integer.parseInt(args[1]);
            ec = new clienteMensajeria(server, port);
        }
        ec.init();
    }
}
