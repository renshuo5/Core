package com.rensframework.core.entity;

import org.apache.commons.lang.StringUtils;

public class ContainerComponent implements Component {
	private static final long serialVersionUID = 739000292718283388L;
	protected String separator;
	private String value;

	public ContainerComponent(String separator) {
		this.separator = separator;
	}

	public String getStrValue() {
		return this.value;
	}

	public void setStrValue(String value) {
		if ("".equals(value)) {
			this.value = null;
		} else {
			this.value = value;
		}
	}

	protected String[] getStrValueArray() {
		if (StringUtils.isNotBlank(this.value)) {
			return this.value.trim().split(this.separator);
		}
		return new String[0];
	}

	protected String asString(Object obj) {
		if ((obj instanceof Enum)) {
			return ((Enum) obj).name();
		}
		return obj.toString();
	}

	protected boolean needOrdered() {
		return false;
	}

	public int hashCode() {
		return null == getStrValue() ? super.hashCode() : getStrValue()
				.hashCode();
	}

	public boolean equals(Object obj) {
		boolean equal = (obj != null) && (obj.getClass().equals(getClass()));
		if (equal) {
			String thisValue = getStrValue();
			String objValue = ((ContainerComponent) obj).getStrValue();

			equal = obj == this;
		}
		return equal;
	}

	public String toString() {
		return null == getStrValue() ? "" : getStrValue();
	}
}
