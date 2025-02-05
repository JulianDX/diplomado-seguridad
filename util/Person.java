package util;

import java.io.Serializable;

public class Person implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int age;
	private double height;
	
	public Person (String name, int age, double height)  {
		super();
		this.name = name;
		this.age = age;
		this.height = height;
	}

	@Override
	public String toString() {
		return "Name=" + name + ", age=" + age + ", height=" + height;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	
	
	
}
