package com.spring.angular.reddit.resource.errorhandling;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.angular.reddit.resource.DtoBase;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestError extends DtoBase {

    private static final long serialVersionUID = -536375211929585493L;
    private ServiceException serviceException;

    private PolicyException policyException;

    public ServiceException getServiceException() {
        return serviceException;
    }

    public void setServiceException(ServiceException serviceException) {
        this.serviceException = serviceException;
    }

    public PolicyException getPolicyException() {
        return policyException;
    }

    public void setPolicyException(PolicyException policyException) {
        this.policyException = policyException;
    }
}