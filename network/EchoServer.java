package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import util.Base64;
import util.Person;
import util.Util;

public class EchoServer {
	public static final int PORT = 3400;

	private ServerSocket listener;
	private Socket serverSideSocket;
	
	private PrintWriter toNetwork;
	private BufferedReader fromNetwork;
	
	private int port;
	
	public EchoServer() {
		this.port = PORT;
		System.out.println("Julian Roa - Jul 20/2024");
		System.out.println("Echo server is running on port: " + this.port);
		//System.out.println("Run: java -jar EchoClient.jar hostname " + this.port);
	}
	
	public EchoServer(int port) {
		this.port = port;
		System.out.println("Julian Roa - Jul 20/2024");
		System.out.println("Echo server is running on port: " + this.port);
		//System.out.println("Run: java -jar EchoClient.jar hostname " + this.port);
	}
	
	private void createStreams(Socket socket) throws IOException {
		toNetwork = new PrintWriter(socket.getOutputStream(), true);
		fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	private void protocol(Socket socket) throws IOException, ClassNotFoundException {
		
		createStreams(socket);

		String nombre = fromNetwork.readLine();
		System.out.println("[Server] From client: Archivos recibidos");
		
		String saldo = fromNetwork.readLine();
		System.out.println("[Server] From client: " + saldo);
		
		String cadena = fromNetwork.readLine();
		System.out.println("[Server] From client: " + cadena);


		toNetwork.println("");
		System.out.println("[Server] Waiting for a new client.");
	}
	
	private void init() throws IOException, ClassNotFoundException {
		listener = new ServerSocket(this.port);

		while (true) {
			serverSideSocket = listener.accept();
			
			String ip = serverSideSocket.getInetAddress().getHostAddress();
			int port = serverSideSocket.getPort();
			System.out.println("Client IP addres: " + ip);
			System.out.println("Client number port: " + port);

			protocol(serverSideSocket);
		}
	}
	
	public static void main(String args[]) throws Exception {
		EchoServer es = null;
		if (args.length == 0) {
			es = new EchoServer();
		} else {
			int port = Integer.parseInt(args[0]);
			es = new EchoServer(port);
		}
		es.init();	
	}
}