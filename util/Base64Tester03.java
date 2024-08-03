package util;

import java.io.IOException;
import java.util.ArrayList;

public class Base64Tester03 {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		ArrayList<Person> personList = new ArrayList<Person>();
		
		Person person = new Person("Julián", 25, 1.78);
		Person person2 = new Person("Juan", 23, 1.75);
		Person person3 = new Person("Duvan", 27, 1.80);
		Person person4 = new Person("Joshua", 26, 1.87);
		
		personList.add(person);
		personList.add(person2);
		personList.add(person3);
		personList.add(person4);
		
		System.out.println(personList);
		
		byte[] nameBA = Util.objectToByteArray(personList);
		String nameB64 = Base64.encode(nameBA);
		System.out.println(nameB64);
		
		byte[] nameBA2 = Base64.decode(nameB64);
		@SuppressWarnings("unchecked")
		ArrayList<Person> names2 = (ArrayList<Person>) Util.byteArrayToObject(nameBA2);
		System.out.println(names2);
		
	}

}
