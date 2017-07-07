package com.ft.methodearticleinternalcomponentsmapper.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ft.bodyprocessing.richcontent.VideoSiteConfiguration;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;
import java.util.List;

public class MethodeArticleInternalComponentsMapperConfiguration extends Configuration {

  private final ConsumerConfiguration consumerConfiguration;
  private final ProducerConfiguration producerConfiguration;
  private final DocumentStoreApiConfiguration documentStoreApiConfiguration;
  private final ConcordanceApiConfiguration concordanceApiConfiguration;
  private final MethodeArticleMapperConfiguration methodeArticleMapperConfiguration;
  private final List<VideoSiteConfiguration> videoSiteConfig;
  private final List<String> interactiveGraphicsWhiteList;
  private final String contentUriPrefix;

  public MethodeArticleInternalComponentsMapperConfiguration(@JsonProperty("consumer") ConsumerConfiguration consumerConfiguration,
                                                             @JsonProperty("producer") ProducerConfiguration producerConfiguration,
                                                             @JsonProperty("documentStoreApi") DocumentStoreApiConfiguration documentStoreApiConfiguration,
                                                             @JsonProperty("concordanceApi") ConcordanceApiConfiguration concordanceApiConfiguration,
                                                             @JsonProperty("videoSiteConfig") List<VideoSiteConfiguration> videoSiteConfig,
                                                             @JsonProperty("interactiveGraphicsWhiteList") List<String> interactiveGraphicsWhiteList,
                                                             @JsonProperty("methodeArticleMapper") MethodeArticleMapperConfiguration methodeArticleMapperConfiguration,
                                                             @JsonProperty("contentUriPrefix") String contentUriPrefix) {
    this.consumerConfiguration = consumerConfiguration;
    this.producerConfiguration = producerConfiguration;
    this.documentStoreApiConfiguration = documentStoreApiConfiguration;
    this.concordanceApiConfiguration = concordanceApiConfiguration;
    this.videoSiteConfig = videoSiteConfig;
    this.interactiveGraphicsWhiteList = interactiveGraphicsWhiteList;
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
  public ConcordanceApiConfiguration getConcordanceApiConfiguration() {
    return concordanceApiConfiguration;
  }

  @NotNull
  public DocumentStoreApiConfiguration getDocumentStoreApiConfiguration() {
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
  public MethodeArticleMapperConfiguration getMethodeArticleMapperConfiguration() {
    return methodeArticleMapperConfiguration;
  }
}
