package com.spring.angular.reddit.exception;

import com.spring.angular.reddit.constants.RequestErrorTypes;
import org.springframework.http.HttpStatus;

public class ServerException extends Exception {

    private final RequestErrorTypes requestErrorTypes;
    private final String[] variables;
    private final HttpStatus statusCode;

    public ServerException(RequestErrorTypes requestErrorTypes, String[] variables, HttpStatus statusCode) {
        this.requestErrorTypes = requestErrorTypes;
        this.variables = variables;
        this.statusCode = statusCode;
    }

    public RequestErrorTypes getRequestErrorTypes() {
        return requestErrorTypes;
    }

    public String[] getVariables() {
        return variables;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
