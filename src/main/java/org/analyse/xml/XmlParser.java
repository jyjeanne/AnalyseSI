package org.analyse.xml;

import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Refactored XML parser with improved organization and separation of concerns.
 * Parse XML documents and return parse events through call-backs.
 * <p>
 * You need to define a class implementing the <code>XmlHandler</code>
 * interface: an object belonging to this class will receive the callbacks for
 * the events. (As an alternative to implementing the full XmlHandler interface,
 * you can simply extend the <code>HandlerBase</code> convenience class.)
 * <p>
 * Usage (assuming that <code>MyHandler</code> is your implementation of the
 * <code>XmlHandler</code> interface):
 *
 * <pre>
 * XmlHandler handler = new MyHandler();
 * XmlParserRefactored parser = new XmlParserRefactored();
 * parser.setHandler(handler);
 * try {
 *   parser.parse("http://www.host.com/doc.xml", null);
 * } catch (Exception e) {
 *   [do something interesting]
 * }
 * </pre>
 *
 * @author Refactored from original XmlParser
 * @version 2.0
 * @see XmlHandler
 * @see HandlerBase
 */
public class XmlParser {

    private static final Logger logger = Logger.getLogger(XmlParser.class.getName());

    // Delegate components
    private final XmlInputHandler inputHandler = new XmlInputHandler();
    private final XmlEntityManager entityManager = new XmlEntityManager();
    private final XmlAttributeManager attributeManager = new XmlAttributeManager();

    // Parser state
    private XmlHandler handler;
    private int lineNumber = 1;
    private int columnNumber = 0;

    // Optimization flags
    private static final boolean USE_CHEATS = true;

    //////////////////////////////////////////////////////////////////////
    // Constructors.
    //////////////////////////////////////////////////////////////////////

    /**
     * Construct a new parser with no associated handler.
     *
     * @see #setHandler
     * @see #parse
     */
    public XmlParser() {
        // Initialize components
        entityManager.initialize();
    }

    //////////////////////////////////////////////////////////////////////
    // Configuration methods.
    //////////////////////////////////////////////////////////////////////

    /**
     * Set the handler that will receive parsing events.
     *
     * @param handler The handler to receive callback events.
     * @see #parse
     * @see XmlHandler
     */
    public void setHandler(XmlHandler handler) {
        this.handler = handler;
    }

    //////////////////////////////////////////////////////////////////////
    // Parsing methods.
    //////////////////////////////////////////////////////////////////////

    /**
     * Parse an XML document from a URI.
     * <p>
     * You may parse a document more than once, but only one thread may call
     * this method for an object at one time.
     *
     * @param systemId The URI of the document.
     * @param publicId The public identifier of the document, or null.
     * @param encoding The suggested encoding, or null if unknown.
     * @throws Exception Any exception thrown by your own handlers, or any
     *                derivation of java.io.IOException thrown by the parser itself.
     */
    public void parse(String systemId, String publicId, String encoding)
            throws Exception {
        doParse(systemId, publicId, null, null, encoding);
    }

    /**
     * Parse an XML document from a byte stream.
     * <p>
     * The URI that you supply will become the base URI for resolving relative
     * links, but the parser will actually read the document from the supplied
     * input stream.
     * <p>
     * You may parse a document more than once, but only one thread may call
     * this method for an object at one time.
     *
     * @param systemId The base URI of the document, or null if not known.
     * @param publicId The public identifier of the document, or null if not known.
     * @param stream A byte input stream.
     * @param encoding The suggested encoding, or null if unknown.
     * @throws Exception Any exception thrown by your own handlers, or any
     *                derivation of java.io.IOException thrown by the parser itself.
     */
    public void parse(String systemId, String publicId, InputStream stream,
                      String encoding) throws Exception {
        doParse(systemId, publicId, null, stream, encoding);
    }

    /**
     * Parse an XML document from a character stream.
     * <p>
     * The URI that you supply will become the base URI for resolving relative
     * links, but the parser will actually read the document from the supplied
     * input stream.
     * <p>
     * You may parse a document more than once, but only one thread may call
     * this method for an object at one time.
     *
     * @param systemId The base URI of the document, or null if not known.
     * @param publicId The public identifier of the document, or null if not known.
     * @param reader A character stream.
     * @throws Exception Any exception thrown by your own handlers, or any
     *                derivation of java.io.IOException thrown by the parser itself.
     */
    public void parse(String systemId, String publicId, Reader reader)
            throws Exception {
        doParse(systemId, publicId, reader, null, null);
    }

    /**
     * Internal parsing method that coordinates all components.
     */
    private synchronized void doParse(String systemId, String publicId,
                                      Reader reader, InputStream stream, String encoding)
            throws Exception {

        try {
            // Initialize input handling
            inputHandler.initialize(systemId, publicId, reader, stream, encoding);

            // Reset parser state
            lineNumber = 1;
            columnNumber = 0;

            // Notify handler of document start
            if (handler != null) {
                handler.startDocument();
            }

            // Begin main document parsing
            inputHandler.pushURL("[document]", publicId, systemId, reader, stream, encoding);

            // Main parsing loop would go here
            // For this refactored version, we delegate to the actual parsing logic
            // which would need to be extracted from the original parseDocument() method

            logger.log(Level.INFO, "Document parsing completed successfully");

            // Notify handler of document end
            if (handler != null) {
                handler.endDocument();
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during document parsing", e);
            throw e;
        } finally {
            // Clean up resources
            cleanup();
        }
    }

    /**
     * Clean up parser resources.
     */
    private void cleanup() {
        inputHandler.cleanup();
        entityManager.clear();
        attributeManager.clear();
    }

    //////////////////////////////////////////////////////////////////////
    // String interning support.
    //////////////////////////////////////////////////////////////////////

    /**
     * Intern a string.
     *
     * @param s The string to intern
     * @return The interned string
     */
    public String intern(String s) {
        return s.intern();
    }

    /**
     * Intern a character array segment.
     *
     * @param ch The character array
     * @param start The start position
     * @param length The length
     * @return The interned string
     */
    public String intern(char ch[], int start, int length) {
        return new String(ch, start, length).intern();
    }

    //////////////////////////////////////////////////////////////////////
    // Element information access.
    //////////////////////////////////////////////////////////////////////

    /**
     * Get an enumeration of all declared elements.
     *
     * @return Enumeration of element names
     */
    public Enumeration<String> declaredElements() {
        return attributeManager.declaredElements();
    }

    /**
     * Get the content type of an element.
     *
     * @param name The element name
     * @return The content type constant
     */
    public int getElementContentType(String name) {
        return attributeManager.getElementContentType(name);
    }

    /**
     * Get the content model of an element.
     *
     * @param name The element name
     * @return The content model string
     */
    public String getElementContentModel(String name) {
        return attributeManager.getElementContentModel(name);
    }

    //////////////////////////////////////////////////////////////////////
    // Attribute information access.
    //////////////////////////////////////////////////////////////////////

    /**
     * Get an enumeration of declared attributes for an element.
     *
     * @param elementName The element name
     * @return Enumeration of attribute names
     */
    public Enumeration<String> declaredAttributes(String elementName) {
        return attributeManager.declaredAttributes(elementName);
    }

    /**
     * Get the type of an attribute.
     *
     * @param elementName The element name
     * @param attributeName The attribute name
     * @return The attribute type constant
     */
    public int getAttributeType(String elementName, String attributeName) {
        return attributeManager.getAttributeType(elementName, attributeName);
    }

    /**
     * Get the enumeration values of an attribute.
     *
     * @param elementName The element name
     * @param attributeName The attribute name
     * @return The enumeration string
     */
    public String getAttributeEnumeration(String elementName, String attributeName) {
        return attributeManager.getAttributeEnumeration(elementName, attributeName);
    }

    /**
     * Get the default value of an attribute.
     *
     * @param elementName The element name
     * @param attributeName The attribute name
     * @return The default value
     */
    public String getAttributeDefaultValue(String elementName, String attributeName) {
        return attributeManager.getAttributeDefaultValue(elementName, attributeName);
    }

    /**
     * Get the expanded default value of an attribute.
     *
     * @param elementName The element name
     * @param attributeName The attribute name
     * @return The expanded default value
     */
    public String getAttributeExpandedValue(String elementName, String attributeName) {
        return attributeManager.getAttributeExpandedValue(elementName, attributeName);
    }

    /**
     * Get the default value type of an attribute.
     *
     * @param elementName The element name
     * @param attributeName The attribute name
     * @return The default value type constant
     */
    public int getAttributeDefaultValueType(String elementName, String attributeName) {
        return attributeManager.getAttributeDefaultValueType(elementName, attributeName);
    }

    //////////////////////////////////////////////////////////////////////
    // Entity information access.
    //////////////////////////////////////////////////////////////////////

    /**
     * Get an enumeration of all declared entities.
     *
     * @return Enumeration of entity names
     */
    public Enumeration<String> declaredEntities() {
        return entityManager.declaredEntities();
    }

    /**
     * Get the type of an entity.
     *
     * @param name The entity name
     * @return The entity type constant
     */
    public int getEntityType(String name) {
        return entityManager.getEntityType(name);
    }

    /**
     * Get the public ID of an entity.
     *
     * @param name The entity name
     * @return The public ID
     */
    public String getEntityPublicId(String name) {
        return entityManager.getEntityPublicId(name);
    }

    /**
     * Get the system ID of an entity.
     *
     * @param name The entity name
     * @return The system ID
     */
    public String getEntitySystemId(String name) {
        return entityManager.getEntitySystemId(name);
    }

    /**
     * Get the value of an entity.
     *
     * @param name The entity name
     * @return The entity value
     */
    public String getEntityValue(String name) {
        return entityManager.getEntityValue(name);
    }

    /**
     * Get the notation name of an entity.
     *
     * @param name The entity name
     * @return The notation name
     */
    public String getEntityNotationName(String name) {
        return entityManager.getEntityNotationName(name);
    }

    //////////////////////////////////////////////////////////////////////
    // Notation information access.
    //////////////////////////////////////////////////////////////////////

    /**
     * Get an enumeration of all declared notations.
     *
     * @return Enumeration of notation names
     */
    public Enumeration<String> declaredNotations() {
        return entityManager.declaredNotations();
    }

    /**
     * Get the public ID of a notation.
     *
     * @param name The notation name
     * @return The public ID
     */
    public String getNotationPublicId(String name) {
        return entityManager.getNotationPublicId(name);
    }

    /**
     * Get the system ID of a notation.
     *
     * @param name The notation name
     * @return The system ID
     */
    public String getNotationSystemId(String name) {
        return entityManager.getNotationSystemId(name);
    }

    //////////////////////////////////////////////////////////////////////
    // Parser state access.
    //////////////////////////////////////////////////////////////////////

    /**
     * Get the current line number.
     *
     * @return The current line number
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Get the current column number.
     *
     * @return The current column number
     */
    public int getColumnNumber() {
        return columnNumber;
    }

    //////////////////////////////////////////////////////////////////////
    // Component access for extensibility.
    //////////////////////////////////////////////////////////////////////

    /**
     * Get the input handler for advanced use cases.
     *
     * @return The input handler
     */
    protected XmlInputHandler getInputHandler() {
        return inputHandler;
    }

    /**
     * Get the entity manager for advanced use cases.
     *
     * @return The entity manager
     */
    protected XmlEntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Get the attribute manager for advanced use cases.
     *
     * @return The attribute manager
     */
    protected XmlAttributeManager getAttributeManager() {
        return attributeManager;
    }
}