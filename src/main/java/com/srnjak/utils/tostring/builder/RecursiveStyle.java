package com.srnjak.utils.tostring.builder;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>Recursive style for Apache's
 * {@link org.apache.commons.lang3.builder.ReflectionToStringBuilder}.</p>
 *
 * <p>It enables easy filtering, which objects may participate in recursion.</p>
 */
public class RecursiveStyle extends RecursiveToStringStyle {

    /**
     * Start map indicator
     */
    private static final String MAP_START = "{";

    /**
     * End map indicator
     */
    private static final String MAP_END = "}";

    /**
     * Separator of map entries
     */
    private static final String MAP_ENTRIES_SEPARATOR = ",";

    /**
     * Separator between key and value of the map entry
     */
    private static final String KEY_VALUE_SEPARATOR = "=";

    /**
     * Builder for {@link RecursiveStyle}.
     */
    public static class Builder {

        private Class[] annotations = new Class[]{};
        private Class[] classes = new Class[]{};
        private String[] packages = new String[]{};

        private Class<? extends ToStringBuilder> toStringBuilderClass =
                ToStringBuilder.class;

        /**
         * Specifies annotations to be accepted.
         *
         * @param annotations If object type declares any annotation
         *                    from the array, the object will be accepted
         *                    in recursion.
         * @return this builder
         */
        public Builder acceptAnnotations(Class... annotations) {
            this.annotations = annotations;
            return this;
        }

        /**
         * Specifies classes to be accepted.
         *
         * @param classes Classes of object to be accepted in recursion.
         * @return this builder
         */
        public Builder acceptClasses(Class... classes) {
            this.classes = classes;
            return this;
        }

        /**
         * Specifies package prefixes to be accepted.
         *
         * @param packages If object type is part of package or subpackage from
         *                 the array, the object will be accepted in recursion.
         * @return this builder
         */
        public Builder acceptPackages(String... packages) {
            this.packages = packages;
            return this;
        }

        /**
         * Specifies the toStringBuilder class.
         *
         * @param toStringBuilderClass The toStringBuilder class.
         * @return this builder
         */
        public Builder toStringBuilder(
                Class<? extends ToStringBuilder> toStringBuilderClass) {

            this.toStringBuilderClass = toStringBuilderClass;
            return this;
        }

        /**
         * Builds the {@link RecursiveStyle}.
         *
         * @return The {@link RecursiveStyle} object.
         */
        public RecursiveStyle build() {
            return new RecursiveStyle(
                    annotations, classes, packages, toStringBuilderClass);
        }
    }

    /**
     * Provides builder for this class.
     *
     * @return The builder
     */
    public static Builder builder() {
        return new Builder();
    };
    private Class<? extends Annotation>[] annotations;
    private Class<?>[] classes;
    private String[] packages;

    private Class<? extends ToStringBuilder> toStringBuilderClass;


    /**
     * Constructor.
     *
     * @param annotations If object type declares any annotation
     *                    from the array, the object will be accepted
     *                    in recursion.
     * @param classes Classes of object to be accepted in recursion.
     * @param packages If object type is part of package or subpackage from
     *                 the array, the object will be accepted in recursion.
     * @param toStringBuilderClass The class to build string out of an object.
     */
    public RecursiveStyle(
            Class<? extends Annotation>[] annotations,
            Class<?>[] classes,
            String[] packages,
            Class<? extends ToStringBuilder> toStringBuilderClass) {

        this.annotations = annotations;
        this.classes = classes;
        this.packages = packages;

        this.toStringBuilderClass = toStringBuilderClass;
    }

    /**
     * Returns whether or not to recursively format the given Class.
     */
    @Override
    protected boolean accept(Class<?> clazz) {

        if (clazz.isEnum()) {
            return false;
        }

        return List.of(
                Arrays.stream(annotations).anyMatch(clazz::isAnnotationPresent),
                Arrays.asList(classes).contains(clazz),
                Arrays.stream(packages).anyMatch(
                        clazz.getPackageName()::startsWith))
                .contains(Boolean.TRUE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void appendDetail(
            StringBuffer buffer, String fieldName, Map<?, ?> map) {
        this.appendClassName(buffer, map);
        this.appendIdentityHashCode(buffer, map);
        this.appendDetail(
                buffer,
                fieldName,
                map.entrySet().toArray(Map.Entry[]::new));
    };

    /**
     * Append detail of map entry
     *
     * @param buffer string buffer to write into
     * @param fieldName name of the field
     * @param entries array of map entries
     */
    protected void appendDetail(
            StringBuffer buffer,
            String fieldName,
            Map.Entry<?, ?>[] entries) {

        buffer.append(MAP_START);

        for(int i = 0; i < entries.length; ++i) {
            Object key = entries[i].getKey();
            Object value = entries[i].getValue();

            if (i > 0) {
                buffer.append(MAP_ENTRIES_SEPARATOR);
            }

            this.appendDetail(buffer, fieldName, key);
            buffer.append(KEY_VALUE_SEPARATOR);

            if (value == null) {
                this.appendNullText(buffer, fieldName);
            } else {
                this.appendDetail(buffer, fieldName, value);
            }
        }

        buffer.append(MAP_END);
    }

    /**
     * Append detail of an object entry
     *
     * @param buffer string buffer to write into
     * @param fieldName name of the field
     * @param value the object value
     */
    @Override
    public void appendDetail(
            StringBuffer buffer,
            String fieldName,
            Object value) {

        if (!ClassUtils.isPrimitiveWrapper(value.getClass())
                && !String.class.equals(value.getClass())
                && this.accept(value.getClass())) {

            try {
                buffer.append(
                        toStringBuilderClass
                                .getMethod(
                                        "toString",
                                        Object.class,
                                        ToStringStyle.class)
                                .invoke(null, value, this));
            } catch (IllegalAccessException
                    | InvocationTargetException
                    | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

        } else {
            super.appendDetail(buffer, fieldName, value);
        }

    }
}
