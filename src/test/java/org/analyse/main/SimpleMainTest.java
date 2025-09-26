package org.analyse.main;

import org.analyse.core.modules.AnalyseModule;
import org.analyse.core.modules.FilterModule;
import org.analyse.core.context.ApplicationContext;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Simple test class without JUnit to verify ApplicationContext functionality
 */
public class SimpleMainTest {

    /**
     * Simple test implementation of AnalyseModule for testing purposes
     */
    private static class TestModule extends AnalyseModule {
        private final String id;

        public TestModule(String id) {
            this.id = id;
        }

        @Override
        public String getName() {
            return "Test Module " + id;
        }

        @Override
        public String getAuthor() {
            return "Test Author";
        }

        @Override
        public String getID() {
            return id;
        }

        @Override
        public void clear() {
            // Test implementation
        }

        @Override
        public void initGUI(org.analyse.core.gui.AnalyseFrame analyseFrame) {
            // Test implementation
        }

        @Override
        public FilterModule getFiltre(String id) {
            return null;
        }
    }

    private static void resetApplicationContextSingleton() {
        try {
            Field instanceField = ApplicationContext.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception e) {
            // Ignore reflection errors in tests
        }
    }

    public static void main(String[] args) {
        System.out.println("Running simple tests for ApplicationContext refactoring...");

        try {
            // Skip Main class tests that require dependencies
            // testSettingsDirectory();
            // testUserPropsPath();
            testApplicationContextSingleton();
            testModuleAddition();
            testModuleRetrieval();
            testNullHandling();

            System.out.println("Core ApplicationContext tests passed!");
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testSettingsDirectory() {
        String expectedPath = System.getProperty("user.home") + File.separator + ".analyseSI" + File.separator;
        if (!expectedPath.equals(Main.SETTINGS_DIRECTORY)) {
            throw new RuntimeException("Settings directory test failed");
        }
        System.out.println("✓ Settings directory test passed");
    }

    private static void testUserPropsPath() {
        String expectedPath = Main.SETTINGS_DIRECTORY + "analyseSI.properties";
        if (!expectedPath.equals(Main.USER_PROPS)) {
            throw new RuntimeException("User props path test failed");
        }
        System.out.println("✓ User props path test passed");
    }

    private static void testApplicationContextSingleton() {
        // Reset singleton for clean test
        resetApplicationContextSingleton();

        ApplicationContext instance1 = ApplicationContext.getInstance();
        ApplicationContext instance2 = ApplicationContext.getInstance();

        if (instance1 != instance2) {
            throw new RuntimeException("ApplicationContext singleton test failed");
        }

        if (!instance1.isInitialized()) {
            instance1.initialize();
        }

        if (!instance1.isInitialized()) {
            throw new RuntimeException("ApplicationContext initialization test failed");
        }

        System.out.println("✓ ApplicationContext singleton test passed");
    }

    private static void testModuleAddition() {
        // Reset and get fresh instance
        resetApplicationContextSingleton();
        ApplicationContext context = ApplicationContext.getInstance();
        context.initialize();
        context.clearModules();

        TestModule testModule = new TestModule("test-module");
        context.addModule(testModule);

        if (context.getModules().size() != 1) {
            throw new RuntimeException("Module addition test failed - wrong size");
        }

        if (context.getModule("test-module") != testModule) {
            throw new RuntimeException("Module addition test failed - wrong module returned");
        }

        System.out.println("✓ Module addition test passed");
    }

    private static void testModuleRetrieval() {
        // Reset and get fresh instance
        resetApplicationContextSingleton();
        ApplicationContext context = ApplicationContext.getInstance();
        context.initialize();
        context.clearModules();

        TestModule testModule = new TestModule("test-module");
        context.addModule(testModule);

        // Test ApplicationContext.getModule directly
        AnalyseModule result = context.getModule("test-module");
        if (result != testModule) {
            throw new RuntimeException("Module retrieval via ApplicationContext.getModule test failed");
        }

        // Test non-existent module
        AnalyseModule nullResult = context.getModule("non-existent");
        if (nullResult != null) {
            throw new RuntimeException("Non-existent module test failed");
        }

        System.out.println("✓ Module retrieval test passed");
    }

    private static void testNullHandling() {
        // Reset and get fresh instance
        resetApplicationContextSingleton();
        ApplicationContext context = ApplicationContext.getInstance();
        context.initialize();
        context.clearModules();

        // Test null ID handling
        AnalyseModule nullResult1 = context.getModule(null);
        if (nullResult1 != null) {
            throw new RuntimeException("Null ID handling test failed");
        }

        // Test empty ID handling
        AnalyseModule nullResult2 = context.getModule("");
        if (nullResult2 != null) {
            throw new RuntimeException("Empty ID handling test failed");
        }

        // Test adding module with null ID
        TestModule nullIdModule = new TestModule(null);
        boolean exceptionThrown = false;
        try {
            context.addModule(nullIdModule);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        if (!exceptionThrown) {
            throw new RuntimeException("Null module ID validation test failed");
        }

        System.out.println("✓ Null handling test passed");
    }
}