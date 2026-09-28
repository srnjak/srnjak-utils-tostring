package com.srnjak.utils.tostring.model;

import com.srnjak.utils.tostring.builder.ToStringExclude;

/**
 * Field annotated with the library's own {@link ToStringExclude}.
 */
public class WithExcludedField {

    private final String visible = "yes";

    @ToStringExclude
    private final String secret = "no";

    private transient String temporary = "tmp";

    public String getVisible() {
        return visible;
    }

    public String getSecret() {
        return secret;
    }
}
