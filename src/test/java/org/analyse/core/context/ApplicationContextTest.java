package org.analyse.core.context;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.analyse.core.modules.AnalyseModule;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationContextTest {

    private ApplicationContext context;

    @BeforeEach
    void setUp() {
        // Reset singleton for each test
        try {
            java.lang.reflect.Field instance = ApplicationContext.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            // Ignore
        }
        context = ApplicationContext.getInstance();
    }

    @Nested
    @DisplayName("Singleton Pattern Tests")
    class SingletonPatternTests {

        @Test
        @DisplayName("Should return singleton instance")
        void shouldReturnSingletonInstance() {
            ApplicationContext instance1 = ApplicationContext.getInstance();
            ApplicationContext instance2 = ApplicationContext.getInstance();

            assertNotNull(instance1);
            assertNotNull(instance2);
            assertSame(instance1, instance2);
        }
    }

    @Nested
    @DisplayName("Initialization Tests")
    class InitializationTests {

        @Test
        @DisplayName("Should initialize context")
        void shouldInitializeContext() {
            assertFalse(context.isInitialized());

            context.initialize();

            assertTrue(context.isInitialized());
        }

        @Test
        @DisplayName("Should throw exception on double initialization")
        void shouldThrowExceptionOnDoubleInitialization() {
            context.initialize();

            assertThrows(IllegalStateException.class, () -> context.initialize());
        }
    }

    @Nested
    @DisplayName("Module Management Tests")
    class ModuleManagementTests {

        @Test
        @DisplayName("Should add and retrieve module")
        void shouldAddAndRetrieveModule() {
            AnalyseModule mockModule = mock(AnalyseModule.class);
            when(mockModule.getID()).thenReturn("testModule");

            context.addModule(mockModule);
            AnalyseModule retrieved = context.getModule("testModule");

            assertNotNull(retrieved);
            assertSame(mockModule, retrieved);
        }

        @Test
        @DisplayName("Should return null for non-existent module")
        void shouldReturnNullForNonExistentModule() {
            AnalyseModule result = context.getModule("nonExistentModule");
            assertNull(result);
        }

        @Test
        @DisplayName("Should remove module")
        void shouldRemoveModule() {
            AnalyseModule mockModule = mock(AnalyseModule.class);
            when(mockModule.getID()).thenReturn("testModule");

            context.addModule(mockModule);
            AnalyseModule removed = context.removeModule("testModule");

            assertNotNull(removed);
            assertSame(mockModule, removed);
            assertNull(context.getModule("testModule"));
        }

        @Test
        @DisplayName("Should clear all modules")
        void shouldClearAllModules() {
            AnalyseModule module1 = mock(AnalyseModule.class);
            when(module1.getID()).thenReturn("module1");
            AnalyseModule module2 = mock(AnalyseModule.class);
            when(module2.getID()).thenReturn("module2");

            context.addModule(module1);
            context.addModule(module2);

            context.clearModules();

            assertEquals(0, context.getModules().size());
        }

        @Test
        @DisplayName("Should throw exception for null module")
        void shouldThrowExceptionForNullModule() {
            assertThrows(IllegalArgumentException.class, () -> context.addModule(null));
        }

        @Test
        @DisplayName("Should throw exception for module with null ID")
        void shouldThrowExceptionForModuleWithNullId() {
            AnalyseModule mockModule = mock(AnalyseModule.class);
            when(mockModule.getID()).thenReturn(null);

            assertThrows(IllegalArgumentException.class, () -> context.addModule(mockModule));
        }
    }

    @Nested
    @DisplayName("Lifecycle Management Tests")
    class LifecycleManagementTests {

        @Test
        @DisplayName("Should shutdown context properly")
        void shouldShutdownContextProperly() {
            context.initialize();
            assertTrue(context.isInitialized());

            context.shutdown();

            assertFalse(context.isInitialized());
            assertEquals(0, context.getModules().size());
        }
    }

    @Nested
    @DisplayName("Debug Information Tests")
    class DebugInformationTests {

        @Test
        @DisplayName("Should provide debug information")
        void shouldProvideDebugInformation() {
            String debugInfo = context.getDebugInfo();

            assertNotNull(debugInfo);
            assertTrue(debugInfo.contains("ApplicationContext Debug Info"));
            assertTrue(debugInfo.contains("Initialized"));
            assertTrue(debugInfo.contains("Modules count"));
        }
    }
}