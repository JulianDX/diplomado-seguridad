package integrity;

public class HashTester3 {
	
	public static void main(String[] args) {
		try {
			Hasher.generateIntegrityCheckerFile("src/sendClientServer", "integritycheck.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
