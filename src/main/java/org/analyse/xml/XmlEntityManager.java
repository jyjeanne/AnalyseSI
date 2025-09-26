package org.analyse.xml;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Manages XML entities (internal, external, parameter entities).
 * Extracted from XmlParser for better separation of concerns.
 *
 * @author Refactored from original XmlParser
 * @version 1.0
 */
public class XmlEntityManager {

    private static final Logger logger = Logger.getLogger(XmlEntityManager.class.getName());

    // Entity storage
    private final Hashtable<String, Integer> entityHash = new Hashtable<>();
    private final Hashtable<String, String> entityInfo = new Hashtable<>();

    // Notation storage
    private final Hashtable<String, String[]> notationHash = new Hashtable<>();

    /**
     * Entity information structure.
     */
    private static class EntityInfo {
        int type;
        String publicId;
        String systemId;
        String value;
        String notationName;

        EntityInfo(int type, String publicId, String systemId, String value, String notationName) {
            this.type = type;
            this.publicId = publicId;
            this.systemId = systemId;
            this.value = value;
            this.notationName = notationName;
        }
    }

    /**
     * Initialize with default XML entities.
     */
    public void initialize() {
        // Set the default entities
        setInternalEntity("amp", "&#38;");
        setInternalEntity("lt", "&#60;");
        setInternalEntity("gt", "&#62;");
        setInternalEntity("apos", "&#39;");
        setInternalEntity("quot", "&#34;");
    }

    /**
     * Declare an internal entity.
     *
     * @param name The entity name
     * @param value The entity value
     */
    public void setInternalEntity(String name, String value) {
        entityHash.put(name, Integer.valueOf(XmlConstants.ENTITY_INTERNAL));
        entityInfo.put(name + ".value", value);

        logger.log(Level.FINE, "Declared internal entity: " + name + " = " + value);
    }

    /**
     * Declare an external entity.
     *
     * @param name The entity name
     * @param publicId The public ID (can be null)
     * @param systemId The system ID
     */
    public void setExternalEntity(String name, String publicId, String systemId) {
        entityHash.put(name, Integer.valueOf(XmlConstants.ENTITY_TEXT));
        if (publicId != null) {
            entityInfo.put(name + ".publicId", publicId);
        }
        entityInfo.put(name + ".systemId", systemId);

        logger.log(Level.FINE, "Declared external entity: " + name +
                  " publicId=" + publicId + " systemId=" + systemId);
    }

    /**
     * Declare an external entity with notation (NDATA).
     *
     * @param name The entity name
     * @param publicId The public ID (can be null)
     * @param systemId The system ID
     * @param notationName The notation name
     */
    public void setExternalDataEntity(String name, String publicId, String systemId, String notationName) {
        entityHash.put(name, Integer.valueOf(XmlConstants.ENTITY_NDATA));
        if (publicId != null) {
            entityInfo.put(name + ".publicId", publicId);
        }
        entityInfo.put(name + ".systemId", systemId);
        entityInfo.put(name + ".notationName", notationName);

        logger.log(Level.FINE, "Declared external data entity: " + name +
                  " notation=" + notationName);
    }

    /**
     * Get the type of an entity.
     *
     * @param name The entity name
     * @return The entity type constant
     */
    public int getEntityType(String name) {
        Integer type = entityHash.get(name);
        return type != null ? type.intValue() : XmlConstants.ENTITY_UNDECLARED;
    }

    /**
     * Get the public ID of an entity.
     *
     * @param name The entity name
     * @return The public ID, or null if not available
     */
    public String getEntityPublicId(String name) {
        return entityInfo.get(name + ".publicId");
    }

    /**
     * Get the system ID of an entity.
     *
     * @param name The entity name
     * @return The system ID, or null if not available
     */
    public String getEntitySystemId(String name) {
        return entityInfo.get(name + ".systemId");
    }

    /**
     * Get the value of an internal entity.
     *
     * @param name The entity name
     * @return The entity value, or null if not available
     */
    public String getEntityValue(String name) {
        return entityInfo.get(name + ".value");
    }

    /**
     * Get the notation name of an NDATA entity.
     *
     * @param name The entity name
     * @return The notation name, or null if not available
     */
    public String getEntityNotationName(String name) {
        return entityInfo.get(name + ".notationName");
    }

    /**
     * Check if an entity is declared.
     *
     * @param name The entity name
     * @return true if the entity is declared
     */
    public boolean isEntityDeclared(String name) {
        return entityHash.containsKey(name);
    }

    /**
     * Get an enumeration of all declared entity names.
     *
     * @return Enumeration of entity names
     */
    public Enumeration<String> declaredEntities() {
        return entityHash.keys();
    }

    /**
     * Declare a notation.
     *
     * @param name The notation name
     * @param publicId The public ID (can be null)
     * @param systemId The system ID (can be null)
     */
    public void setNotation(String name, String publicId, String systemId) {
        String[] notation = new String[2];
        notation[0] = publicId;
        notation[1] = systemId;
        notationHash.put(name, notation);

        logger.log(Level.FINE, "Declared notation: " + name +
                  " publicId=" + publicId + " systemId=" + systemId);
    }

    /**
     * Get the public ID of a notation.
     *
     * @param name The notation name
     * @return The public ID, or null if not available
     */
    public String getNotationPublicId(String name) {
        String[] notation = notationHash.get(name);
        return notation != null ? notation[0] : null;
    }

    /**
     * Get the system ID of a notation.
     *
     * @param name The notation name
     * @return The system ID, or null if not available
     */
    public String getNotationSystemId(String name) {
        String[] notation = notationHash.get(name);
        return notation != null ? notation[1] : null;
    }

    /**
     * Check if a notation is declared.
     *
     * @param name The notation name
     * @return true if the notation is declared
     */
    public boolean isNotationDeclared(String name) {
        return notationHash.containsKey(name);
    }

    /**
     * Get an enumeration of all declared notation names.
     *
     * @return Enumeration of notation names
     */
    public Enumeration<String> declaredNotations() {
        return notationHash.keys();
    }

    /**
     * Clear all entity and notation declarations.
     */
    public void clear() {
        entityHash.clear();
        entityInfo.clear();
        notationHash.clear();

        logger.log(Level.FINE, "Cleared all entity and notation declarations");
    }

    /**
     * Get entity count for debugging purposes.
     *
     * @return The number of declared entities
     */
    public int getEntityCount() {
        return entityHash.size();
    }

    /**
     * Get notation count for debugging purposes.
     *
     * @return The number of declared notations
     */
    public int getNotationCount() {
        return notationHash.size();
    }
}