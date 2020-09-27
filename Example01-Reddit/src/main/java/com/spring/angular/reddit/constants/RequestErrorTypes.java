package com.spring.angular.reddit.constants;

public enum RequestErrorTypes {
    BASIC_INVALID_INPUT("SVC0002", "Invalid input value for message part %1"),

    INVALD_ENUM_INPUT("SVC0003", "Invalid input value for message part %1, valid values are %2"),

    GENERIC_SERVICE_ERROR("SVC2000", "The following service error occurred: %1. Error code is %2"),

    INVALID_ACCESS_TOKEN("SVC2003", "Invalid access token"),

    EXPIRED_ACCESS_TOKEN("SVC2003", "Expired access token"),

    DETAILED_INVALID_INPUT("SVC2004", "Invalid input value for %1 %2: %3"),

    EXTRA_INPUT_NOT_ALLOWED("SVC2005", "Input %1 %2 not permitted in request"),

    MANDATORY_INPUT_MISSING("SVC2006", "Mandatory input %1 %2 is missing from request"),

    INVALID_PARAMETER_UPDATE("PLC001", "Invalid input value for %1 %2"),

    GENERIC_POLICY_ERROR("POL2000", "The following policy error occurred: %1. Error code is %2."),

    ASSIGNMENT_POLICY_ERROR("POL2001", "The following policy error occurred for assignment/unassignment operation: %1 reason text: %2"),

    UNKNOWN_RESOURCE("SVC2008", "Unknown %1 %2"),

    FEATURE_NOT_AVAILABLE("POL2006", "Requested feature %1 not available");


    private String messageId;
    private String text;

    RequestErrorTypes(final String messageId, final String text) {
        this.messageId = messageId;
        this.text = text;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getText() {
        return text;
    }
}
