package com.ft.methodearticleinternalcomponentsmapper.transformation;


import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.writer.BodyWriter;
import com.ft.bodyprocessing.xml.eventhandlers.BaseXMLEventHandler;
import com.ft.uuidutils.UUIDValidation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import java.util.HashMap;

public class PodcastPromoXmlEventHandler extends BaseXMLEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PodcastPromoXmlEventHandler.class);

    private static final String PODCAST_PROMO_TAG = "podcast-promo";
    private static final String PODCAST_PROMO_INPUT_UUID_ATTRIBUTE = "episode-uuid";
    private static final String PODCAST_PROMO_OUTPUT_UUID_ATTRIBUTE = "id";
    private static final String PODCAST_PROMO_TITLE_TAG = "h2";
    private static final String PODCAST_PROMO_DESCRIPTION_TAG = "p";

    private PodcastPromoXMLParser podcastPromoXMLParser;

    public PodcastPromoXmlEventHandler(PodcastPromoXMLParser podcastPromoXMLParser) {
        this.podcastPromoXMLParser = podcastPromoXMLParser;
    }

    @Override
    public void handleStartElementEvent(StartElement event, XMLEventReader xmlEventReader, BodyWriter eventWriter,
                                        BodyProcessingContext bodyProcessingContext) throws XMLStreamException {
        try {
            String uuid = getUUID(event);
            UUIDValidation.of(uuid);

            PodcastPromoData podcastPromoData = podcastPromoXMLParser.parseElementData(event, xmlEventReader, bodyProcessingContext);

            HashMap<String, String> attributes = new HashMap<>();
            attributes.put(PODCAST_PROMO_OUTPUT_UUID_ATTRIBUTE, uuid);

            eventWriter.writeStartTag(PODCAST_PROMO_TAG, attributes);
            writeTitle(podcastPromoData, eventWriter);
            writeDescription(podcastPromoData, eventWriter);
            eventWriter.writeEndTag(PODCAST_PROMO_TAG);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Skipping transformation of {}. Mandatory " + PODCAST_PROMO_INPUT_UUID_ATTRIBUTE + " attribute has an invalid uuid.", event.getName().getLocalPart());
            skipUntilMatchingEndTag(event.getName().getLocalPart(), xmlEventReader);
        }
    }

    private String getUUID(StartElement event) {
        Attribute uuidAttribute = event.getAttributeByName(new QName(PODCAST_PROMO_INPUT_UUID_ATTRIBUTE));
        if (uuidAttribute == null) {
            LOGGER.warn("Skipping transformation of {}. Mandatory " + PODCAST_PROMO_INPUT_UUID_ATTRIBUTE + " attribute was missing.", event.getName().getLocalPart());
            return null;
        }

        return uuidAttribute.getValue();
    }

    private void writeTitle(PodcastPromoData podcastPromoData, BodyWriter eventWriter) {
        if (StringUtils.isNotEmpty(podcastPromoData.getTitle())) {
            eventWriter.writeStartTag(PODCAST_PROMO_TITLE_TAG, new HashMap<>());
            eventWriter.writeRaw(podcastPromoData.getTitle());
            eventWriter.writeEndTag(PODCAST_PROMO_TITLE_TAG);
        }
    }

    private void writeDescription(PodcastPromoData podcastPromoData, BodyWriter eventWriter) {
        if (StringUtils.isNotEmpty(podcastPromoData.getDescription())) {
            eventWriter.writeStartTag(PODCAST_PROMO_DESCRIPTION_TAG, new HashMap<>());
            eventWriter.writeRaw(podcastPromoData.getDescription());
            eventWriter.writeEndTag(PODCAST_PROMO_DESCRIPTION_TAG);
        }
    }
}
