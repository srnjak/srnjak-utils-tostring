package com.srnjak.utils.tostring.model;

/**
 * Holder of a value that cannot render itself.
 */
public class HoldsBrokenToString {

    private final String ok = "yes";

    private final BrokenToString broken = new BrokenToString();

    public String getOk() {
        return ok;
    }

    public BrokenToString getBroken() {
        return broken;
    }
}
