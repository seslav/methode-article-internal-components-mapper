package com.ft.methodearticleinternalcomponentsmapper.transformation.body;

import com.ft.bodyprocessing.DefaultTransactionIdBodyProcessingContext;
import com.ft.jerseyhttpwrapper.ResilientClient;
import com.ft.methodearticleinternalcomponentsmapper.exception.DocumentStoreApiUnavailableException;
import com.ft.methodearticleinternalcomponentsmapper.model.EomAssetType;
import com.ft.methodearticleinternalcomponentsmapper.transformation.MethodeLinksBodyProcessor;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.spi.MessageBodyWorkers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.MediaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.ft.methodetesting.xml.XmlMatcher.identicalXmlTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MethodeLinksBodyProcessorTest {

	@Mock
	private ResilientClient documentStoreApiClient;

	@Mock
	private InBoundHeaders headers;
	@Mock
	private MessageBodyWorkers workers;
	@Mock
	private WebResource.Builder builder;
    @Mock
    private ClientHandlerException clientHandlerException;

    private URI uri;
	private InputStream entity;

	@Before
	public void setup() throws Exception {
        uri = new URI("www.anyuri.com");
		entity = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
		WebResource webResource = mock(WebResource.class);
		when(documentStoreApiClient.resource(any(URI.class))).thenReturn(webResource);
		when(webResource.accept(any(MediaType[].class))).thenReturn(builder);
		when(builder.header(anyString(), anyObject())).thenReturn(builder);
		when(builder.get(ClientResponse.class)).thenReturn(clientResponseWithCode(404));
	}

	private MethodeLinksBodyProcessor bodyProcessor;
	
	private String uuid = UUID.randomUUID().toString();
	private static final String TRANSACTION_ID = "tid_test";

    @Test(expected = DocumentStoreApiUnavailableException.class)
    public void shouldThrowDocumentStoreApiNotAvailable(){
        bodyProcessor = new MethodeLinksBodyProcessor(documentStoreApiClient, uri);
        when(builder.get(ClientResponse.class)).thenThrow(clientHandlerException);
        when(clientHandlerException.getCause()).thenReturn(new IOException());

        String body = "<body><a href=\"http://www.ft.com/cms/s/" + uuid + ".html\" title=\"Some absurd text here\"> Link Text</a></body>";
        bodyProcessor.process(body, new DefaultTransactionIdBodyProcessingContext(TRANSACTION_ID));
    }

    @Test(expected = DocumentStoreApiUnavailableException.class)
    public void shouldThrowDocumentStoreApiNotAvailableFor5XX(){
        bodyProcessor = new MethodeLinksBodyProcessor(documentStoreApiClient, uri);
        when(builder.get(ClientResponse.class)).thenReturn(clientResponseWithCode(503));

        String body = "<body><a href=\"http://www.ft.com/cms/s/" + uuid + ".html\" title=\"Some absurd text here\"> Link Text</a></body>";
        bodyProcessor.process(body, new DefaultTransactionIdBodyProcessingContext(TRANSACTION_ID));
    }

	@Test
	public void shouldReplaceNodeWhenItsALinkThatWillBeInTheContentStoreWhenAvailableInContentStore(){
		bodyProcessor = new MethodeLinksBodyProcessor(documentStoreApiClient, uri);
		when(builder.get(ClientResponse.class)).thenReturn(clientResponseWithCode(200));

		String body = "<body><a href=\"http://www.ft.com/cms/s/" + uuid + ".html\" title=\"Some absurd text here\"> Link Text</a></body>";
		String processedBody = bodyProcessor.process(body, new DefaultTransactionIdBodyProcessingContext(TRANSACTION_ID));
		assertThat(processedBody, is(identicalXmlTo("<body><content id=\"" + uuid + "\" title=\"Some absurd text here\" type=\"" + MethodeLinksBodyProcessor.ARTICLE_TYPE + "\"> Link Text</content></body>")));
	}

	@Test
	public void shouldNotReplaceNodeWhenItsALinkThatWillNotBeInTheContentStore(){
		Map<String, EomAssetType> assetTypes = new HashMap<>();
		assetTypes.put(uuid, new EomAssetType.Builder().type("Slideshow").uuid(uuid).build());
		bodyProcessor = new MethodeLinksBodyProcessor(documentStoreApiClient, uri);
		
		String body = "<body><a href=\"http://www.ft.com/cms/s/" + uuid + ".html\" title=\"Some absurd text here\"> Link Text</a></body>";
		String processedBody = bodyProcessor.process(body, new DefaultTransactionIdBodyProcessingContext(TRANSACTION_ID));
        assertThat(processedBody, is(identicalXmlTo("<body><a href=\"http://www.ft.com/cms/s/" + uuid + ".html\" title=\"Some absurd text here\"> Link Text</a></body>")));
	}
	
	@Test
	public void shouldNotTransformAPDFLinkIntoAnInternalLink() {
		Map<String, EomAssetType> assetTypes = new HashMap<>();
		assetTypes.put("add666f2-cd78-11e4-a15a-00144feab7de", new EomAssetType.Builder().type("Pdf").uuid("add666f2-cd78-11e4-a15a-00144feab7de").build());
		bodyProcessor = new MethodeLinksBodyProcessor(documentStoreApiClient, uri);
		String body = "<body><a href=\"http://im.ft-static.com/content/images/add666f2-cd78-11e4-a15a-00144feab7de.pdf\" title=\"im.ft-static.com\">Budget 2015</a></body>";
		String processedBody = bodyProcessor.process(body, new DefaultTransactionIdBodyProcessingContext(TRANSACTION_ID));
		assertThat(processedBody, is(identicalXmlTo("<body><a href=\"http://im.ft-static.com/content/images/add666f2-cd78-11e4-a15a-00144feab7de.pdf\" title=\"im.ft-static.com\">Budget 2015</a></body>")));
	}
	
	@Test
	public void shouldStripIntlFromHrefValueWhenItsNotAValidInternalLink(){
		Map<String, EomAssetType> assetTypes = new HashMap<>();
		assetTypes.put(uuid, new EomAssetType.Builder().type("Slideshow").uuid(uuid).build());
		bodyProcessor = new MethodeLinksBodyProcessor(documentStoreApiClient, uri);
		
		String body = "<body><a href=\"http://www.ft.com/intl/cms/s/" + uuid + ".html\" title=\"Some absurd text here\"> Link Text</a></body>";
		String processedBody = bodyProcessor.process(body, new DefaultTransactionIdBodyProcessingContext(TRANSACTION_ID));
        assertThat(processedBody, is(identicalXmlTo("<body><a href=\"http://www.ft.com/cms/s/" + uuid + ".html\" title=\"Some absurd text here\"> Link Text</a></body>")));
	}
	
	@Test
	public void shouldStripParamFromHrefValueWhenItsNotAValidInternalLink(){
		Map<String, EomAssetType> assetTypes = new HashMap<>();
		assetTypes.put(uuid, new EomAssetType.Builder().type("Slideshow").uuid(uuid).build());
		bodyProcessor = new MethodeLinksBodyProcessor(documentStoreApiClient, uri);
		
		String body = "<body><a href=\"http://www.ft.com/cms/s/" + uuid + ".html?param=5\" title=\"Some absurd text here\"> Link Text</a></body>";
		String processedBody = bodyProcessor.process(body, new DefaultTransactionIdBodyProcessingContext(TRANSACTION_ID));
        assertThat(processedBody, is(identicalXmlTo("<body><a href=\"http://www.ft.com/cms/s/" + uuid + ".html\" title=\"Some absurd text here\"> Link Text</a></body>")));
	}
	
	@Test
	public void shouldRemoveNodeIfATagHasNoHrefAttributeForNonInternalLinks() {
		bodyProcessor = new MethodeLinksBodyProcessor(documentStoreApiClient, uri);
		
		String body = "<body><a title=\"Some absurd text here\">Link Text</a></body>";
		String processedBody = bodyProcessor.process(body, new DefaultTransactionIdBodyProcessingContext(TRANSACTION_ID));
        assertThat(processedBody, is(identicalXmlTo("<body>Link Text</body>")));
	}

	@Test
	public void shouldRemoveNodeIfATagHasNoHrefAttributeForNonInternalLinksWithPreservingAllItsChildren() {
		bodyProcessor = new MethodeLinksBodyProcessor(documentStoreApiClient, uri);

		String body = "<body><a title=\"Some absurd text here\"><strong>first child</strong> Second child</a></body>";
		String processedBody = bodyProcessor.process(body, new DefaultTransactionIdBodyProcessingContext(TRANSACTION_ID));
        assertThat(processedBody, is(identicalXmlTo("<body><strong>first child</strong> Second child</body>")));
	}
	
	@Test
	public void shouldRemoveNodeIfATagHasNoHrefAttributeForNonInternalLinksEvenIfNodeEmpty() {
		bodyProcessor = new MethodeLinksBodyProcessor(documentStoreApiClient, uri);
		
		String body = "<body><a title=\"Some absurd text here\"/></body>";
		String processedBody = bodyProcessor.process(body, new DefaultTransactionIdBodyProcessingContext(TRANSACTION_ID));
        assertThat(processedBody, is(identicalXmlTo("<body></body>")));
	}
	
	@Test
	public void shouldConvertNonArticlePathBasedInternalLinksToFullFledgedWebsiteLinks(){
		Map<String, EomAssetType> assetTypes = new HashMap<>();
		assetTypes.put(uuid, new EomAssetType.Builder().type("EOM::MediaGallery").uuid(uuid).build());
		bodyProcessor = new MethodeLinksBodyProcessor(documentStoreApiClient, uri);
		
		String body = "<body><a href=\"/FT Production/Slideshows/gallery.xml;uuid=" + uuid + "\" title=\"Some absurd text here\"> Link Text</a></body>";
		String processedBody = bodyProcessor.process(body, new DefaultTransactionIdBodyProcessingContext(TRANSACTION_ID));
        assertThat(processedBody, is(identicalXmlTo("<body><a href=\"http://www.ft.com/cms/s/" + uuid + ".html\" title=\"Some absurd text here\"> Link Text</a></body>")));
	}
    
    @Test
    public void thatWhitespaceOnlyLinksAreRemoved() {
        bodyProcessor = new MethodeLinksBodyProcessor(documentStoreApiClient, uri);
        
        String body = "<body>Foo <a href=\"http://www.ft.com/intl/cms/s/0/12345.html\" title=\"Test link containing only whitespace\">\n</a> bar</body>";
        
        String processedBody = bodyProcessor.process(body, new DefaultTransactionIdBodyProcessingContext(TRANSACTION_ID));
        assertThat(processedBody, is(identicalXmlTo("<body>Foo  bar</body>")));
    }
    
    @Test
    public void thatWhitespaceAndChildTagsOnlyLinksArePreserved() {
        bodyProcessor = new MethodeLinksBodyProcessor(documentStoreApiClient, uri);
        
        String body = "<body>Foo <a href=\"http://www.ft.com/intl/cms/s/0/12345.html\" title=\"Test link with tag content\"><img src=\"http://localhost/\"/>\n</a> bar</body>";
        
        String processedBody = bodyProcessor.process(body, new DefaultTransactionIdBodyProcessingContext(TRANSACTION_ID));
        assertThat(processedBody, is(identicalXmlTo(body)));
    }
    
    @Test
    public void thatEmptyLinksWithDataAttributesArePreserved() {
        bodyProcessor = new MethodeLinksBodyProcessor(documentStoreApiClient, uri);
        
        String body = "<body>Foo <a data-asset-type=\"slideshow\" data-embedded=\"true\" href=\"http://www.ft.com/intl/cms/s/0/12345.html\" title=\"Test link with tag content\"></a> bar</body>";
        
        String processedBody = bodyProcessor.process(body, new DefaultTransactionIdBodyProcessingContext(TRANSACTION_ID));
        assertThat(processedBody, is(identicalXmlTo(body)));
    }
    
    @Test
    public void thatEmptyLinksWithinPromoBoxesArePreserved() {
        bodyProcessor = new MethodeLinksBodyProcessor(documentStoreApiClient, uri);
        
        String body = "<body>Foo <promo-box><promo-link><p><a title=\"Test Promo Link\" href=\"http://www.ft.com/cms/s/0/0bdf4bb6-6676-11e4-8bf6-00144feabdc0.html\"/></p></promo-link></promo-box> bar</body>";
        
        String processedBody = bodyProcessor.process(body, new DefaultTransactionIdBodyProcessingContext(TRANSACTION_ID));
        assertThat(processedBody, is(identicalXmlTo(body)));
    }
    
	private ClientResponse clientResponseWithCode(int status) {
		return new ClientResponse(status, headers, entity, workers);
	}
}
