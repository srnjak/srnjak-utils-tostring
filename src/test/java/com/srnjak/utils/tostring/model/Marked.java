package com.srnjak.utils.tostring.model;

/**
 * Type carrying {@link Marker}, so it is accepted by annotation filtering.
 */
@Marker
public class Marked {

    private final String label;

    public Marked(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
