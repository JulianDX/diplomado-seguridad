package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import integrity.Hasher;

public class Files {
	public static void sendFile(String filename, Socket socket) throws Exception {
		System.out.println("File to send: " + filename);
		File localFile = new File(filename);
		BufferedInputStream fromFile = new BufferedInputStream(new FileInputStream(localFile));

		// send the size of the file (in bytes)
		long size = localFile.length();
		System.out.println("Size: " + size);

		PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
		printWriter.println(filename);

		printWriter.println("Size:" + size);

		BufferedOutputStream toNetwork = new BufferedOutputStream(socket.getOutputStream());

		pause(50);
		
		// the file is sent one block at a time
		byte[] blockToSend = new byte[1024];
		int in;
		while ((in = fromFile.read(blockToSend)) != -1) {
			toNetwork.write(blockToSend, 0, in);
		}
		// the stream linked to the socket is flushed and closed
		toNetwork.flush();
		fromFile.close();

		pause(50);
	}

	public static String receiveFile(String folder, Socket socket) throws Exception {
		File fd = new File (folder);
		if (fd.exists()==false) {
			fd.mkdir();
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		BufferedInputStream fromNetwork = new BufferedInputStream(socket.getInputStream());

		String filename = reader.readLine();
		filename = folder + File.separator + filename;

		BufferedOutputStream toFile = new BufferedOutputStream(new FileOutputStream(filename));

		System.out.println("File to receive: " + filename);

		String sizeString = reader.readLine();

		// the sender sends "Size:" + size, so here it is separated
		// long size = Long.parseLong(sizeString.subtring(5));
		long size = Long.parseLong(sizeString.split(":")[1]); 
		System.out.println("Size: " + size);
		
		// the file is received one block at a time
		byte[] blockToReceive = new byte[512];
		int in;
		long remainder = size; // lo que falta
		while ((in = fromNetwork.read(blockToReceive)) != -1) {
			toFile.write(blockToReceive, 0, in);
			remainder -= in;
			if (remainder == 0)
				break;
		}

		pause(50);
		
		// the stream linked to the file is flushed and closed
		toFile.flush();
		toFile.close();
		System.out.println("File received: " + filename);
		return filename;
	}
	
	public static void sendFolder(String folderName, Socket socket) throws Exception {
        File folder = new File(folderName);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("The provided path is not a directory.");
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("The directory is empty or cannot be read.");
        }
        
        Hasher.generateIntegrityCheckerFile("src/sendClientServer/", "integritycheck.txt");

        files = folder.listFiles();
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

        // Send the number of files in the directory
        printWriter.println(files.length);

        for (File file : files) {
            if (file.isFile()) {
                sendFile(file, socket);
            }
        }

        // Notify the end of the folder transfer
        printWriter.println("END_OF_FOLDER");

        printWriter.flush();
    }

    private static void sendFile(File file, Socket socket) throws IOException {
        System.out.println("File to send: " + file.getName());
        BufferedInputStream fromFile = new BufferedInputStream(new FileInputStream(file));

        // Send the file name and size
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        long size = file.length();
        printWriter.println(file.getName());
        printWriter.println("Size:" + size);

        BufferedOutputStream toNetwork = new BufferedOutputStream(socket.getOutputStream());

        try {
			pause(50);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Send the file in blocks
        byte[] blockToSend = new byte[1024];
        int in;
        while ((in = fromFile.read(blockToSend)) != -1) {
            toNetwork.write(blockToSend, 0, in);
        }
        toNetwork.flush();
        fromFile.close();

        try {
			pause(50);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
    public static void receiveFolder(String folder, Socket socket) throws Exception {
        File directory = new File(folder);
        if (!directory.exists()) {
            directory.mkdir();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedInputStream fromNetwork = new BufferedInputStream(socket.getInputStream());

        // Read the number of files to be received
        int numFiles = Integer.parseInt(reader.readLine());
        System.out.println("Number of files to receive: " + numFiles);

        for (int i = 0; i < numFiles; i++) {
            // Receive each file
            String filename = receiveFile(folder, socket);
            System.out.println("Received file: " + filename);
        }

		String resultado = Hasher.checkIntegrityFile("src/receiveClientServer/integritycheck.txt");
		System.out.println(resultado);
        
        // Check for the end of folder signal
        String endSignal = reader.readLine();
        if ("END_OF_FOLDER".equals(endSignal)) {
            System.out.println("End of folder received.");
        } else {
            System.err.println("Unexpected end signal received: " + endSignal);
        }

        reader.close();
        fromNetwork.close();
    }



	public static void pause(int miliseconds) throws Exception {
		Thread.sleep(miliseconds);
	}
}