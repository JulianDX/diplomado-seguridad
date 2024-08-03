package mensajeriaPublica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;

import publickeycipher.PublicKeyCipher;
import util.Base64;

public class servidorMensajeria {
    public static final int PORT = 3400;

    public static HashMap<String, String> clientes = new HashMap<>();
    public static HashMap<String, ArrayList<String>> buzones = new HashMap<>();

    private ServerSocket listener;
    private Socket serverSideSocket;

    private PrintWriter toNetwork;
    private BufferedReader fromNetwork;

    private int port;

    public servidorMensajeria() {
        this.port = PORT;
        System.out.println("Julian Roa - Jul 20/2024");
        System.out.println("Echo server is running on port: " + this.port);
    }

    public servidorMensajeria(int port) {
        this.port = port;
        System.out.println("Julian Roa - Jul 20/2024");
        System.out.println("Echo server is running on port: " + this.port);
    }

    public static Key decodePublicKey(String publicKeyBase64) throws Exception {
        byte[] keyBytes = Base64.decode(publicKeyBase64);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(spec);
    }

    private String cifrarMensaje(String mensaje, PublicKey publicKey) throws Exception {
        PublicKeyCipher publicKeyCipher = new PublicKeyCipher("RSA");
        byte[] mensajeCifrado = publicKeyCipher.encryptMessage(mensaje, publicKey);
        return Base64.encode(mensajeCifrado);
    }

    private void createStreams(Socket socket) throws IOException {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void protocol(Socket socket) throws Exception {
        createStreams(socket);

        String opcion = fromNetwork.readLine();
        System.out.println("[Server] From client: " + opcion);

        if (opcion.startsWith("REGISTRAR")) {
            String[] words = opcion.trim().split("\\s+");

            if (clientes.containsKey(words[1])) {
                toNetwork.println("El usuario " + words[1] + " ya está registrado");
            } else {
                clientes.put(words[1], words[2]);
                buzones.put(words[1], new ArrayList<>());
                toNetwork.println("Bienvenid@ " + words[1]);
            }
        }

        if (opcion.startsWith("OBTENER_LLAVE_PUBLICA")) {
            String[] words = opcion.trim().split("\\s+");

            if (clientes.containsKey(words[1])) {
                toNetwork.println("Llave pública de: " + words[1] + " " + clientes.get(words[1]));
            } else {
                toNetwork.println("El cliente " + words[1] + " no se encuentra registrado");
            }
        }

        if (opcion.startsWith("ENVIAR")) {
            String[] words = opcion.trim().split("\\s+");
            if (clientes.containsKey(words[1])) {
                PublicKey publicKeyDecoded = (PublicKey) decodePublicKey(clientes.get(words[1]));
                String mensajeCifrado = cifrarMensaje(words[2], publicKeyDecoded);
                ArrayList<String> buzónUsuario = buzones.get(words[1]);
                buzónUsuario.add(mensajeCifrado);
                toNetwork.println("Mensaje enviado a: " + words[1]);
                System.out.println(buzones);
            } else {
                toNetwork.println("El cliente " + words[1] + " no se encuentra registrado");
            }
        }

        if (opcion.startsWith("LEER")) {
            String[] words = opcion.trim().split("\\s+");
            if (!clientes.containsKey(words[1])) {
                toNetwork.println("ERROR. El usuario " + words[1] + " no está registrado.");
            } else {
                ArrayList<String> mensajes = buzones.get(words[1]);
                int numMensajes = mensajes.size();
                if (numMensajes == 0) {
                    toNetwork.println("El usuario " + words[1] + " tiene 0 mensajes.");
                } else {
                	if(numMensajes == 1) {
                		toNetwork.println("El usuario " + words[1] + " tiene 1 mensaje.");
                	}else {
                        toNetwork.println("El usuario " + words[1] + " tiene " + numMensajes + " mensajes.");
                	}

                    for (String mensajeCifrado : mensajes) {
                        toNetwork.println("Mensaje: " + mensajeCifrado);
                        System.out.println("[Server] Enviando mensaje: " + mensajeCifrado); // Línea de depuración
                    }
                }
            }
        }

        System.out.println("[Server] Waiting for a new client.");
    }

    private void init() throws Exception {
        listener = new ServerSocket(this.port);

        while (true) {
            serverSideSocket = listener.accept();

            String ip = serverSideSocket.getInetAddress().getHostAddress();
            int port = serverSideSocket.getPort();
            System.out.println("Client IP address: " + ip);
            System.out.println("Client number port: " + port);

            protocol(serverSideSocket);
        }
    }

    public static void main(String args[]) throws Exception {
        servidorMensajeria es = null;
        if (args.length == 0) {
            es = new servidorMensajeria();
        } else {
            int port = Integer.parseInt(args[0]);
            es = new servidorMensajeria(port);
        }
        es.init();
    }
}
