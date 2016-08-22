package com.rensframework.core.model;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseModel implements Serializable {
	private static final long serialVersionUID = 6479341524949060190L;
	protected final transient Logger logger = LoggerFactory
			.getLogger(getClass());
}
