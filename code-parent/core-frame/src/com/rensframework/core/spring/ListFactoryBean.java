package com.rensframework.core.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;

public class ListFactoryBean<T> implements FactoryBean<List<T>> {

	private List<T> baseList;
	private List<T> extendedList;

	public List<T> getObject() throws Exception {
		List<T> consolidatedList = new ArrayList<T>();
		consolidatedList.addAll(this.baseList);
		consolidatedList.addAll(this.extendedList);
		return consolidatedList;
	}

	public Class<?> getObjectType() {
		return List.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setBaseList(List<T> baseList) {
		this.baseList = baseList;
	}

	public void setExtendedList(List<T> extendedList) {
		this.extendedList = extendedList;
	}

}
