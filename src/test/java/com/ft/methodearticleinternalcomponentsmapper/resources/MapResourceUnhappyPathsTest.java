package com.ft.methodearticleinternalcomponentsmapper.resources;

import com.ft.api.jaxrs.errors.WebApplicationClientException;
import com.ft.api.util.transactionid.TransactionIdUtils;
import com.ft.methodearticleinternalcomponentsmapper.exception.MethodeArticleMarkedDeletedException;
import com.ft.methodearticleinternalcomponentsmapper.exception.MethodeArticleNotEligibleForPublishException;
import com.ft.methodearticleinternalcomponentsmapper.model.EomFile;
import com.ft.methodearticleinternalcomponentsmapper.transformation.InternalComponentsMapper;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests that application exceptions that occur during transformation of a published article
 * translate into correct HTTP codes by the POST endpoint.
 *
 * Created by julia.fernee on 29/01/2016.
 */
public class MapResourceUnhappyPathsTest {

    private static final String TRANSACTION_ID = "tid_test";

    private InternalComponentsMapper internalComponentsMapper = mock(InternalComponentsMapper.class);
    private HttpHeaders httpHeaders = mock(HttpHeaders.class);
    private EomFile eomFile = mock(EomFile.class);
    private UUID uuid = UUID.randomUUID();

    /*Class under test*/
    private MapResource mapResource = new MapResource(internalComponentsMapper);

    @Before
    public void preconditions() {
        when(httpHeaders.getRequestHeader(TransactionIdUtils.TRANSACTION_ID_HEADER)).thenReturn(Arrays.asList(TRANSACTION_ID));
        when(eomFile.getUuid()).thenReturn(uuid.toString());
    }

    /**
     * Tests that response contains 404 error code and the correct message
     * when content marked as deleted in Methode is attempted to be published.
     */
    @Test
    public void shouldThrow404ExceptionWhenContentIsMarkedAsDeletedInMethode() {

        when(internalComponentsMapper.map(eq(eomFile), eq(TRANSACTION_ID), any(), anyBoolean()))
                .thenThrow(new MethodeArticleMarkedDeletedException(uuid));
        try {
            mapResource.map(false, eomFile, httpHeaders);
            fail("No exception was thrown, but expected one.");
        } catch (WebApplicationException wace) {
            assertThat(wace.getResponse().getStatus(), equalTo(HttpStatus.SC_NOT_FOUND));
        }
    }

    /**
     * Tests that the response contains http code 422 and the correct message
     * when the type property in the json payload is not EOM::CompoundStory.
     */
    @Test
    public void shouldThrow422ExceptionWhenPublicationNotEligibleForPublishing() {

        when(internalComponentsMapper.map(eq(eomFile), eq(TRANSACTION_ID), any(), anyBoolean())).
                thenThrow(new MethodeArticleNotEligibleForPublishException(uuid));
        try {
            mapResource.map(false, eomFile, httpHeaders);
            fail("No exception was thrown, but expected one.");
        } catch (WebApplicationException wace) {
            assertThat(wace.getResponse().getStatus(), equalTo(HttpStatus.SC_UNPROCESSABLE_ENTITY));
        }
    }
}
