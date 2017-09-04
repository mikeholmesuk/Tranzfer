package com.tech.rev.builder;

import com.tech.rev.model.Account;
import com.tech.rev.model.SystemInfo;
import org.joda.time.DateTime;
import uk.org.fyodor.generators.RDG;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class SystemInfoBuilder {

    private String authorName = RDG.string().next();
    private String applicationName = RDG.string().next();

    public SystemInfoBuilder withAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public SystemInfoBuilder withApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    public SystemInfo build() {
        return new SystemInfo(this.authorName, this.applicationName);
    }
}
