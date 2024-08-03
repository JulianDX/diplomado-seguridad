package integrity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;

import util.Util;

public class Hasher {

	public static String getHash (String input, String algorithm) throws Exception {
		byte[] inputBA = input.getBytes();
		
		MessageDigest hasher = MessageDigest.getInstance(algorithm);
		hasher.update(inputBA);
		
		return Util.byteArrayToHexString(hasher.digest(), "");
	}
	
	public static String getHashFile (String filename, String algorithm) throws Exception {
		MessageDigest hasher = MessageDigest.getInstance(algorithm);
		
		FileInputStream fis = new FileInputStream(filename);
		byte[] buffer = new byte[1024];
		
		int in;
		while ((in = fis.read(buffer)) != -1) {
			hasher.update(buffer, 0 ,in);
		}
		
		fis.close();
		
		return Util.byteArrayToHexString(hasher.digest(), "");
	}
	
	public static void generateIntegrityCheckerFile(String folder, String generatedFile) throws Exception {
        String rutaArchivo = folder + File.separator + generatedFile;
        File directorio = new File(folder);

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo))) {
            if (directorio.isDirectory()) {
                String[] listaArchivos = directorio.list();

                if (listaArchivos != null) {
                    boolean primeraLinea = true;
                    for (String nombreArchivo : listaArchivos) {
                        if (!nombreArchivo.equals(generatedFile)) {
                            String hash = Hasher.getHashFile(folder + File.separator + nombreArchivo, "SHA-256");
                            if (!primeraLinea) {
                                escritor.newLine(); // Agregar una nueva línea antes de escribir, excepto para la primera línea
                            }
                            escritor.write(hash + " *" + nombreArchivo);
                            primeraLinea = false;
                        }
                    }
                    System.out.println("Se ha generado el archivo integritycheck.txt");
                } else {
                    System.out.println("El directorio está vacío o no se pudo leer.");
                }
            } else {
                System.out.println(directorio + " no es un directorio válido.");
            }
        } catch (IOException e) {
            System.err.println("Ocurrió un error al escribir en el archivo: " + e.getMessage());
        }
    }
	
	 public static String checkIntegrityFile(String file) throws Exception {
		 
		 int counter = 0;
		 int lineformat = 0;
		 String warnings = "";
		 int dif = 0;
	        try (BufferedReader lector = new BufferedReader(new FileReader(file))) {
	            String linea;
	            while ((linea = lector.readLine()) != null) {
	                // Buscar el índice del símbolo '*'
	                int indiceSeparador = linea.indexOf('*');
	                if (indiceSeparador != -1) {
	                	String[] test = linea.split("\\*");
                    	if(test[0].length() < 65) {
                    		lineformat += 1;
                    		warnings+="shasum: WARNING: "+lineformat+" "+"line is improperly formatted\n";
                    		lineformat+=1;
                    	}
	                    // Obtener la subcadena después del '*'
	                    String contenidoDespuesDeSeparador = linea.substring(indiceSeparador + 1);
	                    File archivo = new File("src/sendClientServer/", contenidoDespuesDeSeparador);
	                    if(archivo.exists()) {
		                    String hash = getHashFile("src/sendClientServer" + File.separator + contenidoDespuesDeSeparador, "SHA-256")+" ";
		                    if(!hash.equals(test[0])) {
		                    	if(indiceSeparador == -1) {
		                    		System.out.println(contenidoDespuesDeSeparador+":"+" FAILED");
		                    	}else {
		                    		System.out.println(contenidoDespuesDeSeparador+":"+" FAILED");
		                    	}
		                    
		                    	dif += contarCaracteresDiferentes(hash, test[0]);
		                    }else {
		                    	System.out.println(contenidoDespuesDeSeparador+":"+" OK");
		                    }	
	                    }else {
		                    System.out.println("shasum: "+contenidoDespuesDeSeparador+":"+" No such file or directory");
		                    System.out.println(contenidoDespuesDeSeparador+":"+" FAILED open or read");
	                    	counter+=1;
	                    }
	                } 
	            }
	            if(counter == 0) {
                	System.out.println(warnings);
                }else {
                	warnings+="shasum: WARNING: ";
                	if(counter == 1) {
                		warnings+=counter+" "+"listed file could not be read\n";
                	}else {
                		warnings+=counter+" "+"listed files could not be read\n";
                	}
                }
	            if(dif == 0) {
                	
                }else {
                	warnings+="shasum: WARNING: ";
                	if(dif == 1) {
                		warnings+=dif+" "+"computed checksum did NOT match";
                	}else {
                		warnings+=dif+" "+"computed checksums did NOT match";
                	}
                	
                }
	            
	        } catch (IOException e) {
	            System.err.println("Ocurrió un error al leer el archivo: " + e.getMessage());
	        }
	        System.out.println(warnings);
			return warnings;
	    }
	 
	 public static int contarCaracteresDiferentes(String str1, String str2) {
	        // Inicializar el contador de diferencias
	        int diferencias = 0;
	        
	        // Obtener la longitud mínima de las dos cadenas
	        int longitudMinima = Math.min(str1.length(), str2.length());
	        
	        // Comparar las cadenas carácter por carácter hasta la longitud mínima
	        for (int i = 0; i < longitudMinima; i++) {
	            if (str1.charAt(i) != str2.charAt(i)) {
	                diferencias++;
	            }
	        }
	        
	        // Contar las diferencias debido a la longitud de las cadenas
	        diferencias += Math.abs(str1.length() - str2.length());
	        
	        return diferencias;
	    }
	
}
