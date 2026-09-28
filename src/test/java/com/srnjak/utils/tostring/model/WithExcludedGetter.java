package com.srnjak.utils.tostring.model;

import com.srnjak.utils.tostring.builder.ToStringExclude;

/**
 * Getter annotated with the library's own {@link ToStringExclude}, and a
 * field annotated with commons-lang3's variant. Both are honored by
 * {@code ToStringByGettersBuilder}.
 */
public class WithExcludedGetter {

    private final String visible = "yes";
    private final String secret = "no";

    @org.apache.commons.lang3.builder.ToStringExclude
    private final String alsoSecret = "no";

    public String getVisible() {
        return visible;
    }

    @ToStringExclude
    public String getSecret() {
        return secret;
    }

    public String getAlsoSecret() {
        return alsoSecret;
    }
}
