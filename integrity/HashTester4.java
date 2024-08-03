package integrity;

public class HashTester4 {

	public static void main(String[] args) {
		try {
			Hasher.checkIntegrityFile("src/sendClientServer/integritycheck.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
