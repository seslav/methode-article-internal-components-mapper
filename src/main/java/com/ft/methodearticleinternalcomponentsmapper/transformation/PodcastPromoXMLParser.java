package com.ft.methodearticleinternalcomponentsmapper.transformation;

import com.ft.bodyprocessing.BodyProcessingContext;
import com.ft.bodyprocessing.xml.eventhandlers.BaseXMLParser;
import com.ft.bodyprocessing.xml.eventhandlers.UnexpectedElementStructureException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.StartElement;

public class PodcastPromoXMLParser extends BaseXMLParser<PodcastPromoData> {

    private static final String PODCAST_PROMO_TAG = "podcast-promo";
    private static final String PODCAST_PROMO_TITLE_TAG = "h2";
    private static final String PODCAST_PROMO_DESCRIPTION_TAG = "p";

    public PodcastPromoXMLParser() {
        super(PODCAST_PROMO_TAG);
    }

    @Override
    public boolean doesTriggerElementContainAllDataNeeded() {
        return false;
    }

    @Override
    public PodcastPromoData createDataBeanInstance() {
        return new PodcastPromoData();
    }

    @Override
    protected void populateBean(PodcastPromoData podcastPromoData, StartElement startElement, XMLEventReader xmlEventReader, BodyProcessingContext bodyProcessingContext) throws UnexpectedElementStructureException {
        QName elementName = startElement.getName();

        if (isElementNamed(elementName, PODCAST_PROMO_TITLE_TAG)) {
            podcastPromoData.setTitle(parseRawContent(PODCAST_PROMO_TITLE_TAG, xmlEventReader));
        } else if (isElementNamed(elementName, PODCAST_PROMO_DESCRIPTION_TAG)) {
            podcastPromoData.setDescription(parseRawContent(PODCAST_PROMO_DESCRIPTION_TAG, xmlEventReader));
        }
    }
}
