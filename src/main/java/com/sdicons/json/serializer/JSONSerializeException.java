/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.serializer;

public class JSONSerializeException extends Exception {
    private static final long serialVersionUID = 4487547060835012577L;

    public JSONSerializeException(String aComments) {
        super(aComments);
    }

    public JSONSerializeException() {
        super();
    }

    public JSONSerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSONSerializeException(Throwable cause) {
        super(cause);
    }
}
