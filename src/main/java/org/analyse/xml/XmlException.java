package org.analyse.xml;

/**
 * Exception pour les erreurs de parsing XML.
 * Version simplifiée et modernisée.
 */
public class XmlException extends Exception {

    private static final long serialVersionUID = 296196241316747669L;

    private final String systemId;
    private final int line;
    private final int column;

    public XmlException(String message, String systemId, int line, int column) {
        super(message);
        this.systemId = systemId;
        this.line = line;
        this.column = column;
    }

    public XmlException(String message) {
        this(message, null, -1, -1);
    }

    public String getSystemId() {
        return systemId;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("XmlException: ").append(getMessage());

        if (systemId != null) {
            sb.append(" (").append(systemId);
            if (line > 0) {
                sb.append(", ligne ").append(line);
                if (column > 0) {
                    sb.append(", colonne ").append(column);
                }
            }
            sb.append(")");
        }

        return sb.toString();
    }
}