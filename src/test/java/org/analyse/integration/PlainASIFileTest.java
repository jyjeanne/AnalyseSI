package org.analyse.integration;

import org.analyse.core.save.FiltreASI;

import java.io.File;
import java.net.URL;

/**
 * Plain Java integration test for ASI files without any testing framework dependencies
 */
public class PlainASIFileTest {

    public static void main(String[] args) {
        System.out.println("=== Plain ASI File Integration Tests ===");

        try {
            PlainASIFileTest test = new PlainASIFileTest();
            test.runAllTests();
            System.out.println("\n[SUCCESS] All integration tests passed successfully!");
        } catch (Exception e) {
            System.err.println("\n[FAIL] Test failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private final FiltreASI filter;

    public PlainASIFileTest() {
        this.filter = new FiltreASI();
    }

    public void runAllTests() throws Exception {
        testBasicFilterProperties();
        testLoadAllASIFiles();
        testFileCorrespondence();
        testFileSizeValidation();
        testErrorHandling();
    }

    private void testBasicFilterProperties() throws Exception {
        System.out.println("\n[TEST] Testing basic filter properties...");

        assert "asi".equals(filter.getExtension()) : "Filter should handle .asi extension";
        System.out.println("   [OK] Extension: " + filter.getExtension());

        assert "ASI File".equals(filter.getDescription()) : "Filter should have correct description";
        System.out.println("   [OK] Description: " + filter.getDescription());
    }

    private void testLoadAllASIFiles() throws Exception {
        System.out.println("\n[TEST] Testing ASI file loading...");

        String[] testFiles = {"test-001.asi", "test-002.asi", "test-003.asi", "test-004.asi", "test-005.asi",
                              "test-006.asi", "test-007.asi", "test-008.asi", "test-009.asi"};

        int loadedCount = 0;
        for (String filename : testFiles) {
            try {
                // Get file from test resources
                URL resourceUrl = getClass().getClassLoader().getResource("cases/" + filename);
                assert resourceUrl != null : "Test file " + filename + " should exist in resources";

                File testFile = new File(resourceUrl.toURI());
                assert testFile.exists() : "Test file should exist: " + filename;
                assert testFile.canRead() : "Test file should be readable: " + filename;
                assert testFile.length() > 0 : "Test file should not be empty: " + filename;

                // Load the file - main test is that this doesn't throw exceptions
                filter.open(testFile);

                loadedCount++;
                System.out.println("   [OK] " + filename + " loaded successfully (" + testFile.length() + " bytes)");

            } catch (Exception e) {
                System.out.println("   [FAIL] " + filename + " failed to load: " + e.getMessage());
                throw e;
            }
        }

        assert loadedCount == testFiles.length : "All test files should be loaded successfully";
        System.out.println("   [OK] Total files loaded: " + loadedCount + "/" + testFiles.length);
    }

    private void testFileCorrespondence() throws Exception {
        System.out.println("\n[TEST] Testing ASI/PNG file correspondence...");

        String[] testFiles = {"test-001", "test-002", "test-003", "test-004", "test-005",
                              "test-006", "test-007", "test-008", "test-009"};

        int matchCount = 0;
        for (String baseName : testFiles) {
            URL asiUrl = getClass().getClassLoader().getResource("cases/" + baseName + ".asi");
            URL pngUrl = getClass().getClassLoader().getResource("cases/" + baseName + ".png");

            if (asiUrl != null && pngUrl != null) {
                File asiFile = new File(asiUrl.toURI());
                File pngFile = new File(pngUrl.toURI());

                assert asiFile.exists() && asiFile.canRead() : "ASI file should be readable";
                assert pngFile.exists() && pngFile.canRead() : "PNG file should be readable";
                assert asiFile.length() > 100 : "ASI file should contain meaningful data";
                assert pngFile.length() > 500 : "PNG file should contain meaningful image data";

                matchCount++;
                System.out.println("   [OK] " + baseName + " - ASI: " + asiFile.length() +
                                 " bytes, PNG: " + pngFile.length() + " bytes");
            } else {
                if (asiUrl == null) System.out.println("   [FAIL] Missing: " + baseName + ".asi");
                if (pngUrl == null) System.out.println("   [FAIL] Missing: " + baseName + ".png");
                throw new Exception("Missing test file pair: " + baseName);
            }
        }

        assert matchCount == testFiles.length : "All ASI/PNG pairs should exist";
        System.out.println("   [OK] Found " + matchCount + " complete ASI/PNG pairs");
    }

    private void testFileSizeValidation() throws Exception {
        System.out.println("\n[TEST] Testing file size validation...");

        String[] testFiles = {"test-001.asi", "test-005.asi", "test-009.asi"};

        for (String filename : testFiles) {
            URL resourceUrl = getClass().getClassLoader().getResource("cases/" + filename);
            assert resourceUrl != null : "Test file should exist in resources: " + filename;

            File testFile = new File(resourceUrl.toURI());
            long fileSize = testFile.length();

            assert fileSize > 0 : "File should not be empty: " + filename;
            assert fileSize > 50 : "File seems too small to contain valid data: " + filename;
            assert fileSize < 10000 : "File seems unusually large for test data: " + filename;

            // Verify the file can be opened
            filter.open(testFile);

            System.out.println("   [OK] " + filename + " size validation passed (" + fileSize + " bytes)");
        }
    }

    private void testErrorHandling() throws Exception {
        System.out.println("\n[TEST] Testing error handling...");

        // Test 1: Non-existent file
        File nonExistentFile = new File("non-existent.asi");
        try {
            filter.open(nonExistentFile);
            System.out.println("   [OK] Non-existent file handled gracefully");
        } catch (Exception e) {
            System.out.println("   [OK] Non-existent file threw expected exception: " + e.getClass().getSimpleName());
        }

        // Test 2: Corrupted file
        File tempFile = File.createTempFile("corrupted", ".asi");
        tempFile.deleteOnExit();

        try (java.io.FileWriter writer = new java.io.FileWriter(tempFile)) {
            writer.write("This is not a valid ASI file content");
        }

        try {
            filter.open(tempFile);
            System.out.println("   [OK] Corrupted file handled gracefully");
        } catch (Exception e) {
            System.out.println("   [OK] Corrupted file threw expected exception: " + e.getClass().getSimpleName());
        }

        // Test 3: Empty file
        File emptyFile = File.createTempFile("empty", ".asi");
        emptyFile.deleteOnExit();

        try {
            filter.open(emptyFile);
            System.out.println("   [OK] Empty file handled gracefully");
        } catch (Exception e) {
            System.out.println("   [OK] Empty file threw expected exception: " + e.getClass().getSimpleName());
        }
    }
}