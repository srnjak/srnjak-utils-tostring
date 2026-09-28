package com.srnjak.utils.tostring.model;

/**
 * Exposes a property that has no backing field, so getter based output is
 * distinguishable from field based output.
 */
public class Computed {

    private final String first;
    private final String last;

    public Computed(String first, String last) {
        this.first = first;
        this.last = last;
    }

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    public String getFullName() {
        return first + " " + last;
    }
}
