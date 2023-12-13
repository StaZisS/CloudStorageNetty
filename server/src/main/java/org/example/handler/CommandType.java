package org.example.handler;

import org.example.entity.ResponseTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking command handlers class.
 * Used in ServerInitializer to combine endpoint and handler class
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandType {
    ResponseTypeEnum value();
}
