package com.tech.rev.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SystemInfo {
    private final String authorName;
    private final String applicationName;

    @JsonCreator
    public SystemInfo (
            @JsonProperty("authorName") String authorName,
            @JsonProperty("applicationName") String applicationName) {
        this.authorName = authorName;
        this.applicationName = applicationName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SystemInfo that = (SystemInfo) o;

        return new EqualsBuilder()
                .append(authorName, that.authorName)
                .append(applicationName, that.applicationName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(authorName)
                .append(applicationName)
                .toHashCode();
    }
}
