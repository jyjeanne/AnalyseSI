package org.analyse.xml;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class XmlParserTest {

    private XmlParser parser;
    private XmlHandler mockHandler;

    @BeforeEach
    void setUp() {
        parser = new XmlParser();
        mockHandler = mock(XmlHandler.class);
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
            parser.setHandler(mockHandler);
            assertDoesNotThrow(() -> parser.setHandler(mockHandler));
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
            parser.setHandler(mockHandler);
        }

        @Test
        @DisplayName("Should parse empty XML document")
        void shouldParseEmptyXmlDocument(@TempDir Path tempDir) throws Exception {
            Path xmlFile = tempDir.resolve("empty.xml");
            Files.writeString(xmlFile, "<?xml version=\"1.0\"?><root/>");

            parser.parse(xmlFile.toUri().toString(), null, "UTF-8");

            verify(mockHandler, atLeastOnce()).startDocument();
            verify(mockHandler, atLeastOnce()).startElement(eq("root"));
            verify(mockHandler, atLeastOnce()).endElement(eq("root"));
            verify(mockHandler, atLeastOnce()).endDocument();
        }

        @Test
        @DisplayName("Should parse XML with simple elements")
        void shouldParseXmlWithSimpleElements(@TempDir Path tempDir) throws Exception {
            Path xmlFile = tempDir.resolve("simple.xml");
            String xml = "<?xml version=\"1.0\"?>" +
                        "<root>" +
                        "<child>content</child>" +
                        "</root>";
            Files.writeString(xmlFile, xml);

            parser.parse(xmlFile.toUri().toString(), null, "UTF-8");

            verify(mockHandler, times(1)).startElement("root");
            verify(mockHandler, times(1)).startElement("child");
            verify(mockHandler, atLeastOnce()).charData(any(char[].class), anyInt(), anyInt());
            verify(mockHandler, times(1)).endElement("child");
            verify(mockHandler, times(1)).endElement("root");
        }
    }

    @Nested
    @DisplayName("XML Attributes Tests")
    class XmlAttributesTests {

        @BeforeEach
        void setUpHandler() {
            parser.setHandler(mockHandler);
        }

        @Test
        @DisplayName("Should parse element with attributes")
        void shouldParseElementWithAttributes(@TempDir Path tempDir) throws Exception {
            Path xmlFile = tempDir.resolve("attributes.xml");
            String xml = "<?xml version=\"1.0\"?>" +
                        "<root id=\"123\" name=\"test\"/>";
            Files.writeString(xmlFile, xml);

            parser.parse(xmlFile.toUri().toString(), null, "UTF-8");

            verify(mockHandler, atLeastOnce()).startElement(eq("root"));
            verify(mockHandler, atLeastOnce()).attribute(eq("id"), eq("123"), eq(false));
            verify(mockHandler, atLeastOnce()).attribute(eq("name"), eq("test"), eq(false));
        }

        @Test
        @DisplayName("Should handle empty attributes")
        void shouldHandleEmptyAttributes(@TempDir Path tempDir) throws Exception {
            Path xmlFile = tempDir.resolve("empty_attrs.xml");
            String xml = "<?xml version=\"1.0\"?>" +
                        "<root attr=\"\"/>";
            Files.writeString(xmlFile, xml);

            parser.parse(xmlFile.toUri().toString(), null, "UTF-8");

            verify(mockHandler, atLeastOnce()).startElement(eq("root"));
            verify(mockHandler, atLeastOnce()).attribute(eq("attr"), eq(""), eq(false));
        }
    }

    @Nested
    @DisplayName("Nested Elements Tests")
    class NestedElementsTests {

        @BeforeEach
        void setUpHandler() {
            parser.setHandler(mockHandler);
        }

        @Test
        @DisplayName("Should parse deeply nested elements")
        void shouldParseDeeplyNestedElements(@TempDir Path tempDir) throws Exception {
            Path xmlFile = tempDir.resolve("nested.xml");
            String xml = "<?xml version=\"1.0\"?>" +
                        "<level1>" +
                        "  <level2>" +
                        "    <level3>" +
                        "      <level4>deep content</level4>" +
                        "    </level3>" +
                        "  </level2>" +
                        "</level1>";
            Files.writeString(xmlFile, xml);

            parser.parse(xmlFile.toUri().toString(), null, "UTF-8");

            verify(mockHandler, times(1)).startElement("level1");
            verify(mockHandler, times(1)).startElement("level2");
            verify(mockHandler, times(1)).startElement("level3");
            verify(mockHandler, times(1)).startElement("level4");
            verify(mockHandler, times(1)).endElement("level4");
            verify(mockHandler, times(1)).endElement("level3");
            verify(mockHandler, times(1)).endElement("level2");
            verify(mockHandler, times(1)).endElement("level1");
        }
    }

    @Nested
    @DisplayName("Special Characters Tests")
    class SpecialCharactersTests {

        @BeforeEach
        void setUpHandler() {
            parser.setHandler(mockHandler);
        }

        @Test
        @DisplayName("Should handle XML entities")
        void shouldHandleXmlEntities(@TempDir Path tempDir) throws Exception {
            Path xmlFile = tempDir.resolve("entities.xml");
            String xml = "<?xml version=\"1.0\"?>" +
                        "<root>&lt;&gt;&amp;&quot;&apos;</root>";
            Files.writeString(xmlFile, xml);

            parser.parse(xmlFile.toUri().toString(), null, "UTF-8");

            verify(mockHandler, atLeastOnce()).charData(any(char[].class), anyInt(), anyInt());
        }

        @Test
        @DisplayName("Should handle CDATA sections")
        void shouldHandleCdataSections(@TempDir Path tempDir) throws Exception {
            Path xmlFile = tempDir.resolve("cdata.xml");
            String xml = "<?xml version=\"1.0\"?>" +
                        "<root><![CDATA[<special>content</special>]]></root>";
            Files.writeString(xmlFile, xml);

            parser.parse(xmlFile.toUri().toString(), null, "UTF-8");

            verify(mockHandler, atLeastOnce()).startElement("root");
            verify(mockHandler, atLeastOnce()).charData(any(char[].class), anyInt(), anyInt());
            verify(mockHandler, atLeastOnce()).endElement("root");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @BeforeEach
        void setUpHandler() {
            parser.setHandler(mockHandler);
        }

        @Test
        @DisplayName("Should throw exception for malformed XML")
        void shouldThrowExceptionForMalformedXml(@TempDir Path tempDir) throws Exception {
            Path xmlFile = tempDir.resolve("malformed.xml");
            String xml = "<?xml version=\"1.0\"?>" +
                        "<root><unclosed>";
            Files.writeString(xmlFile, xml);

            assertThrows(Exception.class, () ->
                parser.parse(xmlFile.toUri().toString(), null, "UTF-8")
            );
        }

        @Test
        @DisplayName("Should handle parsing without handler")
        void shouldHandleParsingWithoutHandler(@TempDir Path tempDir) throws Exception {
            parser.setHandler(null);
            Path xmlFile = tempDir.resolve("test.xml");
            Files.writeString(xmlFile, "<?xml version=\"1.0\"?><root/>");

            assertThrows(Exception.class, () ->
                parser.parse(xmlFile.toUri().toString(), null, "UTF-8")
            );
        }
    }

    @Nested
    @DisplayName("Reader and InputStream Tests")
    class ReaderAndInputStreamTests {

        @BeforeEach
        void setUpHandler() {
            parser.setHandler(mockHandler);
        }

        @Test
        @DisplayName("Should parse from Reader")
        void shouldParseFromReader() throws Exception {
            String xml = "<?xml version=\"1.0\"?><root>test</root>";
            StringReader reader = new StringReader(xml);

            parser.parse(null, null, reader);

            verify(mockHandler, atLeastOnce()).startDocument();
            verify(mockHandler, atLeastOnce()).startElement("root");
            verify(mockHandler, atLeastOnce()).endElement("root");
            verify(mockHandler, atLeastOnce()).endDocument();
        }

        @Test
        @DisplayName("Should parse from InputStream")
        void shouldParseFromInputStream() throws Exception {
            String xml = "<?xml version=\"1.0\"?><root>test</root>";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));

            parser.parse(null, null, inputStream, "UTF-8");

            verify(mockHandler, atLeastOnce()).startDocument();
            verify(mockHandler, atLeastOnce()).startElement("root");
            verify(mockHandler, atLeastOnce()).endElement("root");
            verify(mockHandler, atLeastOnce()).endDocument();
        }
    }

    @Nested
    @DisplayName("Comments and Processing Instructions Tests")
    class CommentsAndProcessingInstructionsTests {

        @BeforeEach
        void setUpHandler() {
            parser.setHandler(mockHandler);
        }

        @Test
        @DisplayName("Should handle XML comments")
        void shouldHandleXmlComments(@TempDir Path tempDir) throws Exception {
            Path xmlFile = tempDir.resolve("comments.xml");
            String xml = "<?xml version=\"1.0\"?>" +
                        "<!-- This is a comment -->" +
                        "<root><!-- Another comment -->content</root>";
            Files.writeString(xmlFile, xml);

            parser.parse(xmlFile.toUri().toString(), null, "UTF-8");

            verify(mockHandler, atLeastOnce()).startElement("root");
            verify(mockHandler, atLeastOnce()).endElement("root");
        }

        @Test
        @DisplayName("Should handle processing instructions")
        void shouldHandleProcessingInstructions(@TempDir Path tempDir) throws Exception {
            Path xmlFile = tempDir.resolve("pi.xml");
            String xml = "<?xml version=\"1.0\"?>" +
                        "<?xml-stylesheet type=\"text/xsl\" href=\"style.xsl\"?>" +
                        "<root/>";
            Files.writeString(xmlFile, xml);

            parser.parse(xmlFile.toUri().toString(), null, "UTF-8");

            verify(mockHandler, atLeastOnce()).processingInstruction(eq("xml-stylesheet"),
                eq("type=\"text/xsl\" href=\"style.xsl\""));
        }
    }
}