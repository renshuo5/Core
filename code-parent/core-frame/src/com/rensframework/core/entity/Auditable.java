package com.rensframework.core.entity;

public interface Auditable<T extends Entity> extends Entity {

	public abstract String getEntityName();

	public abstract String getIdStr();

	public abstract String getName();

	public abstract T getOrig();

	public abstract void setOrig(T paramT);
}
