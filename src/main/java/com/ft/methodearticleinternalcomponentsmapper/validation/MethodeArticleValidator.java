package com.ft.methodearticleinternalcomponentsmapper.validation;

import com.ft.api.util.transactionid.TransactionIdUtils;
import com.ft.methodearticleinternalcomponentsmapper.exception.MethodeArticleMapperUnavailableException;
import com.ft.methodearticleinternalcomponentsmapper.model.EomFile;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import org.apache.http.HttpStatus;

import java.net.URI;

import javax.ws.rs.core.MediaType;

public class MethodeArticleValidator {
    private Client mamClient;
    private URI mamUri;
    private String mamHost;

    public MethodeArticleValidator(Client mamClient, URI mamUri, String mamHost) {
        this.mamClient = mamClient;
        this.mamUri = mamUri;
        this.mamHost = mamHost;
    }

    public PublishingStatus getPublishingStatus(EomFile eomFile, String transactionId, boolean preview) {
        int responseStatusCode;
        ClientResponse clientResponse = null;
        try {
            clientResponse = mamClient.resource(mamUri)
                    .queryParam("preview", Boolean.toString(preview))
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .header(TransactionIdUtils.TRANSACTION_ID_HEADER, transactionId)
                    .header("Host", mamHost)
                    .entity(eomFile)
                    .post(ClientResponse.class);

            responseStatusCode = clientResponse.getStatus();
        }
        finally {
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
                throw new MethodeArticleMapperUnavailableException(responseStatusCode);
        }
    }
}
