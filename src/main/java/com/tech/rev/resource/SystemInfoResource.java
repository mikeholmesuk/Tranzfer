package com.tech.rev.resource;

import com.tech.rev.model.SystemInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class SystemInfoResource {
    Logger logger = LoggerFactory.getLogger(SystemInfoResource.class);

    private final SystemInfo systemInfo;

    public SystemInfoResource(SystemInfo systemInfo) {
        this.systemInfo = systemInfo;
    }

    @GET
    public SystemInfo getSystemInfo() {
        logger.info("Returning system info");
        return this.systemInfo;
    }
}
