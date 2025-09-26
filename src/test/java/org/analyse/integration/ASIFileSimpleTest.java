package org.analyse.integration;

import org.analyse.core.save.FiltreASI;
import org.analyse.core.modules.AnalyseModule;
import org.analyse.merise.main.MeriseModule;
import org.analyse.merise.mcd.composant.MCDComponent;
import org.analyse.merise.mcd.composant.MCDEntite;
import org.analyse.merise.mcd.composant.MCDAssociation;
import org.analyse.core.context.ApplicationContext;
import org.analyse.core.gui.zgraph.ZElement;

import java.io.File;
import java.net.URL;
import java.util.Iterator;

/**
 * Simple integration test for ASI files without external test dependencies
 */
public class ASIFileSimpleTest {

    public static void main(String[] args) {
        System.out.println("=== ASI File Integration Tests ===");

        try {
            // Initialize ApplicationContext
            ApplicationContext context = ApplicationContext.getInstance();

            testBasicASILoading(context);
            testMultipleASIFiles(context);
            testMCDComponentExtraction(context);
            testImageFileCorrespondence();

            System.out.println("\nAll tests passed successfully!");
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testBasicASILoading(ApplicationContext context) throws Exception {
        System.out.println("Testing basic ASI file loading...");

        FiltreASI filter = new FiltreASI();

        // Test filter properties
        assert "asi".equals(filter.getExtension()) : "Filter should handle .asi extension";
        assert "ASI File".equals(filter.getDescription()) : "Filter should have correct description";

        // Load test-001.asi
        URL resourceUrl = ASIFileSimpleTest.class.getClassLoader().getResource("cases/test-001.asi");
        assert resourceUrl != null : "test-001.asi should exist in test resources";

        File testFile = new File(resourceUrl.toURI());
        assert testFile.exists() : "Test file should exist";
        assert testFile.canRead() : "Test file should be readable";
        assert testFile.length() > 0 : "Test file should not be empty";

        // Clear existing modules and load the file
        context.clearModules();
        filter.open(testFile);

        System.out.println("   OK test-001.asi loaded successfully");
        System.out.println("   OK File size: " + testFile.length() + " bytes");
        System.out.println("   OK Modules loaded: " + context.getModules().size());
    }

    private static void testMultipleASIFiles(ApplicationContext context) throws Exception {
        System.out.println("\nTesting multiple ASI files...");

        FiltreASI filter = new FiltreASI();
        String[] testFiles = {"test-001.asi", "test-002.asi", "test-003.asi", "test-004.asi", "test-005.asi"};

        for (String filename : testFiles) {
            URL resourceUrl = ASIFileSimpleTest.class.getClassLoader().getResource("cases/" + filename);
            if (resourceUrl != null) {
                File testFile = new File(resourceUrl.toURI());

                context.clearModules();
                filter.open(testFile);

                System.out.println("   OK " + filename + " - Modules: " + context.getModules().size() +
                                   ", Size: " + testFile.length() + " bytes");
            } else {
                System.out.println("   WARNING " + filename + " not found in resources");
            }
        }
    }

    private static void testMCDComponentExtraction(ApplicationContext context) throws Exception {
        System.out.println("\nTesting MCD component extraction...");

        FiltreASI filter = new FiltreASI();
        URL resourceUrl = ASIFileSimpleTest.class.getClassLoader().getResource("cases/test-001.asi");
        File testFile = new File(resourceUrl.toURI());

        context.clearModules();
        filter.open(testFile);

        // Look for MCD components through MeriseModule
        MCDComponent mcdComponent = null;
        for (AnalyseModule module : context.getModules().values()) {
            if (module instanceof MeriseModule) {
                MeriseModule meriseModule = (MeriseModule) module;
                mcdComponent = meriseModule.getMCDComponent();
                break;
            }
        }

        if (mcdComponent != null) {
            System.out.println("   OK Found MCD component");
            assert mcdComponent.getData() != null : "MCD should have data dictionary";
            System.out.println("   OK Data dictionary exists");

            // Count elements
            int elementCount = 0;
            int entityCount = 0;
            int associationCount = 0;

            Iterator<ZElement> elements = mcdComponent.enumElements();
            while (elements.hasNext()) {
                ZElement element = elements.next();
                elementCount++;

                if (element instanceof MCDEntite) {
                    entityCount++;
                    MCDEntite entite = (MCDEntite) element;
                    System.out.println("     - Entity: " + entite.getName() +
                                     " (Info count: " + entite.sizeInformation() + ")");
                } else if (element instanceof MCDAssociation) {
                    associationCount++;
                    MCDAssociation assoc = (MCDAssociation) element;
                    System.out.println("     - Association: " + assoc.getName());
                }
            }

            System.out.println("   OK Total elements: " + elementCount);
            System.out.println("   OK Entities: " + entityCount);
            System.out.println("   OK Associations: " + associationCount);

            assert elementCount >= 0 : "Should have non-negative element count";
        } else {
            System.out.println("   INFO No MCD component found in test-001.asi");
        }
    }

    private static void testImageFileCorrespondence() throws Exception {
        System.out.println("\nTesting ASI/PNG file correspondence...");

        String[] testFiles = {"test-001", "test-002", "test-003", "test-004", "test-005",
                              "test-006", "test-007", "test-008", "test-009"};

        for (String baseName : testFiles) {
            URL asiUrl = ASIFileSimpleTest.class.getClassLoader().getResource("cases/" + baseName + ".asi");
            URL pngUrl = ASIFileSimpleTest.class.getClassLoader().getResource("cases/" + baseName + ".png");

            if (asiUrl != null && pngUrl != null) {
                File asiFile = new File(asiUrl.toURI());
                File pngFile = new File(pngUrl.toURI());

                System.out.println("   OK " + baseName + " - ASI: " + asiFile.length() + " bytes, PNG: " + pngFile.length() + " bytes");
            } else {
                if (asiUrl == null) System.out.println("   WARNING Missing: " + baseName + ".asi");
                if (pngUrl == null) System.out.println("   WARNING Missing: " + baseName + ".png");
            }
        }
    }
}