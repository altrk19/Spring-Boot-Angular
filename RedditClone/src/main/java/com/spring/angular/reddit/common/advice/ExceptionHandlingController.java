package com.spring.angular.reddit.common.advice;

import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.exception.ClientException;
import com.spring.angular.reddit.exception.ServerException;
import com.spring.angular.reddit.resource.errorhandling.PolicyException;
import com.spring.angular.reddit.resource.errorhandling.RequestError;
import com.spring.angular.reddit.resource.errorhandling.RequestErrorResource;
import com.spring.angular.reddit.resource.errorhandling.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<RequestErrorResource> serverException(final ServerException ex) {
        RequestErrorTypes requestErrorTypes = ex.getRequestErrorTypes();
        HttpStatus statusCode = ex.getStatusCode();

        if (Objects.isNull(requestErrorTypes)) {
            requestErrorTypes = RequestErrorTypes.GENERIC_SERVICE_ERROR;
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        final ServiceException serviceException = new ServiceException(requestErrorTypes, ex.getVariables());
        final RequestError requestError = new RequestError();
        requestError.setServiceException(serviceException);
        RequestErrorResource requestErrorResource = new RequestErrorResource(requestError);

        return ResponseEntity.status(statusCode).body(requestErrorResource);
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<RequestErrorResource> clientException(final ClientException ex) {
        RequestErrorTypes requestErrorTypes = ex.getRequestErrorTypes();
        HttpStatus statusCode = ex.getStatusCode();

        if (Objects.isNull(requestErrorTypes)) {
            requestErrorTypes = RequestErrorTypes.GENERIC_POLICY_ERROR;
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        final PolicyException errorResponse = new PolicyException(requestErrorTypes, ex.getVariables());
        final RequestError requestError = new RequestError();
        requestError.setPolicyException(errorResponse);
        RequestErrorResource requestErrorResource = new RequestErrorResource(requestError);

        return ResponseEntity.status(statusCode).body(requestErrorResource);
    }

}
