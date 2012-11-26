/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

public class SerializerException extends Exception {
    private static final long serialVersionUID = 4487547060835012577L;

    public SerializerException(String aComments) {
        super(aComments);
    }

    public SerializerException() {
        super();
    }

    public SerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializerException(Throwable cause) {
        super(cause);
    }
}
