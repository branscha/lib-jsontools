/*******************************************************************************
 * Copyright (c) 2006-2012 Bruno Ranschaert
 * Released under the MIT License: http://opensource.org/licenses/MIT
 * Library "jsontools"
 ******************************************************************************/
package com.sdicons.json.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONBoolean;
import com.sdicons.json.model.JSONDecimal;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONNull;
import com.sdicons.json.model.JSONNumber;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

/**
 * Reads JSON text and convert it into a Java model for further handling.
 */
public class JSONParser
{
    // Error messages.
    //
    private static final String PARSER001 = "JSONParser/001: Unexpected content encountered.\nContext: %s X <--ERROR";
    private static final String PARSER002 = "JSONParser/002: Input error during parsing.\nContext: %s X <--ERROR";
    private static final String PARSER003 = "JSONParser/003: Expected symbol '%s' but received token/symbol '%s'.\nContext: %s X <--ERROR";
    private static final String PARSER004 = "JSONParser/004: The object contains a key that is not a string.\nContext: %s X <--ERROR";
    private static final String PARSER005 = "JSONParser/005: The parser was not initialized correctly.";
    //
    private static final String EOF = "EOF";
    private static final String NULL_LITERAL = "null";
    private static final String UNKNOWN_STREAM = "[unknown]";
    //
    private String streamName = UNKNOWN_STREAM;
    private StreamTokenizer st = null;

    /**
     * Construct a parser using a stream.
     * @param aStream A stream containing JSON text.
     * @param aStreamName A String that describes the stream, it will be attached to
     * all JSON objects in the model which are generated from this parser. This makes it
     * possible to identify the stream where the object came from.
     * @throws ParserException   When an error occurs during parser construction.
     */
    public JSONParser(InputStream aStream, String aStreamName)
    throws ParserException
    {
        streamName = aStreamName==null?UNKNOWN_STREAM:aStreamName;
        st = new StreamTokenizer(new InputStreamReader(aStream));
        st.commentChar('#');
    }

    /**
     * Construct a parser using a stream.
     * @param aStream A stream containing JSON text.
     * @throws ParserException  When an error occurs during parser construction.
     */
    public JSONParser(InputStream aStream)
    throws ParserException
    {
        this(aStream, null);
    }

    /**
     * Construct a parser using a reader.
     * @param aReader A reader containing JSON text.
     * @param aStreamName A String that describes the stream, it will be attached to
     * all JSON objects in the model which are generated from this parser. This makes it
     * possible to identify the stream where the object came from.
     * @throws ParserException  When an error occurs during parser construction.
     */
    public JSONParser(Reader aReader, String aStreamName)
    throws ParserException
    {
        streamName = aStreamName==null?UNKNOWN_STREAM:aStreamName;
        st = new StreamTokenizer(aReader);
        st.commentChar('#');
    }

    /**
     * Construct a parser using a reader.
     * @param aReader A reader containing JSON text.
     * @throws ParserException  When an error occurs during parser construction.
     */
    public JSONParser(Reader aReader)
    throws ParserException
    {
        this(aReader, null);
    }

    /**
     * Read the next JSON structure from the stream and convert it into a
     * Java model.
     * @return    A Java object representing the object in the stream.
     * @throws ParserException When a lexer/parser error occured while parsing the stream.
     */
    public JSONValue nextValue()
    throws ParserException
    {
        if(st == null) throw new ParserException(PARSER005);

        try
        {
            return parseJson(new StringBuilder());
        }
        catch(Exception e)
        {
            throw new ParserException(streamName, st.lineno(), 0, e.getMessage());
        }
    }

    // The parsing workhorse.
    //
    protected JSONValue parseJson(StringBuilder parsed) {
        // This is the top-level of the JSON parser, it decides which kind of
        // JSON expression is next in the input stream. The general strategy
        // is to look at the first characters, make a decision about which
        // expression we expect and call the appropriate expression parser. In order to
        // make sure the individual expression parsers see the whole expression we push
        // back the tokens we used to make the decision.
        // Each JSON expression type should have an entry here.
        try {
            st.nextToken();
            switch (st.ttype) {
            case '{':
                // The start of a JSON object.
                //
                parsed.append("{");
                return parseJsonObject(parsed);
            case '[':
                // The start of a JSON list.
                //
                parsed.append("[");
                return parseJsonList(parsed);
            case StreamTokenizer.TT_NUMBER:
                // We must take care of exponential notation as well
                //
                double num = st.nval;
                int exp = 0;
                st.ordinaryChars('\0', ' ');
                st.wordChars('+', '+');
                st.nextToken();
                st.whitespaceChars('\0', ' ');
                st.ordinaryChars('+', '+');
                if (st.ttype == StreamTokenizer.TT_WORD && Character.toUpperCase(st.sval.charAt(0)) == 'E') {
                    String sss = st.sval;
                    try {
                        if (sss.charAt(1) == '+')
                            exp = Integer.parseInt(sss.substring(2));
                        else
                            exp = Integer.parseInt(sss.substring(1));
                    }
                    catch (NumberFormatException e) {
                        st.pushBack();
                    }
                }
                else if (st.ttype < 0 || st.ttype > ' ') st.pushBack();
                num =  num*Math.pow(10,exp);
                // Plain JSON Number.
                //
                parsed.append(num);
                BigDecimal number = new BigDecimal(num);
                JSONNumber resultNumber = null;
                try {
                    BigInteger integer = number.toBigIntegerExact();
                    resultNumber = new JSONInteger(integer);
                }
                catch (ArithmeticException e) {
                    resultNumber = new JSONDecimal(number);
                }
                resultNumber.setLineCol(st.lineno(), 0);
                resultNumber.setStreamName(streamName);
                return resultNumber;
            case '"':
                // JSON String expression.
                //
                st.quoteChar('"');
                parsed.append('"').append(st.sval).append('"');
                JSONString resultString =  new JSONString(st.sval);
                resultString.setLineCol(st.lineno(), 0);
                resultString.setStreamName(streamName);
                return resultString;
            case '\'':
                // JSON String expression.
                //
                st.quoteChar('\'');
                parsed.append('\'').append(st.sval).append('\'');
                JSONString resultString2 = new JSONString(st.sval);
                resultString2.setLineCol(st.lineno(), 0);
                resultString2.setStreamName(streamName);
                return resultString2;
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
                    throw new IllegalArgumentException(String.format(PARSER001, parsed.toString()));
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format(PARSER002, parsed.toString()), e);
        }
    }

    // Parse an object.
    // The first '{' is not in the stream anymore, the top-level parsing routine
    // already read it.
    //
    private JSONObject parseJsonObject(StringBuilder parsed) {
        // This is the JSON object parser, it parses expressions of the form: {
        // key : value , ... }.
        //
        try {
            final JSONObject obj = new JSONObject();
            obj.setLineCol(st.lineno(), 0);
            obj.setStreamName(streamName);

            st.nextToken();
            while (st.ttype != '}') {
                // Key.
                st.pushBack();
                final JSONValue key = parseJson(parsed);
                if(!(key instanceof JSONString)) {
                    throw new IllegalArgumentException(String.format(PARSER004, parsed.toString()));
                }

                // Colon.
                st.nextToken();
                if ((char) st.ttype != ':') {
                    expectationError(":", st, parsed);
                }
                parsed.append(':');

                // Value.
                final JSONValue value = parseJson(parsed);
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
            throw new IllegalArgumentException(String.format(PARSER002, parsed.toString()), e);
        }
    }

    // Convert the current tokenizer token into a readable string so that
    // we can create readable error messages with it.
    //
    private String errToken() {
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
    private String expectationError(String expected, StreamTokenizer st, StringBuilder parsed) {
        throw new IllegalArgumentException(String.format(PARSER003, expected, errToken(), parsed.toString()));
    }

    // Parse an object.
    // The first '[' is not in the stream anymore, the top-level parsing routine
    // already read it.
    //
    private JSONArray parseJsonList(StringBuilder parsed) {
        // This is the JSON list parser, it parses expressions of the form: [
        // val-1, val-2, ... val-n ].
        //
        try {
            final JSONArray array = new JSONArray();
            array.setLineCol(st.lineno(), 0);
            array.setStreamName(streamName);

            st.nextToken();
            while (st.ttype != ']') {
                // Element
                st.pushBack();
                JSONValue element = parseJson(parsed);
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
            throw new IllegalArgumentException(String.format(PARSER002, parsed.toString()), e);
        }
    }
}