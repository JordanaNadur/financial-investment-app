package com.financial.apigateway.domain.model;
public class Route {
    private final String id;
    private final String path;
    private final String serviceUri;
    private final String method;
    private final boolean authRequired;

    public Route(String id, String path, String serviceUri, String method, boolean authRequired) {
        this.id = id;
        this.path = path;
        this.serviceUri = serviceUri;
        this.method = method;
        this.authRequired = authRequired;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getServiceUri() {
        return serviceUri;
    }

    public String getMethod() {
        return method;
    }

    public boolean isAuthRequired() {
        return authRequired;
    }
}
