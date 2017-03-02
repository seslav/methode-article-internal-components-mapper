package com.ft.methodearticleinternalcomponentsmapper.transformation;

import com.google.common.base.Strings;

import com.ft.methodearticleinternalcomponentsmapper.exception.MethodeArticleHasNoInternalComponentsException;
import com.ft.methodearticleinternalcomponentsmapper.exception.MethodeArticleMarkedDeletedException;
import com.ft.methodearticleinternalcomponentsmapper.exception.MethodeArticleNotEligibleForPublishException;
import com.ft.methodearticleinternalcomponentsmapper.exception.TransformationException;
import com.ft.methodearticleinternalcomponentsmapper.model.Design;
import com.ft.methodearticleinternalcomponentsmapper.model.EomFile;
import com.ft.methodearticleinternalcomponentsmapper.model.Image;
import com.ft.methodearticleinternalcomponentsmapper.model.InternalComponents;
import com.ft.methodearticleinternalcomponentsmapper.model.TableOfContents;
import com.ft.methodearticleinternalcomponentsmapper.model.Topper;
import com.ft.methodearticleinternalcomponentsmapper.validation.MethodeArticleValidator;
import com.ft.methodearticleinternalcomponentsmapper.validation.PublishingStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class InternalComponentsMapper {

    private static Logger LOGGER = LoggerFactory.getLogger(InternalComponentsMapper.class);

    private MethodeArticleValidator methodeArticleValidator;

    public InternalComponentsMapper(MethodeArticleValidator methodeArticleValidator) {
        this.methodeArticleValidator = methodeArticleValidator;
    }

    public InternalComponents map(EomFile eomFile, String transactionId, Date lastModified, boolean preview) {

        PublishingStatus status = methodeArticleValidator.getPublishingStatus(eomFile, transactionId, preview);
        UUID uuid = UUID.fromString(eomFile.getUuid());
        switch (status) {
            case INELIGIBLE:
                throw new MethodeArticleNotEligibleForPublishException(uuid);
            case DELETED:
                throw new MethodeArticleMarkedDeletedException(uuid);
        }

        try {
            final XPath xpath = XPathFactory.newInstance().newXPath();
            final Document eomFileDocument = getEomFileDocument(eomFile);

            final Design design = extractDesign(xpath, eomFileDocument);
            final TableOfContents tableOfContents = extractTableOfContents(xpath, eomFileDocument);
            final List<Image> leadImages = extractImages(xpath, eomFileDocument, "/doc/lead/lead-image-set/lead-image-");
            final Topper topper = extractTopper(xpath, eomFileDocument, leadImages);

            if (design == null && tableOfContents == null && leadImages.isEmpty() && topper == null) {
                LOGGER.info("Article {} does not have any internal components.", uuid);
                throw new MethodeArticleHasNoInternalComponentsException(uuid);
            }

            return InternalComponents.builder()
                    .withUuid(uuid)
                    .withPublishReference(transactionId)
                    .withLastModified(lastModified)
                    .withDesign(design)
                    .withTableOfContents(tableOfContents)
                    .withTopper(topper)
                    .withLeadImages(leadImages)
                    .build();
        } catch (ParserConfigurationException | SAXException | XPathExpressionException | IOException e) {
            throw new TransformationException(e);
        }
    }

    private Design extractDesign(XPath xPath, Document eomFileDoc) throws XPathExpressionException {
        final String theme = Strings.nullToEmpty(xPath.evaluate("/doc/lead/lead-components/content-package/@design-theme", eomFileDoc)).trim();

        if (Strings.isNullOrEmpty(theme)) {
            return null;
        }

        return new Design(theme);
    }

    private TableOfContents extractTableOfContents(XPath xPath, Document eomFileDoc) throws XPathExpressionException {
        final String sequence = Strings.nullToEmpty(xPath.evaluate("/doc/lead/lead-components/content-package/@sequence", eomFileDoc)).trim();
        final String labelType = Strings.nullToEmpty(xPath.evaluate("/doc/lead/lead-components/content-package/@label", eomFileDoc)).trim();

        if (Strings.isNullOrEmpty(sequence) && Strings.isNullOrEmpty(labelType)) {
            return null;
        }

        return new TableOfContents(sequence, labelType);
    }

    private Topper extractTopper(XPath xpath, Document eomFileDoc, final List<Image> leadImages) throws XPathExpressionException {
        String topperBasePath = "/doc/lead/lead-components/topper";

        String layout = Strings.nullToEmpty(xpath.evaluate(topperBasePath + "/@layout", eomFileDoc)).trim();
        String theme = Strings.nullToEmpty(xpath.evaluate(topperBasePath + "/@theme", eomFileDoc)).trim();  //TODO deprecate

        //a topper is valid only if the theme attribute is present. Since layout is the new value for theme, we need to check both
        if (Strings.isNullOrEmpty(layout) && Strings.isNullOrEmpty(theme)) {
            return null;
        }

        String headline = Strings.nullToEmpty(xpath.evaluate(topperBasePath + "/topper-headline", eomFileDoc)).trim();
        String standfirst = Strings.nullToEmpty(xpath.evaluate(topperBasePath + "/topper-standfirst", eomFileDoc)).trim();

        String backgroundColour = Strings.nullToEmpty(xpath.evaluate(topperBasePath + "/@background-colour", eomFileDoc)).trim();
        String bgcolor = Strings.nullToEmpty(xpath.evaluate(topperBasePath + "/@bgcolor", eomFileDoc)).trim(); //TODO deprecate

        List<Image> topperImages = extractImages(xpath, eomFileDoc, topperBasePath + "/topper-images/topper-image-"); //TODO deprecate

        return new Topper(
                headline,
                standfirst,
                Strings.isNullOrEmpty(bgcolor) ? backgroundColour : bgcolor,
                layout,
                Strings.isNullOrEmpty(theme) ? layout : theme,
                topperImages.isEmpty() ? leadImages : topperImages);
    }

    private List<Image> extractImages(XPath xpath, Document doc, String basePath) throws XPathExpressionException {
        String[] labels = new String[]{"square", "standard", "wide"};
        List<Image> images = new ArrayList<>();

        for (String label : labels) {
            String id = getImageId(xpath, doc, basePath + label);
            if (Strings.isNullOrEmpty(id)) {
                continue;
            }
            images.add(new Image(id, label));
        }

        return images;
    }


    private String getImageId(XPath xpath, Document doc, String topperImgBasePath) throws XPathExpressionException {
        String topperImageId = null;
        String imageFileRef = Strings.nullToEmpty(xpath.evaluate(topperImgBasePath + "/@fileref", doc)).trim();
        if (imageFileRef.contains("uuid=")) {
            topperImageId = imageFileRef.substring(imageFileRef.lastIndexOf("uuid=") + "uuid=".length());
        }
        return topperImageId;
    }

    private Document getEomFileDocument(EomFile eomFile) throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(new ByteArrayInputStream(eomFile.getValue()));
    }
}
