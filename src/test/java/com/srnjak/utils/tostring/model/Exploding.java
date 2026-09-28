package com.srnjak.utils.tostring.model;

/**
 * Getter that throws, to verify the {@code <N/A>} fallback.
 */
public class Exploding {

    private final String fine = "ok";

    public String getFine() {
        return fine;
    }

    public String getBroken() {
        throw new IllegalStateException("boom");
    }
}
