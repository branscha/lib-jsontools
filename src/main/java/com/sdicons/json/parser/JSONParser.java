/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONBoolean;
import com.sdicons.json.model.JSONDecimal;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONNull;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

/**
 * Reads JSON text and convert it into a Java model for further handling.
 */
public class JSONParser
{
//    private com.sdicons.json.parser.impl.JsonAntlrParser parser;
    private String streamName = "[unknown]";
    private StreamTokenizer st = null;

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
        streamName = aStreamName;
        st = new StreamTokenizer(new InputStreamReader(aStream));
        st.commentChar('#');
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
        streamName = aStreamName;
        st = new StreamTokenizer(aReader);
        st.commentChar('#');
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
            return parseJson(st,  new StringBuilder());
        }
        catch(Exception e)
        {
            throw new JSONParserException(streamName, st.lineno(), 0, e.getMessage());
        }
    }
    
    //////////////////////////////////////////
    
    // Error messages.
    //
    private static final String JSON001 = "JSON001: Unexpected content encountered.\nContext: %s X <--ERROR";
    private static final String JSON002 = "JSON002: Input error during parsing.\nContext: %s X <--ERROR";
    private static final String JSON003 = "JSON003: Expected symbol '%s' but received token/symbol '%s'.\nContext: %s X <--ERROR";

    private static final String EOF = "EOF";
    private static final String NULL_LITERAL = "null";
    
    /**
     * Convert a JSON string into a nested structure of Map instances.
     * 
     * @param data
     *            The JSON string.
     * @return A nested data structure of Map/List instances.
     * 
     */
    public static Object parseJson(String data) {
        StreamTokenizer st = new StreamTokenizer(new StringReader(data));
        StringBuilder parsed = new StringBuilder(data.length());
        return parseJson(st, parsed);
    }
     
    /**
     * Convert a JSON string into a nested structure of Map instances.
     * 
     * @param data
     *            The JSON text stream.
     * @return A nested data structure of Map/List instances.
     * 
     */
    public static Object parseJson(BufferedReader reader) {
        StreamTokenizer st = new StreamTokenizer(reader);
        StringBuilder parsed = new StringBuilder();
        return parseJson(st, parsed);
    }

    // The parsing workhorse.
    //
    protected static JSONValue parseJson(StreamTokenizer st, StringBuilder parsed) {
        // This is the top-level of the JSON parser, it decides which kind of
        // JSON expression is next in the input stream. The general strategy
        // is to look at the first characters, make a decision about which
        // expression
        // we expect and call the appropriate expression parser. In order to
        // make sure
        // the individual expression parsers see the whole expression we push
        // back the tokens we used to make the decision.
        // Each JSON expression type should have an entry here.
        try {
            st.nextToken();
            switch (st.ttype) {
            case '{':
                // The start of a JSON object.
                //
                parsed.append("{");
                return parseJsonObject(st, parsed);
            case '[':
                // The start of a JSON list.
                //
                parsed.append("[");
                return parseJsonList(st, parsed);
            case StreamTokenizer.TT_NUMBER:
                // Plain JSON Number.
                //
                parsed.append(st.nval);
                BigDecimal number = new BigDecimal(st.nval);
                try
                {
                    BigInteger integer = number.toBigIntegerExact();
                    return new JSONInteger(integer);
                }
                catch(ArithmeticException e) 
                {
                    return new JSONDecimal(number);
                }
            case '"':
                // JSON String expression.
                //
                st.quoteChar('"');
                parsed.append('"').append(st.sval).append('"');
                return new JSONString(st.sval);
            case '\'':
                // JSON String expression.
                //
                st.quoteChar('\'');
                parsed.append('\'').append(st.sval).append('\'');
                return new JSONString(st.sval);
            default:
                if ("false".equalsIgnoreCase(st.sval)) {
                    // JSON boolean "false" constant.
                    //
                    return JSONBoolean.FALSE;
                } else if ("true".equalsIgnoreCase(st.sval)) {
                    // JSON boolean "true" constant.
                    //
                    return JSONBoolean.TRUE;
                } else if (NULL_LITERAL.equalsIgnoreCase(st.sval)) {
                    // JSON null.
                    //
                    return JSONNull.NULL;
                } else {
                    throw new IllegalArgumentException(String.format(JSON001, parsed.toString()));
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format(JSON002, parsed.toString()), e);
        }
    }

    // Parse an object.
    // The first '{' is not in the stream anymore, the top-level parsing routine
    // already read it.
    //
    private static JSONObject parseJsonObject(StreamTokenizer st, StringBuilder parsed) {
        // This is the JSON object parser, it parses expressions of the form: {
        // key : value , ... }.
        //
        try {
            final JSONObject obj = new JSONObject();
            st.nextToken();
            while (st.ttype != '}') {
                // Key.
                st.pushBack();
                final JSONValue key = parseJson(st, parsed);
                if(!(key instanceof JSONString)) throw new IllegalArgumentException("Key should be a string.");

                // Colon.
                st.nextToken();
                if ((char) st.ttype != ':') {
                    expectationError(":", st, parsed);
                }
                parsed.append(':');

                // Value.
                final JSONValue value = parseJson(st, parsed);
                obj.getValue().put(((JSONString)key).getValue(), value);

                // Comma.
                st.nextToken();
                if ((char) st.ttype != ',') {
                    if ((char) st.ttype != '}') {
                        expectationError("}", st, parsed);
                    } else {
                        parsed.append("}");
                        break;
                    }
                } else {
                    parsed.append(",");
                    st.nextToken();
                }
            }
            return obj;
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format(JSON002, parsed.toString()), e);
        }
    }

    // Convert the current tokenizer token into a readable string so that
    // we can create readable error messages with it.
    //
    private static String errToken(StreamTokenizer st) {
        switch (st.ttype) {
        case StreamTokenizer.TT_EOF:
            return EOF;
        case StreamTokenizer.TT_WORD:
            return st.sval;
        case StreamTokenizer.TT_NUMBER:
            return "" + st.nval;
        default:
            return Character.toString((char) st.ttype);
        }
    }

    // Create an error message, the tokenizer did not contain an expected
    // character.
    //
    private static String expectationError(String expected, StreamTokenizer st, StringBuilder parsed) {
        throw new IllegalArgumentException(String.format(JSON003, expected, errToken(st), parsed.toString()));
    }

    // Parse an object.
    // The first '[' is not in the stream anymore, the top-level parsing routine
    // already read it.
    //
    private static JSONArray parseJsonList(StreamTokenizer st, StringBuilder parsed) {
        // This is the JSON list parser, it parses expressions of the form: [
        // val-1, val-2, ... val-n ].
        //
        try {
            final JSONArray array = new JSONArray();
            st.nextToken();
            while (st.ttype != ']') {
                // Element
                st.pushBack();
                JSONValue element = parseJson(st, parsed);

                array.getValue().add(element);

                // Comma.
                st.nextToken();
                if ((char) st.ttype != ',') {
                    if ((char) st.ttype != ']') {
                        expectationError("]", st, parsed);
                    } else {
                        parsed.append("]");
                        break;
                    }
                } else {
                    parsed.append(",");
                    st.nextToken();
                }
            }
            return array;
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format(JSON002, parsed.toString()), e);
        }
    }

}
