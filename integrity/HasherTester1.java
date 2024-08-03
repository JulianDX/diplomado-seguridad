package integrity;

public class HasherTester1 {
	
	public static void main(String[] args) {
		String message = "Fundamentos de seguridad digital";
		try {
			System.out.println(Hasher.getHash(message, "sha-1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
