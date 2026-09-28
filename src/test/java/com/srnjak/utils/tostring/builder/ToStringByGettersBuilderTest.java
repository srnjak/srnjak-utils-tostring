package com.srnjak.utils.tostring.builder;

import com.srnjak.utils.tostring.model.Address;
import com.srnjak.utils.tostring.model.Computed;
import com.srnjak.utils.tostring.model.Exploding;
import com.srnjak.utils.tostring.model.WithExcludedGetter;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests exercise the three argument
 * {@code toString(object, style, reflectUpToClass)} overload, which is the
 * only entry point that actually constructs a
 * {@link ToStringByGettersBuilder}. See the disabled tests at the bottom
 * for the reason.
 */
class ToStringByGettersBuilderTest {

    private static final ToStringStyle STYLE =
            ToStringStyle.NO_CLASS_NAME_STYLE;

    private static final Address ADDRESS =
            new Address("Main Street 1", "Springfield");

    @Test
    void readsPropertiesThatHaveNoBackingField() {
        String result = ToStringByGettersBuilder.toString(
                new Computed("Jane", "Smith"), STYLE, null);

        assertTrue(result.contains("fullName=Jane Smith"), result);
    }

    @Test
    void skipsGetterAnnotatedWithToStringExclude() {
        String result = ToStringByGettersBuilder.toString(
                new WithExcludedGetter(), STYLE, null);

        assertTrue(result.contains("visible=yes"), result);
        assertFalse(result.contains("secret=no"), result);
    }

    @Test
    void skipsFieldAnnotatedWithCommonsToStringExclude() {
        String result = ToStringByGettersBuilder.toString(
                new WithExcludedGetter(), STYLE, null);

        assertFalse(result.contains("alsoSecret"), result);
    }

    @Test
    void toStringExcludeSkipsNamedProperties() {
        String result = ToStringByGettersBuilder.toStringExclude(
                ADDRESS, "city");

        assertTrue(result.contains("street=Main Street 1"), result);
        assertFalse(result.contains("city="), result);
    }

    // --- Defects documented below; enable once they are fixed. ---

    /**
     * {@code toString(Object)} and {@code toString(Object, ToStringStyle)}
     * delegate to {@code toString(object, style, false, false, null)}. This
     * class declares no such five argument overload, so the call resolves to
     * the inherited static {@code ReflectionToStringBuilder.toString(...)},
     * which builds a plain field based builder. Getters are never used.
     */
    @Test
    @Disabled("Defect: one and two argument toString fall through to the "
            + "inherited field based ReflectionToStringBuilder.toString")
    void twoArgumentToStringShouldUseGetters() {
        String result = ToStringByGettersBuilder.toString(
                new Computed("Jane", "Smith"), STYLE);

        assertTrue(result.contains("fullName=Jane Smith"), result);
    }

    /**
     * {@code appendFieldsIn} is also invoked for {@code Object.class}, where
     * the stop class stays {@code null}, so {@code Introspector} reports the
     * {@code getClass()} property and it leaks into the output.
     */
    @Test
    void doesNotAppendClassProperty() {
        String result = ToStringByGettersBuilder.toString(ADDRESS, STYLE, null);

        assertFalse(result.contains("class="), result);
    }

    /**
     * The class javadoc promises {@code <N/A>} when reading a value throws.
     * The {@code InvocationTargetException} branch wraps the cause in a
     * {@code RuntimeException} and rethrows it from inside a catch block, so
     * the sibling {@code catch (RuntimeException)} never sees it.
     */
    @Test
    @Disabled("Defect: a throwing getter propagates RuntimeException instead "
            + "of rendering <N/A>, contrary to the class javadoc")
    void shouldReportNotAvailableWhenGetterThrows() {
        String result = ToStringByGettersBuilder.toString(
                new Exploding(), STYLE, null);

        assertTrue(result.contains("fine=ok"), result);
        assertTrue(result.contains("<N/A>"), result);
    }
}
