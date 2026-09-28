package com.srnjak.utils.tostring.model;

/**
 * Value whose own toString throws, so the failure happens after the style
 * has already written the member name.
 */
public class BrokenToString {

    @Override
    public String toString() {
        throw new IllegalStateException("boom");
    }
}
