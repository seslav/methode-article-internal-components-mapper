package com.ft.methodearticleinternalcomponentsmapper.validation;

import com.ft.api.util.transactionid.TransactionIdUtils;
import com.ft.methodearticleinternalcomponentsmapper.exception.MethodeContentPlaceholderMapperUnavailableException;
import com.ft.methodearticleinternalcomponentsmapper.model.EomFile;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import org.apache.http.HttpStatus;

import javax.ws.rs.core.MediaType;
import java.net.URI;

public class MethodeContentPlaceholderValidator {
    private Client mcpmClient;
    private URI mcpmUri;
    private String mcpmHost;

    public MethodeContentPlaceholderValidator(Client mcpmClient, URI mcpmUri, String mcpmHost) {
        this.mcpmClient = mcpmClient;
        this.mcpmUri = mcpmUri;
        this.mcpmHost = mcpmHost;
    }

    public PublishingStatus getPublishingStatus(EomFile eomFile, String transactionId) {
        int responseStatusCode;
        ClientResponse clientResponse = null;
        try {
            clientResponse = mcpmClient.resource(mcpmUri)
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .header(TransactionIdUtils.TRANSACTION_ID_HEADER, transactionId)
                    .header("Host", mcpmHost)
                    .entity(eomFile)
                    .post(ClientResponse.class);

            responseStatusCode = clientResponse.getStatus();
        } finally {
            if (clientResponse != null) {
                clientResponse.close();
            }
        }

        switch (responseStatusCode) {
            case HttpStatus.SC_UNPROCESSABLE_ENTITY:
                return PublishingStatus.INELIGIBLE;
            case HttpStatus.SC_NOT_FOUND:
                return PublishingStatus.DELETED;
            case HttpStatus.SC_OK:
                return PublishingStatus.VALID;
            default:
                throw new MethodeContentPlaceholderMapperUnavailableException(responseStatusCode);
        }
    }
}
