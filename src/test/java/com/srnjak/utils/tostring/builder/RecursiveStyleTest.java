package com.srnjak.utils.tostring.builder;

import com.srnjak.utils.tostring.model.Address;
import com.srnjak.utils.tostring.model.Color;
import com.srnjak.utils.tostring.model.Marked;
import com.srnjak.utils.tostring.model.Marker;
import com.srnjak.utils.tostring.model.Person;
import com.srnjak.utils.tostring.model.WithMap;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecursiveStyleTest {

    private static final Person PERSON = new Person(
            "Jane", 30, new Address("Main Street 1", "Springfield"), Color.RED);

    /**
     * Holder of an annotated type, used for annotation based filtering.
     */
    static class Holder {
        private final Marked marked = new Marked("tag");
    }

    private static RecursiveStyle.Builder style() {
        return RecursiveStyle.builder()
                .toStringBuilder(ToStringByFieldsBuilder.class);
    }

    @Test
    void usesFieldBasedBuilderByDefault() {
        String result = ToStringByFieldsBuilder.toString(
                PERSON,
                RecursiveStyle.builder()
                        .acceptClasses(Address.class)
                        .build());

        assertTrue(result.contains("street=Main Street 1"), result);
    }

    @Test
    void rejectsBuilderThatCannotServeRecursion() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> RecursiveStyle.builder()
                        .toStringBuilder(ToStringBuilder.class)
                        .build());

        assertTrue(
                e.getMessage().contains(ToStringBuilder.class.getName()),
                e.getMessage());
    }

    @Test
    void recursesIntoAcceptedClass() {
        String result = ToStringByFieldsBuilder.toString(
                PERSON, style().acceptClasses(Address.class).build());

        assertTrue(result.contains("street=Main Street 1"), result);
    }

    @Test
    void doesNotRecurseIntoRejectedClass() {
        String result = ToStringByFieldsBuilder.toString(
                PERSON, style().build());

        assertTrue(result.contains("Address@"), result);
        assertFalse(result.contains("street="), result);
    }

    @Test
    void recursesIntoAcceptedPackage() {
        String result = ToStringByFieldsBuilder.toString(
                PERSON,
                style().acceptPackages("com.srnjak.utils.tostring.model")
                        .build());

        assertTrue(result.contains("street=Main Street 1"), result);
    }

    @Test
    void recursesIntoAnnotatedClass() {
        String result = ToStringByFieldsBuilder.toString(
                new Holder(), style().acceptAnnotations(Marker.class).build());

        assertTrue(result.contains("label=tag"), result);
    }

    @Test
    void neverRecursesIntoEnums() {
        String result = ToStringByFieldsBuilder.toString(
                PERSON,
                style().acceptPackages("com.srnjak.utils.tostring.model")
                        .build());

        assertTrue(result.contains("favorite=RED"), result);
    }

    @Test
    void rendersMapEntriesWithNullValues() {
        Map<String, String> entries = new LinkedHashMap<>();
        entries.put("a", "1");
        entries.put("b", null);

        String result = ToStringByFieldsBuilder.toString(
                new WithMap(entries), style().build());

        assertTrue(result.contains("{a=1,b=<null>}"), result);
    }
}
