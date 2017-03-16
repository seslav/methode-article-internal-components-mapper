package com.ft.methodearticleinternalcomponentsmapper.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;

public class MethodeArticleInternalComponentsMapperConfiguration extends Configuration {

    private final MethodeArticleMapperConfiguration methodeArticleMapperConfiguration;
    private final ConsumerConfiguration consumerConfiguration;
    private final ProducerConfiguration producerConfiguration;
    private final String contentUriPrefix;

    public MethodeArticleInternalComponentsMapperConfiguration(@JsonProperty("consumer") ConsumerConfiguration consumerConfiguration,
                                                               @JsonProperty("producer") ProducerConfiguration producerConfiguration,
                                                               @JsonProperty("methodeArticleMapper") MethodeArticleMapperConfiguration methodeArticleMapperConfiguration,
                                                               @JsonProperty("contentUriPrefix") String contentUriPrefix) {
        this.consumerConfiguration = consumerConfiguration;
        this.producerConfiguration = producerConfiguration;
        this.methodeArticleMapperConfiguration = methodeArticleMapperConfiguration;
        this.contentUriPrefix = contentUriPrefix;
    }

    @NotNull
    public ConsumerConfiguration getConsumerConfiguration() {
        return consumerConfiguration;
    }

    @NotNull
    public ProducerConfiguration getProducerConfiguration() {
        return producerConfiguration;
    }

    @NotNull
    public String getContentUriPrefix() {
        return contentUriPrefix;
    }

    @NotNull
    public MethodeArticleMapperConfiguration getMethodeArticleMapperConfiguration() {
        return methodeArticleMapperConfiguration;
    }
}
