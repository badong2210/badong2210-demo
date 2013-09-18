package org.badong2210.core.dozer;

import org.apache.commons.lang.text.StrBuilder;
import org.dozer.CustomConverter;
import org.dozer.MappingException;

import java.lang.reflect.Method;

/**
 * Enum to string dozer converter
 */
public class EnumStringBiDirectionalConverter implements CustomConverter {
    @Override
    public Object convert(Object existingDestinationFieldValue, Object source, Class<?> destinationClass, Class<?> sourceClass) {
        if(source == null) {
            return null;
        }

        if(destinationClass != null){
            if(destinationClass.getSimpleName().equalsIgnoreCase("String")) {
                return this.getString(source);
            } else if( destinationClass.isEnum()) {
                return this.getEnum(destinationClass, source);
            } else {
                throw new MappingException(new StrBuilder("Converter ").append(this.getClass().getSimpleName())
                        .append(" was used incorrectly. Arguments were: ")
                        .append(destinationClass.getClass().getName())
                        .append(" and ")
                        .append(source).toString());
            }
        }

        return null;
    }

    private Object getString(Object object){
        return object.toString();
    }

    private Object getEnum(Class<?> destinationClass, Object source){
        Object enumeration = null;

        Method[] ms = destinationClass.getMethods();
        for(Method m : ms){
            if(m.getName().equalsIgnoreCase("valueOf")){
                try {
                    enumeration = m.invoke( destinationClass.getClass(), (String)source);
                } catch (Exception e) {
                    throw new IllegalArgumentException(e.getMessage(), e);
                }

                return enumeration;
            }
        }

        return null;
    }
}
