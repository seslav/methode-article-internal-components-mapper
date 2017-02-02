package com.ft.methodearticleinternalcomponentsmapper.resources;

import com.ft.api.util.transactionid.TransactionIdUtils;
import com.ft.methodearticleinternalcomponentsmapper.model.EomFile;
import com.ft.methodearticleinternalcomponentsmapper.transformation.InternalComponentsMapper;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests that both article preview and published article get transformed by the POST endpoint.
 *
 * Created by julia.fernee on 28/01/2016.
 */
public class MapResourceHappyPathsTest {

    private static final String TRANSACTION_ID = "tid_test";

    private HttpHeaders httpHeaders = mock(HttpHeaders.class);
    private InternalComponentsMapper internalComponentsMapper = mock(InternalComponentsMapper.class);
    private EomFile eomFile = mock(EomFile.class);
    private String uuid = UUID.randomUUID().toString();

    /* Class upder test. */
    private MapResource mapResource = new MapResource(internalComponentsMapper);

    @Before
    public void preconditions() throws Exception {
        when(eomFile.getUuid()).thenReturn(uuid);
        when(httpHeaders.getRequestHeader(TransactionIdUtils.TRANSACTION_ID_HEADER)).thenReturn(Arrays.asList(TRANSACTION_ID));
    }

    /**
     * Tests that an unpublished article preview request results in processing it as a PREVIEW article.
     */
    @Test
    public void previewProcessedOk() {
        mapResource.map(true, eomFile, httpHeaders);
        verify(internalComponentsMapper, times(1)).map(eq(eomFile), eq(TRANSACTION_ID), any(), eq(true));
    }

    /**
     * Tests that an published article request results in processing it as a PUBLISHED article.
     */
    @Test
    public void publicationProcessedOk() {
        mapResource.map(false, eomFile, httpHeaders);
        verify(internalComponentsMapper, times(1)).map(eq(eomFile), eq(TRANSACTION_ID), any(), eq(false));
    }

}
