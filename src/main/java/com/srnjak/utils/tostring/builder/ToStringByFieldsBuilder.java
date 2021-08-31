package com.srnjak.utils.tostring.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Assists in implementing {@link Object#toString()} methods using reflection.
 *
 * If accessing the values caused <code>java.lang.RuntimeException</code>,
 * it reports <code>&lt;N/A&gt;</code> instead of value.
 *
 * @see ReflectionToStringBuilder
 */
public class ToStringByFieldsBuilder extends ReflectionToStringBuilder {

    /**
     * Logger
     */
    private static final Logger log =
            Logger.getLogger(ToStringByFieldsBuilder.class.getName());

    /**
     * <p>
     * Builds a <code>toString</code> value using
     * the default <code>ToStringStyle</code> through reflection.
     * </p>
     *
     * <p>
     * It uses <code>AccessibleObject.setAccessible</code> to gain access
     * to private fields. This means that it will throw a security exception
     * if run under a security manager, if the permissions are not set up
     * correctly. It is also not as efficient as testing explicitly.
     * </p>
     *
     * <p>
     * Transient members will be not be included, as they are likely derived.
     * Static fields will not be included. Superclass fields will be appended.
     * </p>
     *
     * @param object the Object to be output
     * @return the String result
     * @throws IllegalArgumentException if the Object is <code>null</code>
     * @see ToStringExclude
     * @see org.apache.commons.lang3.builder.ToStringExclude
     */
    public static String toString(final Object object) {
        return toString(object, null, false, false, null);
    }

    /**
     * <p>
     * Builds a <code>toString</code> value through reflection.
     * </p>
     *
     * <p>
     * It uses <code>AccessibleObject.setAccessible</code> to gain access
     * to private fields. This means that it will throw a security exception
     * if run under a security manager, if the permissions are not set up
     * correctly. It is also not as efficient as testing explicitly.
     * </p>
     *
     * <p>
     * Transient members will be not be included, as they are likely derived.
     * Static fields will not be included. Superclass fields will be appended.
     * </p>
     *
     * <p>
     * If the style is <code>null</code>, the default
     * <code>ToStringStyle</code> is used.
     * </p>
     *
     * @param object the Object to be output
     * @param style  the style of the <code>toString</code> to create,
     *               may be <code>null</code>
     * @return the String result
     * @throws IllegalArgumentException if the Object or
     *              <code>ToStringStyle</code> is <code>null</code>
     * @see ToStringExclude
     * @see org.apache.commons.lang3.builder.ToStringExclude
     */
    public static String toString(
            final Object object, final ToStringStyle style) {
        return toString(object, style, false, false, null);
    }

    /**
     * <p>
     * Builds a <code>toString</code> value through reflection.
     * </p>
     *
     * <p>
     * It uses <code>AccessibleObject.setAccessible</code> to gain access to
     * private fields. This means that it will throw a security exception if
     * run under a security manager, if the permissions are not set up
     * correctly. It is also not as efficient as testing explicitly.
     * </p>
     *
     * <p>
     * If the <code>outputTransients</code> is <code>true</code>, transient
     * members will be output, otherwise they are ignored, as they are likely
     * derived fields, and not part of the value of the Object.
     * </p>
     *
     * <p>
     * Static fields will not be included. Superclass fields will be appended.
     * </p>
     *
     * <p>
     * If the style is <code>null</code>, the default <code>ToStringStyle</code>
     * is used.
     * </p>
     *
     * @param object           the Object to be output
     * @param style            the style of the <code>toString</code> to create,
     *                         may be <code>null</code>
     * @param outputTransients whether to include transient fields
     * @return the String result
     * @throws IllegalArgumentException if the Object is <code>null</code>
     * @see ToStringExclude
     * @see org.apache.commons.lang3.builder.ToStringExclude
     */
    public static String toString(
            final Object object,
            final ToStringStyle style,
            final boolean outputTransients) {
        return toString(object, style, outputTransients, false, null);
    }

    /**
     * <p>
     * Builds a <code>toString</code> value through reflection.
     * </p>
     *
     * <p>
     * It uses <code>AccessibleObject.setAccessible</code> to gain access
     * to private fields. This means that it will throw a security exception
     * if run under a security manager, if the permissions are not set up
     * correctly. It is also not as efficient as testing explicitly.
     * </p>
     *
     * <p>
     * If the <code>outputTransients</code> is <code>true</code>, transient
     * fields will be output, otherwise they are ignored, as they are likely
     * derived fields, and not part of the value of the Object.
     * </p>
     *
     * <p>
     * If the <code>outputStatics</code> is <code>true</code>,
     * static fields will be output, otherwise they are ignored.
     * </p>
     *
     * <p>
     * Static fields will not be included. Superclass fields will be appended.
     * </p>
     *
     * <p>
     * If the style is <code>null</code>,
     * the default <code>ToStringStyle</code> is used.
     * </p>
     *
     * @param object           the Object to be output
     * @param style            the style of the <code>toString</code> to create,
     *                         may be <code>null</code>
     * @param outputTransients whether to include transient fields
     * @param outputStatics    whether to include static fields
     * @return the String result
     * @throws IllegalArgumentException if the Object is <code>null</code>
     * @see ToStringExclude
     * @see org.apache.commons.lang3.builder.ToStringExclude
     */
    public static String toString(
            final Object object,
            final ToStringStyle style,
            final boolean outputTransients,
            final boolean outputStatics) {

        return toString(object, style, outputTransients, outputStatics, null);
    }

    /**
     * <p>
     * Builds a <code>toString</code> value through reflection.
     * </p>
     *
     * <p>
     * It uses <code>AccessibleObject.setAccessible</code> to gain access
     * to private fields. This means that it will throw a security exception
     * if run under a security manager, if the permissions are not set up
     * correctly. It is also not as efficient as testing explicitly.
     * </p>
     *
     * <p>
     * If the <code>outputTransients</code> is <code>true</code>, transient
     * fields will be output, otherwise they are ignored, as they are likely
     * derived fields, and not part of the value of the Object.
     * </p>
     *
     * <p>
     * If the <code>outputStatics</code> is <code>true</code>,
     * static fields will be output, otherwise they are
     * ignored.
     * </p>
     *
     * <p>
     * Superclass fields will be appended up to and including the specified
     * superclass. A null superclass is treated as
     * <code>java.lang.Object</code>.
     * </p>
     *
     * <p>
     * If the style is <code>null</code>, the default
     * <code>ToStringStyle</code> is used.
     * </p>
     *
     * @param <T>              the type of the object
     * @param object           the Object to be output
     * @param style            the style of the <code>toString</code> to create,
     *                         may be <code>null</code>
     * @param outputTransients whether to include transient fields
     * @param outputStatics    whether to include static fields
     * @param reflectUpToClass the superclass to reflect up to (inclusive),
     *                         may be <code>null</code>
     * @return the String result
     * @throws IllegalArgumentException if the Object is <code>null</code>
     * @see ToStringExclude
     * @see org.apache.commons.lang3.builder.ToStringExclude
     */
    public static <T> String toString(
            final T object,
            final ToStringStyle style,
            final boolean outputTransients,
            final boolean outputStatics,
            final Class<? super T> reflectUpToClass) {

        return new ToStringByFieldsBuilder(
                object,
                style,
                null,
                reflectUpToClass,
                outputTransients,
                outputStatics)
                .toString();
    }

    /**
     * Builds a String for a toString method excluding the given field names.
     *
     * @param object            The object to "toString".
     * @param excludeFieldNames The field names to exclude.
     *                          Null excludes nothing.
     * @return The toString value.
     */
    public static String toStringExclude(
            final Object object,
            final Collection<String> excludeFieldNames) {
        return toStringExclude(object, toNoNullStringArray(excludeFieldNames));
    }

    /**
     * Converts the given Collection into an array of Strings.
     * The returned array does not contain <code>null</code>
     * entries. Note that {@link java.util.Arrays#sort(Object[])}
     * will throw an {@link NullPointerException} if an array element
     * is <code>null</code>.
     *
     * @param collection The collection to convert
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
     * Builds a String for a toString method excluding the given field names.
     *
     * @param object            The object to "toString".
     * @param excludeFieldNames The field names to exclude
     * @return The toString value.
     */
    public static String toStringExclude(
            final Object object,
            final String... excludeFieldNames) {

        return new ToStringByFieldsBuilder(object)
                .setExcludeFieldNames(excludeFieldNames).toString();
    }

    /**
     * <p>
     * Constructor.
     * </p>
     *
     * <p>
     * This constructor outputs using the default style set with
     * <code>setDefaultStyle</code>.
     * </p>
     *
     * @param object the Object to build a <code>toString</code> for,
     *               must not be <code>null</code>
     * @throws IllegalArgumentException if the Object passed in is
     *              <code>null</code>
     */
    public ToStringByFieldsBuilder(Object object) {
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
     * @param object the Object to build a <code>toString</code> for,
     *               must not be <code>null</code>
     * @param style  the style of the <code>toString</code> to create,
     *               may be <code>null</code>
     * @throws IllegalArgumentException if the Object passed in is
     *              <code>null</code>
     */
    public ToStringByFieldsBuilder(Object object, ToStringStyle style) {
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
     * @param object the Object to build a <code>toString</code> for
     * @param style  the style of the <code>toString</code> to create,
     *               may be <code>null</code>
     * @param buffer the <code>StringBuffer</code> to populate,
     *               may be <code>null</code>
     * @throws IllegalArgumentException if the Object passed in is
     *              <code>null</code>
     */
    public ToStringByFieldsBuilder(
            Object object, ToStringStyle style, StringBuffer buffer) {
        super(object, style, buffer);
    }

    /**
     * Constructor.
     *
     * @param <T>              the type of the object
     * @param object           the Object to build a <code>toString</code> for
     * @param style            the style of the <code>toString</code> to create,
     *                         may be <code>null</code>
     * @param buffer           the <code>StringBuffer</code> to populate,
     *                         may be <code>null</code>
     * @param reflectUpToClass the superclass to reflect up to (inclusive),
     *                         may be <code>null</code>
     * @param outputTransients whether to include transient fields
     * @param outputStatics    whether to include static fields
     */
    public <T extends Object> ToStringByFieldsBuilder(
            T object,
            ToStringStyle style,
            StringBuffer buffer,
            Class<? super T> reflectUpToClass,
            boolean outputTransients,
            boolean outputStatics) {

        super(
                object,
                style,
                buffer,
                reflectUpToClass,
                outputTransients,
                outputStatics);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean accept(Field field) {
        return super.accept(field)
                && !field.isAnnotationPresent(ToStringExclude.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void appendFieldsIn(Class<?> clazz) {
        if (clazz.isArray()) {
            this.reflectionAppendArray(this.getObject());
            return;
        }

        Field[] fields = clazz.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);
        for (Field field : fields) {
            String fieldName = field.getName();
            if (this.accept(field)) {
                try {
                    Object fieldValue = this.getValue(field);
                    this.append(fieldName, fieldValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (RuntimeException e) {
                    log.finer(e::toString);
                    log.finest(() -> ExceptionUtils.getStackTrace(e));

                    this.append(null, "<N/A>");
                }
            }
        }
    }
}