package com.rensframework.core.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.rensframework.core.entity.listener.TimeEntityListener;

@Data
@ToString(callSuper = true, exclude = { "creation", "lastModified" })
@EqualsAndHashCode(callSuper = true, exclude = { "creation", "lastModified" })
@MappedSuperclass
@EntityListeners({ TimeEntityListener.class })
public abstract class TimeEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6799509605518453261L;

	@Column(updatable = false, nullable = false)
	protected Timestamp creation;

	@Column(nullable = false)
	protected Timestamp lastModified;

}
