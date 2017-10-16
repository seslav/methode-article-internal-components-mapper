package com.ft.methodearticleinternalcomponentsmapper.transformation;

import com.codahale.metrics.MetricRegistry;
import com.ft.methodearticleinternalcomponentsmapper.clients.DocumentStoreApiClient;
import com.ft.methodearticleinternalcomponentsmapper.exception.TransientUuidResolverException;
import com.ft.methodearticleinternalcomponentsmapper.exception.UuidResolverException;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BlogUuidResolverTest {

    private static final String TRANSACTION_ID = "tid_ab!430d8ef";

    private BlogUuidResolver resolver;

    @Mock
    private DocumentStoreApiClient client;

    @Before
    public void before() {
        Map<String, String> mappings = Maps.newHashMap();
        mappings.put("blogs.ft.com/westminster", "FT-LABS-WP-1-12");
        mappings.put("ftalphaville.ft.com", "FT-LABS-WP-1-24");
        mappings.put("www.ft.com/fastft", "FT-LABS-WP-1-335");

        mappings.put("example.com/blah", "example-mapped");

        when(client.resolveUUID("auth-prefix://FT-LABS-WP-1-12", "http://blogs.ft.com/westminster/?p=427471", TRANSACTION_ID)).thenReturn("uuid");
        when(client.resolveUUID("auth-prefix://FT-LABS-WP-1-335", "http://www.ft.com/fastft/?p=427471", TRANSACTION_ID)).thenReturn("uuid");
        when(client.resolveUUID("auth-prefix://FT-LABS-WP-1-24", "http://ftalphaville.ft.com/?p=427471", TRANSACTION_ID)).thenReturn("uuid");

        when(client.resolveUUID("auth-prefix://example-mapped", "http://example.com/blah/?p=40140", TRANSACTION_ID)).thenReturn("uuid");

        resolver = new BlogUuidResolver(new MetricRegistry(), client, "auth-prefix://", mappings);
    }

    @Test
    public void shouldResolveRealisticUuid() {
        String uuid = resolver.resolveUuid("http://www.ft.com/fastft?post=427471", "427471", TRANSACTION_ID);
        assertEquals("uuid", uuid);
    }

    @Test
    public void shouldResolveRealisticUuid2() {
        String uuid = resolver.resolveUuid("http://ftalphaville.ft.com?post=427471", "427471", TRANSACTION_ID);
        assertEquals("uuid", uuid);
    }

    @Test
    public void shouldResolveRealisticUuid3() {
        String uuid = resolver.resolveUuid("http://blogs.ft.com/westminster/?post=427471", "427471", TRANSACTION_ID);
        assertEquals("uuid", uuid);
    }

    @Test
    public void shouldResolveUuid() {
        String uuid = resolver.resolveUuid("http://example.com:8080/blah?p=40140&web_blog=some-random-string", "40140", TRANSACTION_ID);
        assertEquals("uuid", uuid);
    }

    @Test
    public void shouldResolveUuidDespiteTestPrefix() {
        String uuid = resolver.resolveUuid("http://test.example.com:8080/blah?p=40140&web_blog=some-random-string", "40140", TRANSACTION_ID);
        assertEquals("uuid", uuid);
    }

    @Test(expected = UuidResolverException.class)
    public void shouldNotFindAMapping() {
        resolver.resolveUuid("http://example.com:8080/the-world/?p=40140&web_blog=some-random-string", "40140", TRANSACTION_ID);
    }

    @Test (expected = TransientUuidResolverException.class)
    public void shouldThrowTransientUuidResolverExceptionIfDocStoreCallRetrunsNotFoud () {
    	
    	when(client.resolveUUID("auth-prefix://FT-LABS-WP-1-335", "http://www.ft.com/fastft/?p=427471", TRANSACTION_ID)).thenThrow(new TransientUuidResolverException("errorMsg", URI.create("http://www.ft.com/fastft/?p=427471"), "identifierValue"));
    	resolver.resolveUuid("http://www.ft.com/fastft?post=427471", "427471", TRANSACTION_ID);
    	
    	try{
    	resolver.resolveUuid("http://www.ft.com/fastft?post=427471", "427471", TRANSACTION_ID);
    	}catch (TransientUuidResolverException ture){   		
    		assertEquals("identifierValue", ture.getIdentifierValue());    
    		assertEquals(URI.create("http://www.ft.com/fastft/?p=427471"), ture.getUri());
    		throw ture;
    	}    	
    	fail("expected TransientUuidResolverException not thrown");
    }
}


