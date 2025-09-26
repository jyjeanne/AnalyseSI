package org.analyse.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.analyse.core.modules.AnalyseModule;
import org.analyse.core.modules.FilterModule;
import org.analyse.core.context.ApplicationContext;

import javax.swing.UIManager;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private ApplicationContext context;

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

    @BeforeEach
    void setUp() {
        // Reset ApplicationContext singleton pour les tests
        resetApplicationContextSingleton();

        // Get fresh instance
        context = ApplicationContext.getInstance();
        context.initialize();

        // Clear any existing modules
        context.clearModules();

        // Reset legacy static variables to null (still used for backward compatibility)
        Main.analyseFrame = null;
        Main.statusbar = null;
        Main.splash = null;
        Main.aboutWindow = null;
        Main.parametrageWindow = null;
        Main.globalActionCollection = null;
    }

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.shutdown();
        }
        resetApplicationContextSingleton();
    }

    /**
     * Reset the ApplicationContext singleton using reflection for test isolation.
     */
    private void resetApplicationContextSingleton() {
        try {
            Field instanceField = ApplicationContext.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception e) {
            // Ignore reflection errors in tests
        }
    }

    @Test
    void settingsDirectoryContainsCorrectUserHomePath() {
        String expectedPath = System.getProperty("user.home") + File.separator + ".analyseSI" + File.separator;
        assertEquals(expectedPath, Main.SETTINGS_DIRECTORY);
    }

    @Test
    void userPropsPathPointsToCorrectFile() {
        String expectedPath = Main.SETTINGS_DIRECTORY + "analyseSI.properties";
        assertEquals(expectedPath, Main.USER_PROPS);
    }

    @Test
    void getModuleReturnsCorrectModuleWhenExists() {
        TestModule testModule = new TestModule("test-module");

        // Add module via ApplicationContext
        context.addModule(testModule);

        AnalyseModule result = Main.getModule("test-module");

        assertEquals(testModule, result);
    }

    @Test
    void getModuleReturnsNullWhenModuleDoesNotExist() {
        AnalyseModule result = Main.getModule("non-existent-module");

        assertNull(result);
    }

    @Test
    void getModuleReturnsNullWhenModulesMapIsEmpty() {
        assertTrue(context.getModules().isEmpty());

        AnalyseModule result = Main.getModule("any-module");

        assertNull(result);
    }

    @Test
    void applicationContextInitializedCorrectly() {
        assertNotNull(context);
        assertTrue(context.isInitialized());
        assertNotNull(context.getModules());
        assertTrue(context.getModules() instanceof Map);
    }

    @Test
    void canAddMultipleModulesToApplicationContext() {
        TestModule module1 = new TestModule("module1");
        TestModule module2 = new TestModule("module2");

        context.addModule(module1);
        context.addModule(module2);

        assertEquals(2, context.getModules().size());
        assertEquals(module1, Main.getModule("module1"));
        assertEquals(module2, Main.getModule("module2"));
        assertEquals(module1, context.getModule("module1"));
        assertEquals(module2, context.getModule("module2"));
    }

    @Test
    void moduleCanBeOverwrittenInApplicationContext() {
        TestModule originalModule = new TestModule("same-id");
        TestModule newModule = new TestModule("same-id");

        context.addModule(originalModule);
        context.addModule(newModule);  // This should overwrite the first one

        assertEquals(1, context.getModules().size());
        assertEquals(newModule, Main.getModule("same-id"));
        assertEquals(newModule, context.getModule("same-id"));
        assertNotEquals(originalModule, Main.getModule("same-id"));
    }

    @Test
    void settingsDirectoryPathUsesSystemFileSeparator() {
        String expectedSeparator = File.separator;

        assertTrue(Main.SETTINGS_DIRECTORY.contains(expectedSeparator));
        assertTrue(Main.SETTINGS_DIRECTORY.endsWith(expectedSeparator));
    }

    @Test
    void userPropsFileHasCorrectExtension() {
        assertTrue(Main.USER_PROPS.endsWith(".properties"));
    }

    @Test
    void allDeprecatedStaticFieldsInitializeToNull() {
        assertNull(Main.analyseFrame);
        assertNull(Main.statusbar);
        assertNull(Main.splash);
        assertNull(Main.aboutWindow);
        assertNull(Main.parametrageWindow);
        assertNull(Main.globalActionCollection);
    }

    @Test
    void applicationContextSingletonBehavior() {
        ApplicationContext instance1 = ApplicationContext.getInstance();
        ApplicationContext instance2 = ApplicationContext.getInstance();

        assertSame(instance1, instance2);
        assertSame(context, instance1);
    }

    @Test
    void getModuleWithNullIdReturnsNull() {
        AnalyseModule result = Main.getModule(null);
        assertNull(result);
    }

    @Test
    void getModuleWithEmptyIdReturnsNull() {
        AnalyseModule result = Main.getModule("");
        assertNull(result);
    }

    @Test
    void addModuleWithNullIdThrowsException() {
        TestModule nullIdModule = new TestModule(null);

        assertThrows(IllegalArgumentException.class, () -> {
            context.addModule(nullIdModule);
        });
    }

    @Test
    void addModuleWithEmptyIdThrowsException() {
        TestModule emptyIdModule = new TestModule("");

        assertThrows(IllegalArgumentException.class, () -> {
            context.addModule(emptyIdModule);
        });
    }

    @Test
    void addNullModuleThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            context.addModule(null);
        });
    }

    @Test
    void removeModuleFromApplicationContext() {
        TestModule testModule = new TestModule("test-module");

        context.addModule(testModule);
        assertEquals(1, context.getModules().size());

        AnalyseModule removed = context.removeModule("test-module");

        assertEquals(testModule, removed);
        assertEquals(0, context.getModules().size());
        assertNull(Main.getModule("test-module"));
    }

    @Test
    void removeNonExistentModuleReturnsNull() {
        AnalyseModule removed = context.removeModule("non-existent");
        assertNull(removed);
    }

    @Test
    void clearModulesEmptiesApplicationContext() {
        TestModule module1 = new TestModule("module1");
        TestModule module2 = new TestModule("module2");

        context.addModule(module1);
        context.addModule(module2);
        assertEquals(2, context.getModules().size());

        context.clearModules();
        assertEquals(0, context.getModules().size());
        assertTrue(context.getModules().isEmpty());
    }

    @Test
    void getModulesReturnsDefensiveCopy() {
        TestModule testModule = new TestModule("test-module");
        context.addModule(testModule);

        Map<String, AnalyseModule> modules1 = context.getModules();
        Map<String, AnalyseModule> modules2 = context.getModules();

        assertNotSame(modules1, modules2); // Should be different instances
        assertEquals(modules1, modules2);  // But with same content
    }
}