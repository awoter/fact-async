package com.woter.fact.async;

public class User {
	
	private String name;
	private int age;
	
	public User(){}
	
	public User (int age,String name){
		this.age = age;
		this.name = name;
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
}
