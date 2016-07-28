package com.rs.user.frame;

import org.springframework.beans.factory.FactoryBean;

/**
 * Hello world!
 *
 */
public class ConsoleCla implements FactoryBean<String> {

	private String consoleString;

	public void setConsoleString(String consoleString) {
		System.out.println(consoleString);
		this.consoleString = consoleString;
	}

	@Override
	public String getObject() throws Exception {
		return this.consoleString;
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}
}
