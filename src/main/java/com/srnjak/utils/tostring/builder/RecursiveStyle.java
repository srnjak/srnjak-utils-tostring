package com.srnjak.utils.tostring.builder;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * <p>Recursive style for Apache's
 * {@link org.apache.commons.lang3.builder.ReflectionToStringBuilder}.</p>
 *
 * <p>It enables easy filtering, which objects may participate in recursion.</p>
 */
public class RecursiveStyle extends RecursiveToStringStyle {

    /**
     * Builder for {@link RecursiveStyle}.
     */
    public static class Builder {
        private Class[] annotations = new Class[]{};
        private Class[] classes = new Class[]{};
        private String[] packages = new String[]{};

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
         * Builds the {@link RecursiveStyle}.
         *
         * @return The {@link RecursiveStyle} object.
         */
        public RecursiveStyle build() {
            return new RecursiveStyle(annotations, classes, packages);
        }
    }

    /**
     * Provides builder for this class.
     *
     * @return The builder
     */
    public static Builder builder() {
        return new Builder();
    }

    private Class[] annotations;
    private Class[] classes;
    private String[] packages;

    /**
     * Constructor.
     *
     * @param annotations If object type declares any annotation
     *                    from the array, the object will be accepted
     *                    in recursion.
     * @param classes Classes of object to be accepted in recursion.
     * @param packages If object type is part of package or subpackage from
     *                 the array, the object will be accepted in recursion.
     */
    public RecursiveStyle(
            Class[] annotations, Class[] classes, String[] packages) {

        this.annotations = annotations;
        this.classes = classes;
        this.packages = packages;
    }

    /**
     * Returns whether or not to recursively format the given Class.
     */
    @Override
    protected boolean accept(Class<?> clazz) {

        return Stream.of(
                Arrays.stream(annotations).anyMatch(clazz::isAnnotationPresent),
                Arrays.stream(classes).anyMatch(clazz::equals),
                Arrays.stream(packages).anyMatch(
                        clazz.getPackageName()::startsWith))
                .anyMatch(Boolean.TRUE::equals);
    }
}
