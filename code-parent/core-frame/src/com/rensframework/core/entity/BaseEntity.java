package com.rensframework.core.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import lombok.Data;
import lombok.ToString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@MappedSuperclass
public abstract class BaseEntity implements Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4503898297553089509L;

	@Transient
	protected final transient Logger logger = LoggerFactory
			.getLogger(getClass());
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	@Version
	protected long version = -1L;

}
