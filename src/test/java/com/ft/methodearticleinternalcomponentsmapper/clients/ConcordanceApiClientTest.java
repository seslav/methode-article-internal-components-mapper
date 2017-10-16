package com.ft.methodearticleinternalcomponentsmapper.clients;

import com.ft.methodearticleinternalcomponentsmapper.exception.ConcordanceApiException;
import com.ft.methodearticleinternalcomponentsmapper.model.concordance.ConceptView;
import com.ft.methodearticleinternalcomponentsmapper.model.concordance.Concordance;
import com.ft.methodearticleinternalcomponentsmapper.model.concordance.Concordances;
import com.ft.methodearticleinternalcomponentsmapper.model.concordance.Identifier;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import java.net.URI;
import java.util.Collections;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConcordanceApiClientTest {

    @Mock
    private Client jerseyClient;
    @Mock
    private WebResource webResource;
    @Mock
    private WebResource.Builder webResourceBuilder;
    @Mock
    private ClientResponse clientResponse;

    private ConcordanceApiClient concordanceApiClient;

    @Before
    public void setUp() throws Exception {
        String hostHeader = "public-concordances-api";
        concordanceApiClient = new ConcordanceApiClient(jerseyClient, "localhost", 8080,
                "concordances", hostHeader);

        when(jerseyClient.resource(any(URI.class))).thenReturn(webResource);
        when(webResource.header("Host", hostHeader)).thenReturn(webResourceBuilder);
        when(webResourceBuilder.get(ClientResponse.class)).thenReturn(clientResponse);
        when(clientResponse.getStatus()).thenReturn(200);
    }

    @Test
    public void testGetConcordancesByIdentifierValuesReturnsNullIfIdentifierValuesIsEmpty() throws Exception {
        Concordances concordances = concordanceApiClient.getConcordancesByIdentifierValues(Collections.emptyList());
        assertThat(concordances).isNull();
    }

    @Test(expected = ConcordanceApiException.class)
    public void testGetConcordancesByIdentifierValuesThrowsExceptionIfConcordanceApiReturns3xx() throws Exception {
        when(clientResponse.getStatus()).thenReturn(300);
        concordanceApiClient.getConcordancesByIdentifierValues(Collections.singletonList("identifierValue"));
    }

    @Test(expected = ConcordanceApiException.class)
    public void testGetConcordancesByIdentifierValuesThrowsExceptionIfConcordanceApiReturns4xx() throws Exception {
        when(clientResponse.getStatus()).thenReturn(400);
        concordanceApiClient.getConcordancesByIdentifierValues(Collections.singletonList("identifierValue"));
    }

    @Test(expected = ConcordanceApiException.class)
    public void testGetConcordancesByIdentifierValuesThrowsExceptionIfConcordanceApiReturns5xx() throws Exception {
        when(clientResponse.getStatus()).thenReturn(500);
        concordanceApiClient.getConcordancesByIdentifierValues(Collections.singletonList("identifierValue"));
    }

    @Test
    public void testGetConcordancesByIdentifierValuesReturnsValidConcordances() {
        Identifier identifier = new Identifier("authority", "identifierValue");
        ConceptView conceptView =  new ConceptView("id", "apiUrl");
        Concordances concordances = new Concordances(Collections.singletonList(new Concordance(conceptView, identifier)));
        when(clientResponse.getEntity(Concordances.class)).thenReturn(concordances);

        Concordances actualConcordances = concordanceApiClient.getConcordancesByIdentifierValues(Collections.singletonList("identifierValue"));

        assertThat(actualConcordances).isNotNull();
        assertThat(actualConcordances.getConcordances().size()).isEqualTo(1);
        assertThat(actualConcordances.getConcordances().get(0).getIdentifier()).isEqualTo(identifier);
        assertThat(actualConcordances.getConcordances().get(0).getConcept()).isEqualTo(conceptView);
    }
}
