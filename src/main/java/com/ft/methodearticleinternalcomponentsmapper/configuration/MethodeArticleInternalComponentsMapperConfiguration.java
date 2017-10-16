package com.ft.methodearticleinternalcomponentsmapper.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ft.bodyprocessing.richcontent.VideoSiteConfiguration;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class MethodeArticleInternalComponentsMapperConfiguration extends Configuration {

    private final ConsumerConfiguration consumerConfiguration;
    private final ProducerConfiguration producerConfiguration;
    private final UppServiceConfiguration documentStoreApiConfiguration;
    private final UppServiceConfiguration concordanceApiConfiguration;
    private final UppServiceConfiguration methodeArticleMapperConfiguration;
    private final UppServiceConfiguration methodeContentPlaceholderMapperConfiguration;
    private final ValidationConfiguration validationConfiguration;
    private final List<VideoSiteConfiguration> videoSiteConfig;
    private final List<String> interactiveGraphicsWhiteList;
    private final String contentUriPrefix;
    private final Map<String, String> contentTypeTemplates;
    private final String apiHost;

    public MethodeArticleInternalComponentsMapperConfiguration(@JsonProperty("consumer") ConsumerConfiguration consumerConfiguration,
                                                               @JsonProperty("producer") ProducerConfiguration producerConfiguration,
                                                               @JsonProperty("documentStoreApi") UppServiceConfiguration documentStoreApiConfiguration,
                                                               @JsonProperty("concordanceApi") UppServiceConfiguration concordanceApiConfiguration,
                                                               @JsonProperty("videoSiteConfig") List<VideoSiteConfiguration> videoSiteConfig,
                                                               @JsonProperty("interactiveGraphicsWhiteList") List<String> interactiveGraphicsWhiteList,
                                                               @JsonProperty("methodeArticleMapper") UppServiceConfiguration methodeArticleMapperConfiguration,
                                                               @JsonProperty("methodeContentPlaceholderMapper") UppServiceConfiguration methodeContentPlaceholderMapperConfiguration,
                                                               @JsonProperty("validationConfiguration") ValidationConfiguration validationConfiguration,
                                                               @JsonProperty("contentUriPrefix") String contentUriPrefix,
                                                               @JsonProperty("contentTypeTemplates") Map<String, String> contentTypeTemplates,
                                                               @JsonProperty("apiHost") String apiHost) {
        this.consumerConfiguration = consumerConfiguration;
        this.producerConfiguration = producerConfiguration;
        this.documentStoreApiConfiguration = documentStoreApiConfiguration;
        this.concordanceApiConfiguration = concordanceApiConfiguration;
        this.videoSiteConfig = videoSiteConfig;
        this.interactiveGraphicsWhiteList = interactiveGraphicsWhiteList;
        this.methodeArticleMapperConfiguration = methodeArticleMapperConfiguration;
        this.methodeContentPlaceholderMapperConfiguration = methodeContentPlaceholderMapperConfiguration;
        this.validationConfiguration = validationConfiguration;
        this.contentUriPrefix = contentUriPrefix;
        this.contentTypeTemplates = contentTypeTemplates;
        this.apiHost = apiHost;
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
    public UppServiceConfiguration getConcordanceApiConfiguration() {
        return concordanceApiConfiguration;
    }

    @NotNull
    public UppServiceConfiguration getDocumentStoreApiConfiguration() {
        return documentStoreApiConfiguration;
    }

    @NotNull
    public List<VideoSiteConfiguration> getVideoSiteConfig() {
        return videoSiteConfig;
    }

    @NotNull
    public List<String> getInteractiveGraphicsWhitelist() {
        return interactiveGraphicsWhiteList;
    }

    @NotNull
    public String getContentUriPrefix() {
        return contentUriPrefix;
    }

    @NotNull
    public UppServiceConfiguration getMethodeArticleMapperConfiguration() {
        return methodeArticleMapperConfiguration;
    }

    @NotNull
    public UppServiceConfiguration getMethodeContentPlaceholderMapperConfiguration() {
        return methodeContentPlaceholderMapperConfiguration;
    }

    @NotNull
    public ValidationConfiguration getValidationConfiguration() {
        return validationConfiguration;
    }

    @NotNull
    public Map<String, String> getContentTypeTemplates() {
        return contentTypeTemplates;
    }

    @NotNull
    public String getApiHost() {
        return apiHost;
    }
}
