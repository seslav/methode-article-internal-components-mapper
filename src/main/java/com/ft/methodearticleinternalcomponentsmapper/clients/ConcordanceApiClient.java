package com.ft.methodearticleinternalcomponentsmapper.clients;

import com.ft.methodearticleinternalcomponentsmapper.configuration.UppServiceConfiguration;
import com.ft.methodearticleinternalcomponentsmapper.exception.ConcordanceApiException;
import com.ft.methodearticleinternalcomponentsmapper.model.concordance.Concordances;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_OK;

public class ConcordanceApiClient extends UppServiceClient {

    private static final Logger LOG = LoggerFactory.getLogger(ConcordanceApiClient.class);
    private static final String TME_AUTHORITY = "http://api.ft.com/system/FT-TME";

    private final URI concordanceApiBaseUri;

    public ConcordanceApiClient(UppServiceConfiguration uppServiceConfiguration, Environment environment) {
        super(uppServiceConfiguration, environment);
        concordanceApiBaseUri = UriBuilder.fromPath(apiPath).scheme("http").host(apiHost).port(apiPort).build();
    }

    public ConcordanceApiClient(Client jerseyClient, String apiHost, int apiPort, String apiPath, String hostHeader) {
        super(jerseyClient, apiHost, apiPort, apiPath, hostHeader);
        concordanceApiBaseUri = UriBuilder.fromPath(apiPath).scheme("http").host(apiHost).port(apiPort).build();
    }

    public Concordances getConcordancesByIdentifierValues(List<String> identifierValues) {
        if (identifierValues.isEmpty()) {
            LOG.warn("TME identifier values were not provided");
            return null;
        }

        final URI concordanceApiUri;
        try {
            UriBuilder builder = UriBuilder.fromUri(concordanceApiBaseUri)
                    .queryParam("authority", URLEncoder.encode(TME_AUTHORITY, "UTF-8"));
            for (String identifier : identifierValues) {
                builder.queryParam("identifierValue", URLEncoder.encode(identifier, "UTF-8"));
            }
            concordanceApiUri = builder.build();
        } catch (UnsupportedEncodingException e) {
            throw new ConcordanceApiException(e.getMessage());
        }

        LOG.info("Call to Concordance API: {}", concordanceApiUri);
        final ClientResponse clientResponse = jerseyClient.resource(concordanceApiUri)
                .header("Host", hostHeader)
                .get(ClientResponse.class);

        return processResponse(clientResponse, resp -> {
            if (resp.getStatus() == SC_OK) {
                return resp.getEntity(Concordances.class);
            } else {
                throw new ConcordanceApiException("Impossible to interact with Concordance API: " + resp.getStatusInfo());
            }
        });
    }
}
