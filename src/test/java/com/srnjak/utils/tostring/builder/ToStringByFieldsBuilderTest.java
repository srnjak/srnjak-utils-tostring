package com.srnjak.utils.tostring.builder;

import com.srnjak.utils.tostring.model.Address;
import com.srnjak.utils.tostring.model.HoldsBrokenToString;
import com.srnjak.utils.tostring.model.WithExcludedField;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToStringByFieldsBuilderTest {

    private static final Address ADDRESS =
            new Address("Main Street 1", "Springfield");

    @Test
    void rendersDeclaredFieldsInAlphabeticalOrder() {
        assertEquals(
                "[city=Springfield,street=Main Street 1]",
                ToStringByFieldsBuilder.toString(
                        ADDRESS, ToStringStyle.NO_CLASS_NAME_STYLE));
    }

    @Test
    void defaultStyleIncludesClassNameAndValues() {
        String result = ToStringByFieldsBuilder.toString(ADDRESS);

        assertTrue(result.contains("Address@"), result);
        assertTrue(result.contains("street=Main Street 1"), result);
    }

    @Test
    void skipsFieldAnnotatedWithToStringExclude() {
        String result = ToStringByFieldsBuilder.toString(
                new WithExcludedField(), ToStringStyle.NO_CLASS_NAME_STYLE);

        assertTrue(result.contains("visible=yes"), result);
        assertFalse(result.contains("secret"), result);
    }

    @Test
    void skipsTransientFields() {
        String result = ToStringByFieldsBuilder.toString(
                new WithExcludedField(), ToStringStyle.NO_CLASS_NAME_STYLE);

        assertFalse(result.contains("temporary"), result);
    }

    @Test
    void agreesWithGetterBasedBuilderOnMemberOrder() {
        assertEquals(
                ToStringByGettersBuilder.toString(
                        ADDRESS, ToStringStyle.NO_CLASS_NAME_STYLE),
                ToStringByFieldsBuilder.toString(
                        ADDRESS, ToStringStyle.NO_CLASS_NAME_STYLE),
                "both builders must order members the same way");
    }

    @Test
    void reportsNotAvailableWhenValueCannotRenderItself() {
        String result = ToStringByFieldsBuilder.toString(
                new HoldsBrokenToString(), ToStringStyle.NO_CLASS_NAME_STYLE);

        assertEquals("[broken=<N/A>,ok=yes]", result);
    }

    @Test
    void toStringExcludeSkipsNamedFields() {
        String result = ToStringByFieldsBuilder.toStringExclude(
                ADDRESS, "city");

        assertTrue(result.contains("street=Main Street 1"), result);
        assertFalse(result.contains("city="), result);
    }
}
