package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.SecureRandom;

public class Util {
	
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();
	
	public static String byteArrayToHexString(byte [] bytes, String separator) {
		String result = "";
		
		for (int i = 0; i < bytes.length; i++) {
			result += String.format("%02x", bytes[i]) + separator;
		}
		
		return result.toString();
	}
	
	public static String getKey(Key key, String type) {
		String finalKey = "";
        String publicKeyBase64 = Base64.encode(key.getEncoded());
        finalKey+="-----BEGIN "+ type + " KEY-----\n";
		finalKey+=publicKeyBase64+"\n";
		finalKey+="-----END "+ type + " KEY-----";
		return finalKey;
	}

	public static void saveObject(Object o, String fileName) throws
	IOException{
		FileOutputStream fileOut;
		ObjectOutputStream out;
		
		fileOut = new FileOutputStream(fileName);
		out = new ObjectOutputStream(fileOut);
		
		out.writeObject(o);
		
		out.flush();
		out.close();
		
	}
	
	public static Object loadObject (String fileName) throws
	IOException,
	ClassNotFoundException,
	InterruptedException{
		
		FileInputStream fileIn;
		ObjectInputStream in;
		
		fileIn = new FileInputStream(fileName);
		in = new ObjectInputStream(fileIn);
		
		Thread.sleep(100);
		
		Object o = in.readObject();
		
		fileIn.close();
		in.close();
		
		return o;
		
	}
	

	
	public static byte[] objectToByteArray(Object o) throws IOException {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bos);
		out.writeObject(o);
		out.close();
		byte[] buffer = bos.toByteArray();
		
		return buffer;
	}
	
	public static Object byteArrayToObject(byte[] byteArray) throws IOException, ClassNotFoundException {
		
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteArray));
		Object o = in.readObject();
		in.close();
		
		return o;
		
	}
	
	public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(index));
        }
        return sb.toString();
    }
	
	public static byte[][] split(byte[] input, int chunkSize) {
	    int numChunks = (int) Math.ceil((double) input.length / chunkSize); // Calcular la cantidad de fragmentos
	    byte[][] chunks = new byte[numChunks][]; // Crear el arreglo para los fragmentos

	    for (int i = 0; i < numChunks; i++) {
	        // Calcular el tamaño del fragmento actual
	        int size = (i == numChunks - 1) ? input.length % chunkSize : chunkSize;
	        if (size == 0) {
	            size = chunkSize; // Si el tamaño es cero, asignar el tamaño del fragmento
	        }

	        // Crear el fragmento y copiar los datos correspondientes
	        chunks[i] = new byte[size];
	        System.arraycopy(input, i * chunkSize, chunks[i], 0, size);
	    }

	    return chunks; // Retornar el arreglo de fragmentos
	}
    
	public static byte[] join(byte[][] input) {
	    // Calcular el tamaño total del arreglo resultante
	    int totalLength = 0;
	    for (byte[] chunk : input) {
	        totalLength += chunk.length;
	    }

	    // Crear el arreglo de destino
	    byte[] output = new byte[totalLength];

	    // Copiar los elementos de cada fila en el arreglo de destino
	    int currentIndex = 0;
	    for (byte[] chunk : input) {
	        System.arraycopy(chunk, 0, output, currentIndex, chunk.length);
	        currentIndex += chunk.length;
	    }

	    return output;
	}
	
}
