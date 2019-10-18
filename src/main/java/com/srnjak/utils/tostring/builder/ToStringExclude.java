package com.srnjak.utils.tostring.builder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to exclude a property from being being used by
 * the {@link ToStringByGettersBuilder}.
 *
 * The annotation should be placed on getter.
 *
 * @see org.apache.commons.lang3.builder.ToStringExclude
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface ToStringExclude {

}