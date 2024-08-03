package util;

import java.io.IOException;

public class Base64Tester02 {
	
	public static void main (String[] args)  throws IOException, ClassNotFoundException {
		
		Person person = new Person("Julián", 25, 1.78);
		
		System.out.println(person.toString());
		
		byte[] personBA = Util.objectToByteArray(person);
		String personB64 = Base64.encode(personBA);
		System.out.println(personB64);
		
		byte[] personBA2 = Base64.decode(personB64);
		Person person2 = (Person) Util.byteArrayToObject(personBA2);
		System.out.println(person2);
		
	}

}
