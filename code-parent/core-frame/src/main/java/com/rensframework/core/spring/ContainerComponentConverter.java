package com.rensframework.core.spring;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.converter.GenericConverter;

import com.rensframework.core.entity.ContainerComponent;

public final class ContainerComponentConverter implements
		ConditionalGenericConverter {

	public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
		Set<GenericConverter.ConvertiblePair> set = new HashSet<GenericConverter.ConvertiblePair>();
		set.add(new GenericConverter.ConvertiblePair(String.class,
				ContainerComponent.class));
		set.add(new GenericConverter.ConvertiblePair(ContainerComponent.class,
				String.class));
		return Collections.unmodifiableSet(set);
	}

	public Object convert(Object source, TypeDescriptor sourceType,
			TypeDescriptor targetType) {
		Class<?> sourceClazz = sourceType.getType();
		if (ContainerComponent.class.isAssignableFrom(sourceClazz)) {
			return source == null ? null : ((ContainerComponent) source)
					.getStrValue();
		}
		Class<?> targetClazz = targetType.getType();

		ContainerComponent target = (ContainerComponent) BeanUtils
				.instantiate(targetClazz);
		target.setStrValue((String) source);
		return target;
	}

	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return true;
	}
}
