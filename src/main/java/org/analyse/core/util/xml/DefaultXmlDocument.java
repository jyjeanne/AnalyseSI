/*
 * 05/28/2003 - 11:43:10
 * 
 * DefaultXmlDocument.java - Copyright (C) 2003 Dreux Loic dreuxl@free.fr
 * 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.analyse.core.util.xml;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DefaultXmlDocument
{
    private static final Logger logger = Logger.getLogger(DefaultXmlDocument.class.getName());
    private Document document;

    private File file;

    public DefaultXmlDocument(File file)
    {
        this.file = file;
    }

    public void load()
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
                public void fatalError(SAXParseException e) throws SAXException
                {
                    throw e;
                }

                public void error(SAXParseException e) throws SAXParseException
                {
                    throw e;
                }

                public void warning(SAXParseException err)
                        throws SAXParseException
                {
                    logger.log(Level.WARNING, "XML parsing warning at line " + err.getLineNumber() +
                            ", uri " + err.getSystemId() + ": " + err.getMessage());
                }
            });

            document = builder.parse(file);

        } catch (SAXParseException spe) {
            logger.log(Level.SEVERE, "XML parsing error at line " + spe.getLineNumber() +
                    ", uri " + spe.getSystemId() + ": " + spe.getMessage(), spe);

            Exception x = spe;
            if (spe.getException() != null)
                x = spe.getException();
            x.printStackTrace();

        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null)
                x = sxe.getException();
            x.printStackTrace();

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void save()
    {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error saving XML document", ex);
        }
    }

    public Document getDocument()
    {
        return document;
    }
}