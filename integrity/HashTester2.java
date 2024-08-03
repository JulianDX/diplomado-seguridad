package integrity;

public class HashTester2 {
	
	public static void main(String[] args) {
		String filename = "src/files/pacr42-2.pdf";
		try {
			String hash = Hasher.getHashFile(filename, "SHA-256");
			System.out.println(hash);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
