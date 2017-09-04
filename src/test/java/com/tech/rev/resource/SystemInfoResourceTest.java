package com.tech.rev.resource;

import com.tech.rev.builder.SystemInfoBuilder;
import com.tech.rev.model.SystemInfo;
import com.tech.rev.service.AccountService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SystemInfoResourceTest {
    private static SystemInfo systemInfo = new SystemInfoBuilder()
            .withAuthorName("Mike Holmes")
            .withApplicationName("Tranzfer")
            .build();

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new SystemInfoResource(systemInfo))
            .build();

    @Test
    public void serverRootReturnsExpectedSystemInfo() {
        assertThat(resources.client().target("/").request().get(SystemInfo.class))
                .isEqualTo(systemInfo);
    }
}
