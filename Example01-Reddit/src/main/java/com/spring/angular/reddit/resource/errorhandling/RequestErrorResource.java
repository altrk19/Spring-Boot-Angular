package com.spring.angular.reddit.resource.errorhandling;

public class RequestErrorResource {
    private RequestError requestError;

    public RequestErrorResource(RequestError requestError) {
        this.requestError = requestError;
    }

    public RequestError getRequestError() {
        return requestError;
    }

    public void setRequestError(RequestError requestError) {
        this.requestError = requestError;
    }
}