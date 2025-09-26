package org.analyse.xml;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XmlParserTest {

    private XmlParser parser;
    private TestXmlHandler handler;

    @BeforeEach
    void setUp() {
        parser = new XmlParser();
        handler = new TestXmlHandler();
    }

    @Nested
    @DisplayName("Parser Configuration Tests")
    class ParserConfigurationTests {

        @Test
        @DisplayName("Should create parser without handler")
        void shouldCreateParserWithoutHandler() {
            assertNotNull(parser);
        }

        @Test
        @DisplayName("Should set handler correctly")
        void shouldSetHandlerCorrectly() {
            parser.setHandler(handler);
            assertDoesNotThrow(() -> parser.setHandler(handler));
        }

        @Test
        @DisplayName("Should accept null handler")
        void shouldAcceptNullHandler() {
            assertDoesNotThrow(() -> parser.setHandler(null));
        }
    }

    @Nested
    @DisplayName("Simple XML Parsing Tests")
    class SimpleXmlParsingTests {

        @BeforeEach
        void setUpHandler() {
            parser.setHandler(handler);
        }

        @Test
        @DisplayName("Should parse empty XML document")
        void shouldParseEmptyXmlDocument() throws Exception {
            String xml = "<?xml version=\"1.0\"?><root/>";
            StringReader reader = new StringReader(xml);

            parser.parse("test", null, reader);

            assertTrue(handler.startDocumentCalled, "startDocument should be called");
            assertTrue(handler.elements.contains("root"), "root element should be parsed");
            assertTrue(handler.endDocumentCalled, "endDocument should be called");
        }

        @Test
        @DisplayName("Should parse XML with simple elements")
        void shouldParseXmlWithSimpleElements() throws Exception {
            String xml = "<?xml version=\"1.0\"?>" +
                        "<root>" +
                        "<child>content</child>" +
                        "</root>";
            StringReader reader = new StringReader(xml);

            parser.parse("test", null, reader);

            assertTrue(handler.elements.contains("root"), "root element should be parsed");
            assertTrue(handler.elements.contains("child"), "child element should be parsed");
            assertTrue(handler.charDataCalled, "character data should be parsed");
        }
    }

    @Nested
    @DisplayName("XML Attributes Tests")
    class XmlAttributesTests {

        @BeforeEach
        void setUpHandler() {
            parser.setHandler(handler);
        }

        @Test
        @DisplayName("Should parse element with attributes")
        void shouldParseElementWithAttributes() throws Exception {
            String xml = "<?xml version=\"1.0\"?>" +
                        "<root id=\"123\" name=\"test\"/>";
            StringReader reader = new StringReader(xml);

            parser.parse("test", null, reader);

            assertTrue(handler.elements.contains("root"), "root element should be parsed");
            assertTrue(handler.attributes.containsKey("id"), "id attribute should be parsed");
            assertTrue(handler.attributes.containsKey("name"), "name attribute should be parsed");
            assertEquals("123", handler.attributes.get("id"), "id attribute value should be correct");
            assertEquals("test", handler.attributes.get("name"), "name attribute value should be correct");
        }

        @Test
        @DisplayName("Should handle empty attributes")
        void shouldHandleEmptyAttributes() throws Exception {
            String xml = "<?xml version=\"1.0\"?>" +
                        "<root attr=\"\"/>";
            StringReader reader = new StringReader(xml);

            parser.parse("test", null, reader);

            assertTrue(handler.elements.contains("root"), "root element should be parsed");
            assertTrue(handler.attributes.containsKey("attr"), "empty attribute should be parsed");
            assertEquals("", handler.attributes.get("attr"), "empty attribute value should be correct");
        }
    }

    @Nested
    @DisplayName("Nested Elements Tests")
    class NestedElementsTests {

        @BeforeEach
        void setUpHandler() {
            parser.setHandler(handler);
        }

        @Test
        @DisplayName("Should parse deeply nested elements")
        void shouldParseDeeplyNestedElements() throws Exception {
            String xml = "<?xml version=\"1.0\"?>" +
                        "<level1>" +
                        "  <level2>" +
                        "    <level3>" +
                        "      <level4>deep content</level4>" +
                        "    </level3>" +
                        "  </level2>" +
                        "</level1>";
            StringReader reader = new StringReader(xml);

            parser.parse("test", null, reader);

            assertTrue(handler.elements.contains("level1"), "level1 should be parsed");
            assertTrue(handler.elements.contains("level2"), "level2 should be parsed");
            assertTrue(handler.elements.contains("level3"), "level3 should be parsed");
            assertTrue(handler.elements.contains("level4"), "level4 should be parsed");
            assertTrue(handler.charDataCalled, "character data should be parsed");
        }
    }

    @Nested
    @DisplayName("Special Characters Tests")
    class SpecialCharactersTests {

        @BeforeEach
        void setUpHandler() {
            parser.setHandler(handler);
        }

        @Test
        @DisplayName("Should handle XML entities")
        void shouldHandleXmlEntities() throws Exception {
            String xml = "<?xml version=\"1.0\"?>" +
                        "<root>&lt;&gt;&amp;&quot;&apos;</root>";
            StringReader reader = new StringReader(xml);

            parser.parse("test", null, reader);

            assertTrue(handler.elements.contains("root"), "root element should be parsed");
            assertTrue(handler.charDataCalled, "character data with entities should be parsed");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @BeforeEach
        void setUpHandler() {
            parser.setHandler(handler);
        }

        @Test
        @DisplayName("Should throw exception for malformed XML")
        void shouldThrowExceptionForMalformedXml() throws Exception {
            String xml = "<?xml version=\"1.0\"?>" +
                        "<root><unclosed>";
            StringReader reader = new StringReader(xml);

            assertThrows(Exception.class, () ->
                parser.parse("test", null, reader)
            );
        }


    }

    @Nested
    @DisplayName("Reader and InputStream Tests")
    class ReaderAndInputStreamTests {

        @BeforeEach
        void setUpHandler() {
            parser.setHandler(handler);
        }

        @Test
        @DisplayName("Should parse from Reader")
        void shouldParseFromReader() throws Exception {
            String xml = "<?xml version=\"1.0\"?><root>test</root>";
            StringReader reader = new StringReader(xml);

            parser.parse("test", null, reader);

            assertTrue(handler.startDocumentCalled, "startDocument should be called");
            assertTrue(handler.elements.contains("root"), "root element should be parsed");
            assertTrue(handler.endDocumentCalled, "endDocument should be called");
        }


    }

    @Nested
    @DisplayName("Processing Instructions Tests")
    class ProcessingInstructionsTests {

        @BeforeEach
        void setUpHandler() {
            parser.setHandler(handler);
        }

        @Test
        @DisplayName("Should handle processing instructions")
        void shouldHandleProcessingInstructions() throws Exception {
            String xml = "<?xml version=\"1.0\"?>" +
                        "<?xml-stylesheet type=\"text/xsl\" href=\"style.xsl\"?>" +
                        "<root/>";
            StringReader reader = new StringReader(xml);

            parser.parse("test", null, reader);

            assertTrue(handler.elements.contains("root"), "root element should be parsed");
            assertTrue(handler.processingInstructionCalled, "processing instruction should be called");
        }
    }

    /**
     * Test implementation of XmlHandler that records parsing events
     */
    private static class TestXmlHandler implements XmlHandler {
        boolean startDocumentCalled = false;
        boolean endDocumentCalled = false;
        boolean charDataCalled = false;
        boolean processingInstructionCalled = false;

        List<String> elements = new ArrayList<>();
        java.util.Map<String, String> attributes = new java.util.HashMap<>();

        @Override
        public void startDocument() throws java.lang.Exception {
            startDocumentCalled = true;
        }

        @Override
        public void endDocument() throws java.lang.Exception {
            endDocumentCalled = true;
        }

        @Override
        public Object resolveEntity(String publicId, String systemId) throws java.lang.Exception {
            return null;
        }

        @Override
        public void startExternalEntity(String systemId) throws java.lang.Exception {
            // No-op for test
        }

        @Override
        public void endExternalEntity(String systemId) throws java.lang.Exception {
            // No-op for test
        }

        @Override
        public void doctypeDecl(String name, String publicId, String systemId) throws java.lang.Exception {
            // No-op for test
        }

        @Override
        public void attribute(String aname, String value, boolean isSpecified) throws java.lang.Exception {
            attributes.put(aname, value);
        }

        @Override
        public void startElement(String elname) throws java.lang.Exception {
            elements.add(elname);
        }

        @Override
        public void endElement(String elname) throws java.lang.Exception {
            // Elements are recorded in startElement
        }

        @Override
        public void charData(char[] ch, int start, int length) throws java.lang.Exception {
            charDataCalled = true;
        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws java.lang.Exception {
            // No-op for test
        }

        @Override
        public void processingInstruction(String target, String data) throws java.lang.Exception {
            processingInstructionCalled = true;
        }

        @Override
        public void error(String message, String systemId, int line, int column) throws java.lang.Exception {
            throw new Exception("XML Parse Error: " + message + " at line " + line + ", column " + column);
        }
    }
}