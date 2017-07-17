package com.ft.methodearticleinternalcomponentsmapper.util;


public interface ApiUriGenerator {

    /**
     * Converts the relativePath to the appropriate absolute path for the users current route and protocol.
     *
     * @param relativePath
     * @return an absolute URI.
     */
    String resolve(String relativePath);
}
