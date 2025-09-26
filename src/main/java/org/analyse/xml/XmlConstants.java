package org.analyse.xml;

import java.util.Hashtable;

/**
 * Constants used by the XML parser.
 * Extracted from XmlParser for better organization and maintainability.
 *
 * @author Refactored from original XmlParser
 * @version 1.0
 */
public final class XmlConstants {

    private XmlConstants() {
        // Prevent instantiation
    }

    //
    // Constants for element content type.
    //

    /**
     * Constant: an element has not been declared.
     *
     * @see XmlParser#getElementContentType
     */
    public static final int CONTENT_UNDECLARED = 0;

    /**
     * Constant: the element has a content model of ANY.
     *
     * @see XmlParser#getElementContentType
     */
    public static final int CONTENT_ANY = 1;

    /**
     * Constant: the element has declared content of EMPTY.
     *
     * @see XmlParser#getElementContentType
     */
    public static final int CONTENT_EMPTY = 2;

    /**
     * Constant: the element has mixed content.
     *
     * @see XmlParser#getElementContentType
     */
    public static final int CONTENT_MIXED = 3;

    /**
     * Constant: the element has element content.
     *
     * @see XmlParser#getElementContentType
     */
    public static final int CONTENT_ELEMENTS = 4;

    //
    // Constants for the entity type.
    //

    /**
     * Constant: the entity has not been declared.
     *
     * @see XmlParser#getEntityType
     */
    public static final int ENTITY_UNDECLARED = 0;

    /**
     * Constant: the entity is internal.
     *
     * @see XmlParser#getEntityType
     */
    public static final int ENTITY_INTERNAL = 1;

    /**
     * Constant: the entity is external, non-XML data.
     *
     * @see XmlParser#getEntityType
     */
    public static final int ENTITY_NDATA = 2;

    /**
     * Constant: the entity is external XML data.
     *
     * @see XmlParser#getEntityType
     */
    public static final int ENTITY_TEXT = 3;

    //
    // Constants for attribute type.
    //

    /**
     * Constant: the attribute has not been declared for this element type.
     *
     * @see XmlParser#getAttributeType
     */
    public static final int ATTRIBUTE_UNDECLARED = 0;

    /**
     * Constant: the attribute value is a string value.
     *
     * @see XmlParser#getAttributeType
     */
    public static final int ATTRIBUTE_CDATA = 1;

    /**
     * Constant: the attribute value is a unique identifier.
     *
     * @see XmlParser#getAttributeType
     */
    public static final int ATTRIBUTE_ID = 2;

    /**
     * Constant: the attribute value is a reference to a unique identifier.
     *
     * @see XmlParser#getAttributeType
     */
    public static final int ATTRIBUTE_IDREF = 3;

    /**
     * Constant: the attribute value is a list of ID references.
     *
     * @see XmlParser#getAttributeType
     */
    public static final int ATTRIBUTE_IDREFS = 4;

    /**
     * Constant: the attribute value is the name of an entity.
     *
     * @see XmlParser#getAttributeType
     */
    public static final int ATTRIBUTE_ENTITY = 5;

    /**
     * Constant: the attribute value is a list of entity names.
     *
     * @see XmlParser#getAttributeType
     */
    public static final int ATTRIBUTE_ENTITIES = 6;

    /**
     * Constant: the attribute value is a name token.
     *
     * @see XmlParser#getAttributeType
     */
    public static final int ATTRIBUTE_NMTOKEN = 7;

    /**
     * Constant: the attribute value is a list of name tokens.
     *
     * @see XmlParser#getAttributeType
     */
    public static final int ATTRIBUTE_NMTOKENS = 8;

    /**
     * Constant: the attribute value is a token from an enumeration.
     *
     * @see XmlParser#getAttributeType
     */
    public static final int ATTRIBUTE_ENUMERATED = 9;

    /**
     * Constant: the attribute is the name of a notation.
     *
     * @see XmlParser#getAttributeType
     */
    public static final int ATTRIBUTE_NOTATION = 10;

    /**
     * Hash table of attribute types.
     */
    private static final Hashtable<String, Integer> attributeTypeHash;
    static {
        attributeTypeHash = new Hashtable<>();
        attributeTypeHash.put("CDATA", Integer.valueOf(ATTRIBUTE_CDATA));
        attributeTypeHash.put("ID", Integer.valueOf(ATTRIBUTE_ID));
        attributeTypeHash.put("IDREF", Integer.valueOf(ATTRIBUTE_IDREF));
        attributeTypeHash.put("IDREFS", Integer.valueOf(ATTRIBUTE_IDREFS));
        attributeTypeHash.put("ENTITY", Integer.valueOf(ATTRIBUTE_ENTITY));
        attributeTypeHash.put("ENTITIES", Integer.valueOf(ATTRIBUTE_ENTITIES));
        attributeTypeHash.put("NMTOKEN", Integer.valueOf(ATTRIBUTE_NMTOKEN));
        attributeTypeHash.put("NMTOKENS", Integer.valueOf(ATTRIBUTE_NMTOKENS));
        attributeTypeHash.put("NOTATION", Integer.valueOf(ATTRIBUTE_NOTATION));
    }

    /**
     * Get the attribute type hash table.
     *
     * @return The attribute type hash table
     */
    public static Hashtable<String, Integer> getAttributeTypeHash() {
        return attributeTypeHash;
    }

    //
    // Constants for supported encodings.
    //
    public static final int ENCODING_UTF_8 = 1;
    public static final int ENCODING_ISO_8859_1 = 2;
    public static final int ENCODING_UCS_2_12 = 3;
    public static final int ENCODING_UCS_2_21 = 4;
    public static final int ENCODING_UCS_4_1234 = 5;
    public static final int ENCODING_UCS_4_4321 = 6;
    public static final int ENCODING_UCS_4_2143 = 7;
    public static final int ENCODING_UCS_4_3412 = 8;
    public static final int ENCODING_UTF_16 = 9;

    //
    // Constants for attribute default value.
    //

    /**
     * Constant: the attribute is not declared.
     *
     * @see XmlParser#getAttributeDefaultValueType
     */
    public static final int ATTRIBUTE_DEFAULT_UNDECLARED = 0;

    /**
     * Constant: the attribute has a literal default value specified.
     *
     * @see XmlParser#getAttributeDefaultValueType
     * @see XmlParser#getAttributeDefaultValue
     */
    public static final int ATTRIBUTE_DEFAULT_SPECIFIED = 1;

    /**
     * Constant: the attribute was declared #IMPLIED.
     *
     * @see XmlParser#getAttributeDefaultValueType
     */
    public static final int ATTRIBUTE_DEFAULT_IMPLIED = 2;

    /**
     * Constant: the attribute was declared #REQUIRED.
     *
     * @see XmlParser#getAttributeDefaultValueType
     */
    public static final int ATTRIBUTE_DEFAULT_REQUIRED = 3;

    /**
     * Constant: the attribute was declared #FIXED.
     *
     * @see XmlParser#getAttributeDefaultValueType
     * @see XmlParser#getAttributeDefaultValue
     */
    public static final int ATTRIBUTE_DEFAULT_FIXED = 4;

    //
    // Constants for input.
    //
    public static final int INPUT_NONE = 0;
    public static final int INPUT_INTERNAL = 1;
    public static final int INPUT_EXTERNAL = 2;
    public static final int INPUT_STREAM = 3;
    public static final int INPUT_BUFFER = 4;
    public static final int INPUT_READER = 5;

    //
    // Flags for reading literals.
    //
    public static final int LIT_CHAR_REF = 1;
    public static final int LIT_ENTITY_REF = 2;
    public static final int LIT_PE_REF = 4;
    public static final int LIT_NORMALIZE = 8;

    //
    // Flags for parsing context.
    //
    public static final int CONTEXT_NONE = 0;
    public static final int CONTEXT_DTD = 1;
    public static final int CONTEXT_ENTITYVALUE = 2;
    public static final int CONTEXT_ATTRIBUTEVALUE = 3;
}