package com.heter.the.message.common.net.annotation;

import com.heter.the.message.common.net.message.Message;

import java.lang.annotation.*;

/**
 * The annotation type is used in {@link Message}
 * to specify module and cmd of the given message.
 * @author kinson
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageMeta {

	short module() default 0;

	byte cmd() default 0;

}
