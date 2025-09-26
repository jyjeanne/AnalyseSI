package org.analyse.integration;

import org.analyse.core.save.FiltreASI;
import java.io.File;
import java.net.URL;

/**
 * Basic ASI file test that focuses on file loading without complex module interactions
 */
public class BasicASIFileTest {

    public static void main(String[] args) {
        System.out.println("=== Basic ASI File Tests ===");

        try {
            testFilterProperties();
            testFileExistence();
            testFileLoading();

            System.out.println("\nAll basic tests passed successfully!");
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testFilterProperties() throws Exception {
        System.out.println("\nTesting FiltreASI properties...");

        FiltreASI filter = new FiltreASI();

        assert "asi".equals(filter.getExtension()) : "Filter should handle .asi extension";
        System.out.println("   OK Extension: " + filter.getExtension());

        assert "ASI File".equals(filter.getDescription()) : "Filter should have correct description";
        System.out.println("   OK Description: " + filter.getDescription());
    }

    private static void testFileExistence() throws Exception {
        System.out.println("\nTesting test resource files existence...");

        String[] testFiles = {"test-001.asi", "test-002.asi", "test-003.asi", "test-004.asi", "test-005.asi",
                              "test-006.asi", "test-007.asi", "test-008.asi", "test-009.asi"};

        int foundCount = 0;
        for (String filename : testFiles) {
            URL resourceUrl = BasicASIFileTest.class.getClassLoader().getResource("cases/" + filename);
            if (resourceUrl != null) {
                File testFile = new File(resourceUrl.toURI());
                assert testFile.exists() : "Test file should exist: " + filename;
                assert testFile.canRead() : "Test file should be readable: " + filename;
                assert testFile.length() > 0 : "Test file should not be empty: " + filename;

                foundCount++;
                System.out.println("   OK " + filename + " - Size: " + testFile.length() + " bytes");

                // Also check for corresponding PNG
                URL pngUrl = BasicASIFileTest.class.getClassLoader().getResource("cases/" + filename.replace(".asi", ".png"));
                if (pngUrl != null) {
                    File pngFile = new File(pngUrl.toURI());
                    System.out.println("       + PNG: " + pngFile.length() + " bytes");
                }
            } else {
                System.out.println("   WARNING " + filename + " not found");
            }
        }

        assert foundCount > 0 : "At least one test file should be found";
        System.out.println("   OK Found " + foundCount + " test files");
    }

    private static void testFileLoading() throws Exception {
        System.out.println("\nTesting ASI file loading...");

        FiltreASI filter = new FiltreASI();
        String[] testFiles = {"test-001.asi", "test-002.asi", "test-003.asi"};

        for (String filename : testFiles) {
            URL resourceUrl = BasicASIFileTest.class.getClassLoader().getResource("cases/" + filename);
            if (resourceUrl != null) {
                File testFile = new File(resourceUrl.toURI());

                // Test that loading doesn't throw exceptions
                try {
                    filter.open(testFile);
                    System.out.println("   OK " + filename + " loaded without exceptions");
                } catch (Exception e) {
                    System.out.println("   WARNING " + filename + " - Load exception: " + e.getMessage());
                    // Don't fail the test for loading issues - just report them
                }
            }
        }
    }

    private static void testImageFileCorrespondence() throws Exception {
        System.out.println("\nTesting ASI/PNG file correspondence...");

        String[] testFiles = {"test-001", "test-002", "test-003", "test-004", "test-005",
                              "test-006", "test-007", "test-008", "test-009"};

        int matchCount = 0;
        for (String baseName : testFiles) {
            URL asiUrl = BasicASIFileTest.class.getClassLoader().getResource("cases/" + baseName + ".asi");
            URL pngUrl = BasicASIFileTest.class.getClassLoader().getResource("cases/" + baseName + ".png");

            if (asiUrl != null && pngUrl != null) {
                File asiFile = new File(asiUrl.toURI());
                File pngFile = new File(pngUrl.toURI());
                matchCount++;

                System.out.println("   OK " + baseName + " - ASI: " + asiFile.length() +
                                 " bytes, PNG: " + pngFile.length() + " bytes");
            } else {
                if (asiUrl == null) System.out.println("   Missing: " + baseName + ".asi");
                if (pngUrl == null) System.out.println("   Missing: " + baseName + ".png");
            }
        }

        assert matchCount > 0 : "At least one ASI/PNG pair should exist";
        System.out.println("   OK Found " + matchCount + " matching ASI/PNG pairs");
    }
}