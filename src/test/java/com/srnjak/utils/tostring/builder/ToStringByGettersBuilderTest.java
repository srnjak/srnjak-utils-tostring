package com.srnjak.utils.tostring.builder;

import com.srnjak.utils.tostring.model.Address;
import com.srnjak.utils.tostring.model.Computed;
import com.srnjak.utils.tostring.model.Exploding;
import com.srnjak.utils.tostring.model.WithExcludedGetter;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Every public entry point must produce property based output; none may fall
 * through to the inherited field based implementation.
 */
class ToStringByGettersBuilderTest {

    private static final ToStringStyle STYLE =
            ToStringStyle.NO_CLASS_NAME_STYLE;

    private static final Address ADDRESS =
            new Address("Main Street 1", "Springfield");

    private static Computed computed() {
        return new Computed("Jane", "Smith");
    }

    @Test
    void readsPropertiesThatHaveNoBackingField() {
        String result = ToStringByGettersBuilder.toString(
                computed(), STYLE, null);

        assertTrue(result.contains("fullName=Jane Smith"), result);
    }

    @Test
    void singleArgumentToStringUsesGetters() {
        String result = ToStringByGettersBuilder.toString(computed());

        assertTrue(result.contains("fullName=Jane Smith"), result);
    }

    @Test
    void twoArgumentToStringUsesGetters() {
        String result = ToStringByGettersBuilder.toString(computed(), STYLE);

        assertTrue(result.contains("fullName=Jane Smith"), result);
    }

    @Test
    void fiveArgumentToStringUsesGetters() {
        String result = ToStringByGettersBuilder.toString(
                computed(), STYLE, false, false, null);

        assertTrue(result.contains("fullName=Jane Smith"), result);
    }

    @Test
    void doesNotAppendClassProperty() {
        String result = ToStringByGettersBuilder.toString(ADDRESS, STYLE, null);

        assertTrue(result.contains("street=Main Street 1"), result);
        assertFalse(result.contains("class="), result);
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
    void reportsNotAvailableWhenGetterThrows() {
        String result = ToStringByGettersBuilder.toString(
                new Exploding(), STYLE, null);

        assertTrue(result.contains("fine=ok"), result);
        assertTrue(result.contains("<N/A>"), result);
    }

    @Test
    void toStringExcludeSkipsNamedProperties() {
        String result = ToStringByGettersBuilder.toStringExclude(
                ADDRESS, "city");

        assertTrue(result.contains("street=Main Street 1"), result);
        assertFalse(result.contains("city="), result);
    }

    @Test
    void toStringIncludeKeepsOnlyNamedProperties() {
        String result = ToStringByGettersBuilder.toStringInclude(
                computed(), "fullName");

        assertTrue(result.contains("fullName=Jane Smith"), result);
        assertFalse(result.contains("first="), result);
    }
}
