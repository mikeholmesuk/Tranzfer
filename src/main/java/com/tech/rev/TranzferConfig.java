package com.tech.rev;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tech.rev.model.SystemInfo;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TranzferConfig extends Configuration {
    @Valid
    @NotNull
    private DataSourceFactory database;
    @Valid
    @NotNull
    private FlywayFactory flywayFactory;
    @Valid
    @NotNull
    private SystemInfo systemInfo;

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }

    @JsonProperty("flyway")
    public FlywayFactory getFlywayFactory() {
        return flywayFactory;
    }

    @JsonProperty("flyway")
    public void setFlywayFactory(FlywayFactory factory) {
        this.flywayFactory = factory;
    }

    @JsonProperty("system")
    public SystemInfo getSystemInfo() {
        return this.systemInfo;
    }

    @JsonProperty("system")
    public void setSystemInfo(SystemInfo systemInfo) {
        System.out.println("Setting system info :: " + systemInfo);
        this.systemInfo = systemInfo;
    }
}
