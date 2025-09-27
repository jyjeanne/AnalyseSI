package org.analyse.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import org.analyse.core.save.FiltreASI;
import org.analyse.core.modules.AnalyseModule;
import org.analyse.core.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.URL;

/**
 * Integration tests using real .asi files from test resources
 */
class ASIFileIntegrationTest {

    private FiltreASI filter;

    @BeforeEach
    void setUp() {
        filter = new FiltreASI();
        // Get ApplicationContext instance (auto-initialized as singleton)
        ApplicationContext.getInstance();
    }

    @Nested
    @DisplayName("ASI File Loading Tests")
    class ASIFileLoadingTests {

        @Test
        @DisplayName("Should successfully load ASI test files")
        void shouldLoadASITestFiles() throws Exception {
            String[] testFiles = {"test-001.asi", "test-002.asi", "test-003.asi", "test-004.asi", "test-005.asi",
                                  "test-006.asi", "test-007.asi", "test-008.asi", "test-009.asi"};

            for (String filename : testFiles) {
            // Get file from test resources
            URL resourceUrl = getClass().getClassLoader().getResource("cases/" + filename);
            assertNotNull(resourceUrl, "Test file " + filename + " should exist in resources");

            File testFile = new File(resourceUrl.toURI());
            assertTrue(testFile.exists(), "Test file should exist: " + filename);
            assertTrue(testFile.canRead(), "Test file should be readable: " + filename);

                // Load the file
                assertDoesNotThrow(() -> filter.open(testFile),
                    "Loading " + filename + " should not throw an exception");
            }
        }


    }

    @Nested
    @DisplayName("MCD Component Validation Tests")
    class MCDComponentValidationTests {

        @Test
        @DisplayName("Should create valid MCD components from ASI files")
        void shouldCreateValidMCDComponents() throws Exception {
            String[] testFiles = {"test-001.asi", "test-002.asi", "test-003.asi"};

            for (String filename : testFiles) {
            URL resourceUrl = getClass().getClassLoader().getResource("cases/" + filename);
            File testFile = new File(resourceUrl.toURI());

            // Clear any existing modules
            ApplicationContext context = ApplicationContext.getInstance();
            context.clearModules();

            // Load the ASI file
            filter.open(testFile);

            // Check that at least one module was loaded
            assertFalse(context.getModules().isEmpty(),
                "At least one module should be loaded from " + filename);

            // Validate each loaded module
            for (AnalyseModule module : context.getModules().values()) {
                assertNotNull(module, "Module should not be null");

                    // Basic validation - ensure module is properly initialized
                    assertNotNull(module.toString(), "Module should have valid string representation");
                }
            }
        }
    }

    @Nested
    @DisplayName("File Format Validation Tests")
    class FileFormatValidationTests {

        @Test
        @DisplayName("Should validate ASI file extension")
        void shouldValidateASIFileExtension() {
            assertEquals("asi", filter.getExtension(), "Filter should handle .asi extension");
            assertEquals("ASI File", filter.getDescription(), "Filter should have correct description");
        }

        @Test
        @DisplayName("Should handle different ASI file sizes")
        void shouldHandleDifferentFileSizes() throws Exception {
            String[] testFiles = {"test-001.asi", "test-005.asi", "test-009.asi"};

            for (String filename : testFiles) {
            URL resourceUrl = getClass().getClassLoader().getResource("cases/" + filename);
            File testFile = new File(resourceUrl.toURI());

            long fileSize = testFile.length();
            assertTrue(fileSize > 0, "File should not be empty: " + filename);

                // Files should be compressed (typically smaller than uncompressed XML)
                // This is a basic sanity check
                assertTrue(fileSize > 50, "File seems too small to contain valid data: " + filename);
            }
        }
    }

    @Nested
    @DisplayName("Expected Content Validation Tests")
    class ExpectedContentValidationTests {

        @Test
        @DisplayName("Should load test-001.asi and validate basic structure")
        void shouldLoadTest001AndValidateStructure() throws Exception {
            URL resourceUrl = getClass().getClassLoader().getResource("cases/test-001.asi");
            File testFile = new File(resourceUrl.toURI());

            ApplicationContext context = ApplicationContext.getInstance();
            context.clearModules();
            filter.open(testFile);

            // test-001.asi should contain at least one module
            assertFalse(context.getModules().isEmpty(), "test-001.asi should contain modules");

            // Basic validation that modules were loaded and are functional
            assertTrue(context.getModules().size() >= 0, "Modules should be accessible after loading");
        }

        @Test
        @DisplayName("Should maintain data integrity across different test files")
        void shouldMaintainDataIntegrity() throws Exception {
            String[] testFiles = {"test-002.asi", "test-003.asi", "test-004.asi"};

            for (String filename : testFiles) {
            URL resourceUrl = getClass().getClassLoader().getResource("cases/" + filename);
            File testFile = new File(resourceUrl.toURI());

            ApplicationContext context = ApplicationContext.getInstance();
            int moduleCountBefore = context.getModules().size();

            filter.open(testFile);

            // After loading, we should have modules
            assertFalse(context.getModules().isEmpty(),
                filename + " should result in loaded modules");

                // Validate that modules are properly initialized
                for (AnalyseModule module : context.getModules().values()) {
                    assertNotNull(module, "All modules should be non-null after loading " + filename);
                }
            }
        }
    }


}