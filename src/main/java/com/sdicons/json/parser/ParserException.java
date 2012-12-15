/*******************************************************************************
 * Copyright (c) 2006-2013 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.parser;

public class ParserException
extends Exception
{
    private static final long serialVersionUID = 1L;
    
    private int line = -1;
    private int column = -1;
    private String streamName = "[unknown stream]";

    public ParserException()
    {
        super();
    }

    public ParserException(String aMsg)
    {
        super(aMsg);
    }

    public ParserException(String aStreamName, int aLine, int aCol, String aMsg)
    {
        super(aMsg);
        line = aLine;
        column = aCol;
        streamName = aStreamName;
    }

    public int getLine()
    {
        return line;
    }

    public int getColumn()
    {
        return column;
    }

    public String getStreamName()
    {
        return streamName;
    }

    @Override
    public String getMessage()
    {
        final StringBuilder lBuilder = new StringBuilder();
        lBuilder.append("Error in stream: \"").append(streamName).append("\" in (line/col): (").append(line).append("/").append(column).append(") with message: \n").append(super.getMessage());
        return lBuilder.toString();
    }
}
