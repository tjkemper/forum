package com.ex.model;

public class RegisterUser {
	
	private String username;
	private String password;
	
	public RegisterUser(){}

	public RegisterUser(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "RegisterUser [username=" + username + ", password=" + password + "]";
	}

}
