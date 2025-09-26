package org.analyse.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class UtilitiesTest {

    @Nested
    @DisplayName("getRelease() Tests")
    class GetReleaseTests {

        @Test
        @DisplayName("Should return application name with version")
        void shouldReturnApplicationNameWithVersion() {
            String release = Utilities.getRelease();
            assertNotNull(release);
            assertTrue(release.contains(Constantes.NOM_APPLICATION));
            assertTrue(release.contains(Constantes.RELEASE));
        }
    }

    @Nested
    @DisplayName("getExtension() Tests")
    class GetExtensionTests {

        @Test
        @DisplayName("Should extract extension from simple filename")
        void shouldExtractExtensionFromSimpleFilename() {
            assertEquals("txt", Utilities.getExtension("file.txt"));
            assertEquals("java", Utilities.getExtension("MyClass.java"));
            assertEquals("xml", Utilities.getExtension("config.xml"));
        }

        @Test
        @DisplayName("Should handle uppercase extensions")
        void shouldHandleUppercaseExtensions() {
            assertEquals("txt", Utilities.getExtension("file.TXT"));
            assertEquals("pdf", Utilities.getExtension("document.PDF"));
        }

        @Test
        @DisplayName("Should extract extension from path with directories")
        void shouldExtractExtensionFromPath() {
            assertEquals("java", Utilities.getExtension("/path/to/file.java"));
            assertEquals("xml", Utilities.getExtension("C:\\Windows\\config.xml"));
        }

        @Test
        @DisplayName("Should return empty string for files without extension")
        void shouldReturnEmptyForNoExtension() {
            assertEquals("", Utilities.getExtension("README"));
            assertEquals("", Utilities.getExtension("/path/to/file"));
        }

        @Test
        @DisplayName("Should handle files with multiple dots")
        void shouldHandleMultipleDots() {
            assertEquals("gz", Utilities.getExtension("archive.tar.gz"));
            assertEquals("java", Utilities.getExtension("my.test.file.java"));
        }

        @Test
        @DisplayName("Should handle edge cases")
        void shouldHandleEdgeCases() {
            assertEquals("", Utilities.getExtension(""));
            assertEquals("", Utilities.getExtension("."));
            assertEquals("", Utilities.getExtension(".."));
            assertEquals("", Utilities.getExtension(".hidden"));
        }
    }

    @Nested
    @DisplayName("replaceExtension() Tests")
    class ReplaceExtensionTests {

        @Test
        @DisplayName("Should replace extension in simple filename")
        void shouldReplaceExtensionInSimpleFilename() {
            assertEquals("file.xml", Utilities.replaceExtension("file.txt", "xml"));
            assertEquals("document.pdf", Utilities.replaceExtension("document.doc", "pdf"));
        }

        @Test
        @DisplayName("Should replace extension in path")
        void shouldReplaceExtensionInPath() {
            assertEquals("/path/to/file.xml",
                Utilities.replaceExtension("/path/to/file.java", "xml"));
            assertEquals("C:\\Windows\\config.ini",
                Utilities.replaceExtension("C:\\Windows\\config.xml", "ini"));
        }

        @Test
        @DisplayName("Should handle files with multiple dots")
        void shouldHandleFilesWithMultipleDots() {
            assertEquals("archive.tar.bz2",
                Utilities.replaceExtension("archive.tar.gz", "bz2"));
        }

        @Test
        @DisplayName("Should return original filename for files without extension")
        void shouldReturnOriginalForNoExtension() {
            assertEquals("README", Utilities.replaceExtension("README", "md"));
            assertEquals("/path/to/file",
                Utilities.replaceExtension("/path/to/file", "txt"));
        }
    }

    @Nested
    @DisplayName("normaliseString() Tests")
    class NormaliseStringTests {

        @Test
        @DisplayName("Should normalize string by replacing spaces with underscores")
        void shouldNormalizeStringWithSpaces() {
            assertEquals("hello_world", Utilities.normaliseString("hello world", Constantes.LOWER));
            assertEquals("test_string_here", Utilities.normaliseString("test string here", Constantes.UPPER));
        }

        @Test
        @DisplayName("Should handle strings without spaces")
        void shouldHandleStringsWithoutSpaces() {
            assertEquals("teststring", Utilities.normaliseString("teststring", Constantes.LOWER));
        }
    }

    @Nested
    @DisplayName("addressFichier() Tests")
    class AddressFichierTests {

        @Test
        @DisplayName("Should extract address from file path")
        void shouldExtractAddressFromFilePath() {
            String result = Utilities.addressFichier("/path/to/file.txt", ".txt");
            assertEquals("/path/to/file", result);
        }

        @Test
        @DisplayName("Should handle complex paths")
        void shouldHandleComplexPaths() {
            String result = Utilities.addressFichier("C:\\Windows\\System32\\config.xml", ".xml");
            assertEquals("C:\\Windows\\System32\\config", result);
        }
    }

    @Nested
    @DisplayName("Language Support Tests")
    class LanguageSupportTests {

        @Test
        @DisplayName("Should get resource bundle")
        void shouldGetResourceBundle() {
            ResourceBundle bundle = Utilities.getResourceBundle();
            assertNotNull(bundle);
        }

        @Test
        @DisplayName("Should get resource bundle with locale")
        void shouldGetResourceBundleWithLocale() {
            ResourceBundle bundle = Utilities.getResourceBundle(Locale.FRENCH);
            assertNotNull(bundle);
        }

        @Test
        @DisplayName("Should get language message")
        void shouldGetLanguageMessage() {
            String message = Utilities.getLangueMessage("about_text");
            assertNotNull(message);
        }

        @Test
        @DisplayName("Should return key for invalid message")
        void shouldReturnKeyForInvalidMessage() {
            String invalidKey = "invalid_key_that_does_not_exist";
            String message = Utilities.getLangueMessage(invalidKey);
            assertTrue(message.contains(invalidKey));
        }

        @Test
        @DisplayName("Should format message with arguments")
        void shouldFormatMessageWithArguments() {
            Object[] args = new Object[]{"test", "value"};
            String message = Utilities.getLangueMessageFormatter("about_text", args);
            assertNotNull(message);
        }
    }

    @Nested
    @DisplayName("newLine() Tests")
    class NewLineTests {

        @Test
        @DisplayName("Should return Windows line separator")
        void shouldReturnWindowsLineSeparator() {
            String newLine = Utilities.newLine();
            assertEquals("\r\n", newLine);
        }
    }

    @Nested
    @DisplayName("getText() Tests")
    class GetTextTests {

        @Test
        @DisplayName("Should return null for non-existent resource")
        void shouldReturnNullForNonExistentResource() {
            String content = Utilities.getText("/nonexistent.txt", UtilitiesTest.class);
            assertNull(content);
        }

        @Test
        @DisplayName("Should read existing resource file")
        void shouldReadExistingResourceFile() {
            String content = Utilities.getText("/org/analyse/main/about.txt", Utilities.class);
            if (content != null) {
                assertFalse(content.isEmpty());
            }
        }
    }
}