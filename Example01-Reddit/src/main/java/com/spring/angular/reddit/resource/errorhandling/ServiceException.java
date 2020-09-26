package com.spring.angular.reddit.resource.errorhandling;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.angular.reddit.constants.RequestErrorTypes;
import com.spring.angular.reddit.resource.DtoBase;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceException extends DtoBase {
    private static final long serialVersionUID = -2161386652868686884L;

    private String messageId;
    private String text;
    private String[] variables;

    public ServiceException() {
    }

    public ServiceException(final RequestErrorTypes requestErrorType, final String[] variables) {
        this.messageId = requestErrorType.getMessageId();
        this.text = requestErrorType.getText();
        this.variables = variables;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getVariables() {
        return variables;
    }

    public void setVariables(String[] variables) {
        this.variables = variables;
    }
}
