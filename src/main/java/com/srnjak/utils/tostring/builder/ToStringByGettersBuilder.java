package com.srnjak.utils.tostring.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Assists in implementing {@link Object#toString()} methods
 * from getters using reflection.
 *
 * If accessing the values caused <code>java.lang.RuntimeException</code>,
 * it reports <code>&lt;N/A&gt;</code> instead of value.
 *
 * @see ReflectionToStringBuilder
 */
public class ToStringByGettersBuilder extends ReflectionToStringBuilder {

    /**
     * Logger
     */
    private static final Logger log =
            Logger.getLogger(ToStringByGettersBuilder.class.getName());

    /**
     * <p>
     * Builds a <code>toString</code> value from getters using the default
     * <code>ToStringStyle</code> through reflection.
     * </p>
     *
     * @param object
     *            the Object to be output
     * @return the String result
     * @throws IllegalArgumentException
     *             if the Object is <code>null</code>
     *
     * @see ToStringExclude
     * @see org.apache.commons.lang3.builder.ToStringExclude
     */
    public static String toString(final Object object) {
        return toString(object, null, false, false, null);
    }

    /**
     * <p>
     * Builds a <code>toString</code> value from getters through reflection.
     * </p>
     *
     * <p>
     * If the style is <code>null</code>, the default
     * <code>ToStringStyle</code> is used.
     * </p>
     *
     * @param object
     *            the Object to be output
     * @param style
     *            the style of the <code>toString</code> to create,
     *            may be <code>null</code>
     * @return the String result
     * @throws IllegalArgumentException
     *             if the Object is <code>null</code>
     *
     * @see ToStringExclude
     * @see org.apache.commons.lang3.builder.ToStringExclude
     */
    public static String toString(
            final Object object, final ToStringStyle style) {
        return toString(object, style, false, false, null);
    }

    /**
     * <p>
     * Builds a <code>toString</code> value from getters through reflection.
     * </p>
     *
     * <p>
     * Superclass fields will be appended up to and including the specified
     * superclass. A null superclass is treated as
     * <code>java.lang.Object</code>.
     * </p>
     *
     * <p>
     * If the style is <code>null</code>,
     * the default <code>ToStringStyle</code> is used.
     * </p>
     *
     * @param <T>
     *            the type of the object
     * @param object
     *            the Object to be output
     * @param style
     *            the style of the <code>toString</code> to create,
     *            may be <code>null</code>
     * @param reflectUpToClass
     *            the superclass to reflect up to (inclusive),
     *            may be <code>null</code>
     * @return the String result
     * @throws IllegalArgumentException
     *             if the Object is <code>null</code>
     *
     * @see ToStringExclude
     * @see org.apache.commons.lang3.builder.ToStringExclude
     */
    public static <T> String toString(
            final T object,
            final ToStringStyle style,
            final Class<? super T> reflectUpToClass) {

        return new ToStringByGettersBuilder(
                object,
                style,
                null,
                reflectUpToClass)
                .toString();
    }

    /**
     * Builds a String for a toString method from getters
     * excluding the given property names.
     *
     * @param object
     *            The object to "toString".
     * @param excludeFieldNames
     *            The field names to exclude. Null excludes nothing.
     * @return The toString value.
     */
    public static String toStringExclude(
            final Object object, final Collection<String> excludeFieldNames) {
        return toStringExclude(object, toNoNullStringArray(excludeFieldNames));
    }

    /**
     * Builds a String for a toString method from getters
     * excluding the given property names.
     *
     * @param object
     *            The object to "toString".
     * @param excludeFieldNames
     *            The field names to exclude
     * @return The toString value.
     */
    public static String toStringExclude(
            final Object object,
            final String... excludeFieldNames) {

        return new ToStringByGettersBuilder(object)
                .setExcludeFieldNames(excludeFieldNames).toString();
    }

    /**
     * Converts the given Collection into an array of Strings.
     * The returned array does not contain <code>null</code>
     * entries.
     *
     * @param collection
     *            The collection to convert
     * @return A new array of Strings.
     */
    static String[] toNoNullStringArray(final Collection<String> collection) {
        if (collection == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        final List<String> list = new ArrayList<>(collection.size());
        for (final String e : collection) {
            if (e != null) {
                list.add(e);
            }
        }
        return list.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    /**
     * <p>
     * Constructor.
     * </p>
     *
     * <p>
     * This constructor outputs using the default style set
     * with <code>setDefaultStyle</code>.
     * </p>
     *
     * @param object
     *            the Object to build a <code>toString</code> for,
     *            must not be <code>null</code>
     * @throws IllegalArgumentException
     *             if the Object passed in is <code>null</code>
     */
    public ToStringByGettersBuilder(Object object) {
        super(object);
    }

    /**
     * <p>
     * Constructor.
     * </p>
     *
     * <p>
     * If the style is <code>null</code>, the default style is used.
     * </p>
     *
     * @param object
     *            the Object to build a <code>toString</code> for,
     *            must not be <code>null</code>
     * @param style
     *            the style of the <code>toString</code> to create,
     *            may be <code>null</code>
     * @throws IllegalArgumentException
     *             if the Object passed in is <code>null</code>
     */
    public ToStringByGettersBuilder(Object object, ToStringStyle style) {
        super(object, style);
    }

    /**
     * <p>
     * Constructor.
     * </p>
     *
     * <p>
     * If the style is <code>null</code>, the default style is used.
     * </p>
     *
     * <p>
     * If the buffer is <code>null</code>, a new one is created.
     * </p>
     *
     * @param object
     *            the Object to build a <code>toString</code> for
     * @param style
     *            the style of the <code>toString</code> to create,
     *            may be <code>null</code>
     * @param buffer
     *            the <code>StringBuffer</code> to populate,
     *            may be <code>null</code>
     * @throws IllegalArgumentException
     *             if the Object passed in is <code>null</code>
     */
    public ToStringByGettersBuilder(
            Object object, ToStringStyle style, StringBuffer buffer) {
        super(object, style, buffer);
    }

    /**
     * Constructor.
     *
     * @param <T>
     *            the type of the object
     * @param object
     *            the Object to build a <code>toString</code> for
     * @param style
     *            the style of the <code>toString</code> to create,
     *            may be <code>null</code>
     * @param buffer
     *            the <code>StringBuffer</code> to populate,
     *            may be <code>null</code>
     * @param reflectUpToClass
     *            the superclass to reflect up to (inclusive),
     *            may be <code>null</code>
     */
    public <T extends Object> ToStringByGettersBuilder(
            T object,
            ToStringStyle style,
            StringBuffer buffer,
            Class<? super T> reflectUpToClass) {

        super(
                object,
                style,
                buffer,
                reflectUpToClass,
                false,
                false);
    }

    /**
     * Returns whether or not to append the given property
     * defined by <code>PropertyDescriptor</code>.
     * <ul>
     * <li>Inner class fields are not appended.</li>
     * </ul>
     *
     * @param propertyDescriptor
     *            The PropertyDescriptor for property to test.
     * @return Whether or not to append the given property
     * defined by <code>PropertyDescriptor</code>.
     */
    protected boolean accept(final PropertyDescriptor propertyDescriptor) {

        if (propertyDescriptor.getReadMethod() == null) {
            return false;
        }

        if (this.excludeFieldNames != null
                && Arrays.binarySearch(
                this.excludeFieldNames,
                propertyDescriptor.getName()) >= 0) {
            return false;
        }

        String propertyName = propertyDescriptor.getName();
        Method getter = propertyDescriptor.getReadMethod();
        Class<?> declaringClass = getter.getDeclaringClass();

        Field field = FieldUtils.getField(declaringClass, propertyName, true);

        return !getter.isAnnotationPresent(ToStringExclude.class)
                && (field == null || !field.isAnnotationPresent(
                        org.apache.commons.lang3.builder
                                .ToStringExclude.class));
    }

    /**
     * <p>
     * Appends the properties and values defined by
     * the given object of the given Class.
     * </p>
     *
     * <p>
     * If a cycle is detected as an object is &quot;toString()'ed&quot;,
     * such an object is rendered as if <code>Object.toString()</code>
     * had been called and not implemented by the object.
     * </p>
     *
     * @param clazz
     *            The class of object parameter
     */
    @Override
    protected void appendFieldsIn(final Class<?> clazz) {
        try {
            if (clazz.isArray()) {
                this.reflectionAppendArray(this.getObject());
                return;
            }

            Class<?> superclass = null;
            if (!clazz.equals(Object.class)) {
                superclass = clazz.getSuperclass();
            }

            for(PropertyDescriptor propertyDescriptor :
                    Introspector.getBeanInfo(clazz, superclass)
                            .getPropertyDescriptors()) {

                final String fieldName = propertyDescriptor.getName();
                if (this.accept(propertyDescriptor)) {

                    try {
                        this.append(
                                fieldName, this.getValue(propertyDescriptor));

                    } catch (IllegalAccessException |
                            IllegalArgumentException |
                            InvocationTargetException e) {
                        throw new RuntimeException(e);

                    } catch (RuntimeException e) {
                        log.finer(e::toString);
                        log.finest(() -> ExceptionUtils.getStackTrace(e));

                        this.append(null, "<N/A>");
                    }
                }
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * Calls <code>java.lang.reflect.Method.invoke(Object)</code>.
     * </p>
     *
     * @param propertyDescriptor
     *            The PropertyDescriptor to query.
     * @return The Object from the given Field.
     *
     * @throws IllegalArgumentException
     *             see {@link Method#invoke(
     *                   Object, Object... args)}
     * @throws IllegalAccessException
     *             see {@link Method#invoke(
     *                   Object, Object... args)}
     * @throws InvocationTargetException
     *             see {@link Method#invoke(
     *                   Object, Object... args)}
     *
     */
    protected Object getValue(final PropertyDescriptor propertyDescriptor)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        return propertyDescriptor.getReadMethod().invoke(this.getObject());
    }

}