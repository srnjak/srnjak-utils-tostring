package com.srnjak.utils.tostring.model;

import java.util.Map;

/**
 * Holder of a map, to verify the custom map rendering of
 * {@code RecursiveStyle}.
 */
public class WithMap {

    private final Map<String, String> entries;

    public WithMap(Map<String, String> entries) {
        this.entries = entries;
    }

    public Map<String, String> getEntries() {
        return entries;
    }
}
