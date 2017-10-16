package com.ft.methodearticleinternalcomponentsmapper.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ft.jerseyhttpwrapper.config.EndpointConfiguration;

import javax.validation.Valid;

public class UppServiceConfiguration {
    private final EndpointConfiguration endpointConfiguration;
    private final ConnectionConfiguration connectionConfiguration;
    private final String hostHeader;

    public UppServiceConfiguration(@JsonProperty("endpointConfiguration") final EndpointConfiguration endpointConfiguration,
                                   @JsonProperty("numberOfConnectionAttempts") final ConnectionConfiguration connectionConfiguration,
                                   @JsonProperty("hostHeader") String hostHeader) {
        this.endpointConfiguration = endpointConfiguration;
        this.connectionConfiguration = connectionConfiguration;
        this.hostHeader = hostHeader;
    }

    @Valid
    public EndpointConfiguration getEndpointConfiguration() {
        return endpointConfiguration;
    }

    public ConnectionConfiguration getConnectionConfiguration() {
        return connectionConfiguration;
    }

    public String getHostHeader() {
        return hostHeader;
    }
}
