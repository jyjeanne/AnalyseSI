package org.analyse.xml;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Manages XML element attributes and their declarations.
 * Extracted from XmlParser for better separation of concerns.
 *
 * @author Refactored from original XmlParser
 * @version 1.0
 */
public class XmlAttributeManager {

    private static final Logger logger = Logger.getLogger(XmlAttributeManager.class.getName());

    // Element declarations
    private final Hashtable<String, Integer> elementHash = new Hashtable<>();
    private final Hashtable<String, String> elementInfo = new Hashtable<>();

    // Attribute declarations
    private final Hashtable<String, Vector<String>> attributeHash = new Hashtable<>();
    private final Hashtable<String, Object> attributeInfo = new Hashtable<>();

    /**
     * Declare an element with its content type.
     *
     * @param name The element name
     * @param contentType The content type (from XmlConstants)
     * @param contentModel The content model string (can be null)
     */
    public void setElement(String name, int contentType, String contentModel) {
        elementHash.put(name, Integer.valueOf(contentType));
        if (contentModel != null) {
            elementInfo.put(name + ".model", contentModel);
        }

        logger.log(Level.FINE, "Declared element: " + name + " type=" + contentType);
    }

    /**
     * Get the content type of an element.
     *
     * @param name The element name
     * @return The content type constant
     */
    public int getElementContentType(String name) {
        Integer type = elementHash.get(name);
        return type != null ? type.intValue() : XmlConstants.CONTENT_UNDECLARED;
    }

    /**
     * Get the content model of an element.
     *
     * @param name The element name
     * @return The content model string, or null if not available
     */
    public String getElementContentModel(String name) {
        return elementInfo.get(name + ".model");
    }

    /**
     * Check if an element is declared.
     *
     * @param name The element name
     * @return true if the element is declared
     */
    public boolean isElementDeclared(String name) {
        return elementHash.containsKey(name);
    }

    /**
     * Get an enumeration of all declared element names.
     *
     * @return Enumeration of element names
     */
    public Enumeration<String> declaredElements() {
        return elementHash.keys();
    }

    /**
     * Declare an attribute for an element.
     *
     * @param elementName The element name
     * @param attributeName The attribute name
     * @param type The attribute type (from XmlConstants)
     * @param defaultValueType The default value type (from XmlConstants)
     * @param defaultValue The default value (can be null)
     * @param enumeration The enumeration values (can be null)
     */
    public void setAttribute(String elementName, String attributeName, int type,
                            int defaultValueType, String defaultValue, String enumeration) {

        String key = elementName + "." + attributeName;

        // Add to element's attribute list
        Vector<String> attributes = attributeHash.get(elementName);
        if (attributes == null) {
            attributes = new Vector<>();
            attributeHash.put(elementName, attributes);
        }
        if (!attributes.contains(attributeName)) {
            attributes.addElement(attributeName);
        }

        // Store attribute information
        attributeInfo.put(key + ".type", Integer.valueOf(type));
        attributeInfo.put(key + ".defaultType", Integer.valueOf(defaultValueType));
        if (defaultValue != null) {
            attributeInfo.put(key + ".defaultValue", defaultValue);
        }
        if (enumeration != null) {
            attributeInfo.put(key + ".enumeration", enumeration);
        }

        logger.log(Level.FINE, "Declared attribute: " + elementName + "." + attributeName +
                  " type=" + type + " defaultType=" + defaultValueType);
    }

    /**
     * Get the type of an attribute.
     *
     * @param elementName The element name
     * @param attributeName The attribute name
     * @return The attribute type constant
     */
    public int getAttributeType(String elementName, String attributeName) {
        String key = elementName + "." + attributeName;
        Integer type = (Integer) attributeInfo.get(key + ".type");
        return type != null ? type.intValue() : XmlConstants.ATTRIBUTE_UNDECLARED;
    }

    /**
     * Get the enumeration values of an attribute.
     *
     * @param elementName The element name
     * @param attributeName The attribute name
     * @return The enumeration string, or null if not available
     */
    public String getAttributeEnumeration(String elementName, String attributeName) {
        String key = elementName + "." + attributeName;
        return (String) attributeInfo.get(key + ".enumeration");
    }

    /**
     * Get the default value of an attribute.
     *
     * @param elementName The element name
     * @param attributeName The attribute name
     * @return The default value, or null if not available
     */
    public String getAttributeDefaultValue(String elementName, String attributeName) {
        String key = elementName + "." + attributeName;
        return (String) attributeInfo.get(key + ".defaultValue");
    }

    /**
     * Get the expanded default value of an attribute (with entity references resolved).
     *
     * @param elementName The element name
     * @param attributeName The attribute name
     * @return The expanded default value, or null if not available
     */
    public String getAttributeExpandedValue(String elementName, String attributeName) {
        String key = elementName + "." + attributeName;
        String value = (String) attributeInfo.get(key + ".defaultValue");

        // In a full implementation, this would resolve entity references
        // For now, return the raw value
        return value;
    }

    /**
     * Get the default value type of an attribute.
     *
     * @param elementName The element name
     * @param attributeName The attribute name
     * @return The default value type constant
     */
    public int getAttributeDefaultValueType(String elementName, String attributeName) {
        String key = elementName + "." + attributeName;
        Integer type = (Integer) attributeInfo.get(key + ".defaultType");
        return type != null ? type.intValue() : XmlConstants.ATTRIBUTE_DEFAULT_UNDECLARED;
    }

    /**
     * Check if an attribute is declared for an element.
     *
     * @param elementName The element name
     * @param attributeName The attribute name
     * @return true if the attribute is declared
     */
    public boolean isAttributeDeclared(String elementName, String attributeName) {
        String key = elementName + "." + attributeName;
        return attributeInfo.containsKey(key + ".type");
    }

    /**
     * Get an enumeration of all declared attributes for an element.
     *
     * @param elementName The element name
     * @return Enumeration of attribute names, or null if element has no attributes
     */
    public Enumeration<String> declaredAttributes(String elementName) {
        Vector<String> attributes = attributeHash.get(elementName);
        return attributes != null ? attributes.elements() : null;
    }

    /**
     * Get the attribute type from a string name.
     *
     * @param typeName The type name (e.g., "CDATA", "ID", etc.)
     * @return The attribute type constant
     */
    public int getAttributeTypeFromString(String typeName) {
        Integer type = XmlConstants.getAttributeTypeHash().get(typeName);
        return type != null ? type.intValue() : XmlConstants.ATTRIBUTE_UNDECLARED;
    }

    /**
     * Clear all element and attribute declarations.
     */
    public void clear() {
        elementHash.clear();
        elementInfo.clear();
        attributeHash.clear();
        attributeInfo.clear();

        logger.log(Level.FINE, "Cleared all element and attribute declarations");
    }

    /**
     * Get element count for debugging purposes.
     *
     * @return The number of declared elements
     */
    public int getElementCount() {
        return elementHash.size();
    }

    /**
     * Get total attribute count for debugging purposes.
     *
     * @return The total number of declared attributes across all elements
     */
    public int getAttributeCount() {
        int count = 0;
        for (Vector<String> attrs : attributeHash.values()) {
            count += attrs.size();
        }
        return count;
    }
}