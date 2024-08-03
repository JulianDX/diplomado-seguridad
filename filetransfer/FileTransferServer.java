package filetransfer;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

import util.Files;

public class FileTransferServer {
	public static final int PORT = 4000;

	private ServerSocket listener;
	private Socket serverSideSocket;
	
	private int port;
	
	public FileTransferServer() {
		System.out.println("Carlos E. Gomez - May 20/2024");
		System.out.println("File transfer server is running on port: " + this.port);
	}

	public FileTransferServer(int port) {
		this.port = port;
		System.out.println("Carlos E. Gomez - May 20/2024");
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
		 Files.receiveFolder("src/Recibidos", socket);
	}
	
	public static void main(String[] args) throws Exception{

		FileTransferServer fts = null;
		if (args.length == 0) {
			fts = new FileTransferServer();
		} else {
			int port = Integer.parseInt(args[0]);
			fts = new FileTransferServer(port);
		}
		fts.init();	
		
		
	}
}