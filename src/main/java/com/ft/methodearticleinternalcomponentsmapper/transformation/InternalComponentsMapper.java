package com.ft.methodearticleinternalcomponentsmapper.transformation;

import com.google.common.base.Strings;

import com.ft.methodearticleinternalcomponentsmapper.exception.MethodeArticleMarkedDeletedException;
import com.ft.methodearticleinternalcomponentsmapper.exception.MethodeArticleNotEligibleForPublishException;
import com.ft.methodearticleinternalcomponentsmapper.exception.TransformationException;
import com.ft.methodearticleinternalcomponentsmapper.model.EomFile;
import com.ft.methodearticleinternalcomponentsmapper.model.Image;
import com.ft.methodearticleinternalcomponentsmapper.model.InternalComponents;
import com.ft.methodearticleinternalcomponentsmapper.model.Topper;
import com.ft.methodearticleinternalcomponentsmapper.validation.MethodeArticleValidator;
import com.ft.methodearticleinternalcomponentsmapper.validation.PublishingStatus;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class InternalComponentsMapper {

    private MethodeArticleValidator methodeArticleValidator;

    private static final String BODY_TAG_XPATH = "/doc/story/text/body";
    private final XPath xpath = XPathFactory.newInstance().newXPath();

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
            final DocumentBuilder documentBuilder = getDocumentBuilder();
            Document attributesDocument = documentBuilder.parse(new InputSource(new StringReader(eomFile.getAttributes())));
            Document eomFileDocument = documentBuilder.parse(new ByteArrayInputStream(eomFile.getValue()));
            String rawBody = retrieveField(xpath, BODY_TAG_XPATH, eomFileDocument);

            ParsedEomFile parsedEomFile = new ParsedEomFile(uuid, eomFileDocument, rawBody,
                    attributesDocument, eomFile.getWebUrl());

            return transformEomFileToInternalContent(parsedEomFile, transactionId, lastModified);
        } catch (ParserConfigurationException | SAXException | XPathExpressionException | TransformerException | IOException e) {
            throw new TransformationException(e);
        }
    }

    private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        return documentBuilderFactory.newDocumentBuilder();
    }

    private InternalComponents transformEomFileToInternalContent(ParsedEomFile eomFile, String transactionId, Date lastModified)
            throws SAXException, IOException, XPathExpressionException, TransformerException, ParserConfigurationException {

        final XPath xpath = XPathFactory.newInstance().newXPath();
        final Document doc = eomFile.getValue();

        String topperBasePath = "/doc/lead/lead-components/topper/";
        String topperTheme = Strings.nullToEmpty(xpath.evaluate(topperBasePath + "@theme", doc)).trim();
        String topperBGColor = Strings.nullToEmpty(xpath.evaluate(topperBasePath + "@bgcolor", doc)).trim();
        String topperHeadline = Strings.nullToEmpty(xpath.evaluate(topperBasePath + "topper-headline", doc)).trim();
        String topperStandfirst = Strings.nullToEmpty(xpath.evaluate(topperBasePath + "topper-standfirst", doc)).trim();

        Topper topper = new Topper(topperTheme, topperBGColor, buildImages(xpath, doc, topperBasePath), topperHeadline, topperStandfirst);

        return InternalComponents.builder()
                .withUuid(eomFile.getUUID())
                .withPublishReference(transactionId)
                .withLastModified(lastModified)
                .withTopper(topper)
                .build();
    }

    private List<Image> buildImages(XPath xpath, Document doc, String topperBasePath) throws XPathExpressionException {
        String[] labels = new String[]{"square", "standard", "wide"};
        List<Image> images = new ArrayList<>();

        for (String l : labels) {
            String imgBasePath = topperBasePath + "topper-images/topper-image-" + l;
            String id = getTopperImageId(xpath, doc, imgBasePath);
            if (Strings.isNullOrEmpty(id)) {
                continue;
            }
            images.add(new Image(id, l));
        }

        return images;
    }

    private String getTopperImageId(XPath xpath, Document doc, String topperImgBasePath) throws XPathExpressionException {
        String topperImageId = null;
        String imageFileRef = Strings.nullToEmpty(xpath.evaluate(topperImgBasePath + "/@fileref", doc)).trim();
        if (imageFileRef.contains("uuid=")) {
            topperImageId = imageFileRef.substring(imageFileRef.lastIndexOf("uuid=") + "uuid=".length());
        }
        return topperImageId;
    }

    private String retrieveField(XPath xpath, String expression, Document eomFileDocument) throws TransformerException, XPathExpressionException {
        final Node node = (Node) xpath.evaluate(expression, eomFileDocument, XPathConstants.NODE);
        return getNodeAsString(node);
    }

    private String getNodeAsString(Node node) throws TransformerException {
        StringWriter writer = new StringWriter();
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(node), new StreamResult(writer));
        return writer.toString();
    }

    private static class ParsedEomFile {
        private final UUID uuid;
        private final Document attributesDocument;
        private final Document value;
        private final String body;
        private final URI webUrl;

        ParsedEomFile(UUID uuid, Document value, String body, Document attributesDocument, URI webUrl) {
            this.uuid = uuid;
            this.value = value;
            this.body = body;
            this.attributesDocument = attributesDocument;
            this.webUrl = webUrl;
        }

        public UUID getUUID() {
            return uuid;
        }

        public Document getValue() {
            return value;
        }

        public String getBody() {
            return body;
        }

        public Document getAttributes() {
            return attributesDocument;
        }

        public URI getWebUrl() {
            return webUrl;
        }
    }
}
