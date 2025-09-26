package org.analyse.xml;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Handles XML input sources, encoding detection, and input stream management.
 * Extracted from XmlParser for better separation of concerns.
 *
 * @author Refactored from original XmlParser
 * @version 1.0
 */
public class XmlInputHandler {

    private static final Logger logger = Logger.getLogger(XmlInputHandler.class.getName());

    // Current input state
    private String basePublicId;
    private String baseURI;
    private Reader baseReader;
    private InputStream baseInputStream;

    // Input stack for nested parsing
    private final Stack<InputSource> inputStack = new Stack<>();

    // Current input source
    private InputSource currentInputSource;

    /**
     * Inner class to represent an input source.
     */
    private static class InputSource {
        String publicId;
        String systemId;
        Reader reader;
        InputStream inputStream;
        String encoding;
        int type;
        boolean isPE;
        boolean isExternal;
        String externalEntity;

        InputSource(String publicId, String systemId, Reader reader,
                   InputStream inputStream, String encoding, int type) {
            this.publicId = publicId;
            this.systemId = systemId;
            this.reader = reader;
            this.inputStream = inputStream;
            this.encoding = encoding;
            this.type = type;
        }
    }

    /**
     * Initialize the input handler with base input parameters.
     *
     * @param systemId The system ID
     * @param publicId The public ID
     * @param reader Character stream reader (can be null)
     * @param inputStream Byte input stream (can be null)
     * @param encoding Encoding hint (can be null)
     */
    public void initialize(String systemId, String publicId, Reader reader,
                          InputStream inputStream, String encoding) {
        this.basePublicId = publicId;
        this.baseURI = systemId;
        this.baseReader = reader;
        this.baseInputStream = inputStream;
    }

    /**
     * Push a new input source onto the stack.
     *
     * @param entityName The name of the entity
     * @param publicId The public ID
     * @param systemId The system ID
     * @param reader Character stream reader (can be null)
     * @param inputStream Byte input stream (can be null)
     * @param encoding Encoding hint (can be null)
     * @throws Exception If there's an error opening the input source
     */
    public void pushURL(String entityName, String publicId, String systemId,
                       Reader reader, InputStream inputStream, String encoding)
            throws Exception {

        // Create input source based on available parameters
        InputSource source;

        if (reader != null) {
            source = new InputSource(publicId, systemId, reader, null, null,
                                   org.analyse.xml.XmlConstants.INPUT_READER);
        } else if (inputStream != null) {
            source = new InputSource(publicId, systemId, null, inputStream,
                                   encoding, org.analyse.xml.XmlConstants.INPUT_STREAM);
        } else if (systemId != null) {
            // Try to open URL
            try {
                URL url = new URL(systemId);
                URLConnection conn = url.openConnection();
                InputStream stream = new BufferedInputStream(conn.getInputStream());
                source = new InputSource(publicId, systemId, null, stream,
                                       encoding, org.analyse.xml.XmlConstants.INPUT_EXTERNAL);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to open URL: " + systemId, e);
                throw e;
            }
        } else {
            throw new IllegalArgumentException("No input source provided");
        }

        // Push current source to stack if it exists
        if (currentInputSource != null) {
            inputStack.push(currentInputSource);
        }

        currentInputSource = source;
    }

    /**
     * Pop the current input source and return to the previous one.
     *
     * @throws Exception If there's no input source to pop to
     */
    public void popURL() throws Exception {
        if (inputStack.isEmpty()) {
            throw new IllegalStateException("No input source to pop to");
        }

        // Close current input source if needed
        closeCurrentSource();

        // Restore previous input source
        currentInputSource = inputStack.pop();
    }

    /**
     * Close the current input source.
     */
    private void closeCurrentSource() {
        if (currentInputSource != null) {
            try {
                if (currentInputSource.reader != null) {
                    currentInputSource.reader.close();
                }
                if (currentInputSource.inputStream != null) {
                    currentInputSource.inputStream.close();
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error closing input source", e);
            }
        }
    }

    /**
     * Get the current input stream.
     *
     * @return The current input stream, or null if none
     */
    public InputStream getCurrentInputStream() {
        return currentInputSource != null ? currentInputSource.inputStream : null;
    }

    /**
     * Get the current reader.
     *
     * @return The current reader, or null if none
     */
    public Reader getCurrentReader() {
        return currentInputSource != null ? currentInputSource.reader : null;
    }

    /**
     * Get the current system ID.
     *
     * @return The current system ID, or null if none
     */
    public String getCurrentSystemId() {
        return currentInputSource != null ? currentInputSource.systemId : baseURI;
    }

    /**
     * Get the current public ID.
     *
     * @return The current public ID, or null if none
     */
    public String getCurrentPublicId() {
        return currentInputSource != null ? currentInputSource.publicId : basePublicId;
    }

    /**
     * Get the current encoding.
     *
     * @return The current encoding, or null if none
     */
    public String getCurrentEncoding() {
        return currentInputSource != null ? currentInputSource.encoding : null;
    }

    /**
     * Check if the input stack is empty.
     *
     * @return true if the stack is empty
     */
    public boolean isStackEmpty() {
        return inputStack.isEmpty();
    }

    /**
     * Clean up all resources.
     */
    public void cleanup() {
        closeCurrentSource();

        while (!inputStack.isEmpty()) {
            InputSource source = inputStack.pop();
            try {
                if (source.reader != null) {
                    source.reader.close();
                }
                if (source.inputStream != null) {
                    source.inputStream.close();
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error cleaning up input source", e);
            }
        }

        currentInputSource = null;
        baseReader = null;
        baseInputStream = null;
    }
}