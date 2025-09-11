package org.analyse.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.analyse.core.modules.AnalyseModule;
import org.analyse.merise.main.MeriseModule;

import javax.swing.UIManager;
import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainTest {

    @BeforeEach
    void setUp() {
        Main.modules.clear();
        Main.analyseFrame = null;
        Main.statusbar = null;
        Main.splash = null;
        Main.aboutWindow = null;
        Main.parametrageWindow = null;
        Main.globalActionCollection = null;
    }

    @AfterEach
    void tearDown() {
        Main.modules.clear();
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
        AnalyseModule mockModule = mock(AnalyseModule.class);
        when(mockModule.getID()).thenReturn("test-module");
        Main.modules.put("test-module", mockModule);

        AnalyseModule result = Main.getModule("test-module");

        assertEquals(mockModule, result);
    }

    @Test
    void getModuleReturnsNullWhenModuleDoesNotExist() {
        AnalyseModule result = Main.getModule("non-existent-module");

        assertNull(result);
    }

    @Test
    void getModuleReturnsNullWhenModulesMapIsEmpty() {
        assertTrue(Main.modules.isEmpty());

        AnalyseModule result = Main.getModule("any-module");

        assertNull(result);
    }

    @Test
    void modulesMapInitializedAsHashMap() {
        assertNotNull(Main.modules);
        assertTrue(Main.modules instanceof Map);
    }

    @Test
    void canAddMultipleModulesToModulesMap() {
        AnalyseModule module1 = mock(AnalyseModule.class);
        AnalyseModule module2 = mock(AnalyseModule.class);
        when(module1.getID()).thenReturn("module1");
        when(module2.getID()).thenReturn("module2");

        Main.modules.put("module1", module1);
        Main.modules.put("module2", module2);

        assertEquals(2, Main.modules.size());
        assertEquals(module1, Main.getModule("module1"));
        assertEquals(module2, Main.getModule("module2"));
    }

    @Test
    void moduleCanBeOverwrittenInModulesMap() {
        AnalyseModule originalModule = mock(AnalyseModule.class);
        AnalyseModule newModule = mock(AnalyseModule.class);
        when(originalModule.getID()).thenReturn("same-id");
        when(newModule.getID()).thenReturn("same-id");

        Main.modules.put("same-id", originalModule);
        Main.modules.put("same-id", newModule);

        assertEquals(1, Main.modules.size());
        assertEquals(newModule, Main.getModule("same-id"));
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
    void allStaticFieldsInitializeToNull() {
        assertNull(Main.analyseFrame);
        assertNull(Main.statusbar);
        assertNull(Main.splash);
        assertNull(Main.aboutWindow);
        assertNull(Main.parametrageWindow);
        assertNull(Main.globalActionCollection);
    }

    @Test
    void modulesMapIsNotNull() {
        assertNotNull(Main.modules);
    }
}
