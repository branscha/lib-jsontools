/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.parser;

import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.impl.JsonAntlrLexer;
import org.antlr.runtime.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Reads JSON text and convert it into a Java model for further handling.
 */
public class JSONParser
{
    private com.sdicons.json.parser.impl.JsonAntlrParser parser;
    private String streamName = "[unknown]";

    /**
     * Construct a parser using a stream.
     * @param aStream A stream containing JSON text.
     * @param aStreamName A String that describes the stream, it will be attached to
     * all JSON objects in the model which are generated from this parser. This makes it
     * possible to identify the stream where the object came from.
     * @throws JSONParserException   When an error occurs during parser construction.
     */
    public JSONParser(InputStream aStream, String aStreamName)
    throws JSONParserException
    {
        try
        {
            initParser(new ANTLRInputStream(aStream), aStreamName);
        }
        catch(IOException e)
        {
            throw new JSONParserException(streamName, -1, -1, e.getMessage());
        }
    }

    /**
     * Construct a parser using a stream.
     * @param aStream A stream containing JSON text.
     * @throws JSONParserException  When an error occurs during parser construction.
     */
    public JSONParser(InputStream aStream)
    throws JSONParserException
    {
        this(aStream, null);
    }

    /**
     * Construct a parser using a reader.
     * @param aReader A reader containing JSON text.
     * @param aStreamName A String that describes the stream, it will be attached to
     * all JSON objects in the model which are generated from this parser. This makes it
     * possible to identify the stream where the object came from.
     * @throws JSONParserException  When an error occurs during parser construction.
     */
    public JSONParser(Reader aReader, String aStreamName)
    throws JSONParserException
    {
        try
        {
            initParser(new ANTLRReaderStream(aReader), aStreamName);
        }
        catch(IOException e)
        {
            throw new JSONParserException(streamName, -1, -1, e.getMessage());
        }
    }

    private void initParser(CharStream aCharStream, String aStreamName )
    {
        if(aStreamName != null) streamName = aStreamName;
        final JsonAntlrLexer lLexer = new JsonAntlrLexer(aCharStream);
        final CommonTokenStream lTokens = new CommonTokenStream();
        lTokens.setTokenSource(lLexer);
        parser = new com.sdicons.json.parser.impl.JsonAntlrParser(lTokens);
    }

    /**
     * Construct a parser using a reader.
     * @param aReader A reader containing JSON text.
     * @throws JSONParserException  When an error occurs during parser construction.
     */
    public JSONParser(Reader aReader)
    throws JSONParserException
    {
        this(aReader, null);
    }

    /**
     * Read the next JSON structure from the stream and convert it into a
     * Java model.
     * @return    A Java object representing the object in the stream.
     * @throws JSONParserException When a lexer/parser error occured while parsing the stream.
     */
    public JSONValue nextValue()
    throws JSONParserException
    {
        try
        {
            return parser.value(streamName).val;
        }
        catch(RecognitionException e)
        {
            throw new JSONParserException(streamName, e.line, e.charPositionInLine, e.getMessage());
        }
    }
}
