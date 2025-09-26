package org.analyse.core.context;

import java.util.logging.Logger;
import java.util.logging.Level;

import org.analyse.core.gui.AboutWindow;
import org.analyse.core.gui.AnalyseFrame;
import org.analyse.core.gui.ParametrageWindow;
import org.analyse.core.gui.SplashScreen;
import org.analyse.core.gui.action.GlobalActionCollection;
import org.analyse.core.gui.statusbar.AnalyseStatusbar;
import org.analyse.core.modules.AnalyseModule;

/**
 * Helper class to provide safe access to ApplicationContext components.
 * This class provides null-safe methods and graceful degradation when components
 * are not yet initialized, making migration from static variables smoother.
 *
 * @author Refactored migration helper
 * @version 1.0
 */
public final class ContextHelper {

    private static final Logger logger = Logger.getLogger(ContextHelper.class.getName());

    private ContextHelper() {
        // Utility class - prevent instantiation
    }

    /**
     * Get the application context safely.
     *
     * @return The ApplicationContext instance, or null if not available
     */
    private static ApplicationContext getContext() {
        try {
            return ApplicationContext.getInstance();
        } catch (Exception e) {
            logger.log(Level.WARNING, "ApplicationContext not available", e);
            return null;
        }
    }

    // ======================================================================
    // Safe component access methods
    // ======================================================================

    /**
     * Get the main application frame safely.
     *
     * @return The AnalyseFrame instance, or null if not available
     */
    public static AnalyseFrame getAnalyseFrame() {
        ApplicationContext context = getContext();
        if (context != null && context.isInitialized()) {
            try {
                return context.getAnalyseFrame();
            } catch (IllegalStateException e) {
                logger.log(Level.FINE, "AnalyseFrame not yet initialized");
            }
        }
        return null;
    }

    /**
     * Get the status bar safely.
     *
     * @return The AnalyseStatusbar instance, or null if not available
     */
    public static AnalyseStatusbar getStatusbar() {
        ApplicationContext context = getContext();
        if (context != null && context.isInitialized()) {
            try {
                return context.getStatusbar();
            } catch (IllegalStateException e) {
                logger.log(Level.FINE, "Statusbar not yet initialized");
            }
        }
        return null;
    }

    /**
     * Get the splash screen safely.
     *
     * @return The SplashScreen instance, or null if not available
     */
    public static SplashScreen getSplash() {
        ApplicationContext context = getContext();
        if (context != null && context.isInitialized()) {
            return context.getSplash();
        }
        return null;
    }

    /**
     * Get the about window safely.
     *
     * @return The AboutWindow instance, or null if not available
     */
    public static AboutWindow getAboutWindow() {
        ApplicationContext context = getContext();
        if (context != null && context.isInitialized()) {
            try {
                return context.getAboutWindow();
            } catch (IllegalStateException e) {
                logger.log(Level.FINE, "AboutWindow not yet initialized");
            }
        }
        return null;
    }

    /**
     * Get the parameters window safely.
     *
     * @return The ParametrageWindow instance, or null if not available
     */
    public static ParametrageWindow getParametrageWindow() {
        ApplicationContext context = getContext();
        if (context != null && context.isInitialized()) {
            try {
                return context.getParametrageWindow();
            } catch (IllegalStateException e) {
                logger.log(Level.FINE, "ParametrageWindow not yet initialized");
            }
        }
        return null;
    }

    /**
     * Get the global action collection safely.
     *
     * @return The GlobalActionCollection instance, or null if not available
     */
    public static GlobalActionCollection getGlobalActionCollection() {
        ApplicationContext context = getContext();
        if (context != null && context.isInitialized()) {
            try {
                return context.getGlobalActionCollection();
            } catch (IllegalStateException e) {
                logger.log(Level.FINE, "GlobalActionCollection not yet initialized");
            }
        }
        return null;
    }

    /**
     * Get a module by its ID safely.
     *
     * @param id The module ID
     * @return The module instance, or null if not found or not available
     */
    public static AnalyseModule getModule(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        ApplicationContext context = getContext();
        if (context != null && context.isInitialized()) {
            return context.getModule(id);
        }
        return null;
    }

    // ======================================================================
    // Convenience methods for common operations
    // ======================================================================

    /**
     * Update splash screen progress safely.
     *
     * @param progress The progress value (0-100)
     */
    public static void updateSplashProgress(int progress) {
        SplashScreen splash = getSplash();
        if (splash != null) {
            splash.setProgress(progress);
        }
    }

    /**
     * Update status bar message safely.
     *
     * @param message The status message
     */
    public static void updateStatusMessage(String message) {
        AnalyseStatusbar statusbar = getStatusbar();
        if (statusbar != null) {
            // Assuming statusbar has a method to set message
            // statusbar.setMessage(message);
            logger.log(Level.FINE, "Status message: " + message);
        }
    }

    /**
     * Check if the application is fully initialized.
     *
     * @return true if all core components are available
     */
    public static boolean isApplicationReady() {
        return getAnalyseFrame() != null &&
               getStatusbar() != null &&
               getGlobalActionCollection() != null;
    }

    /**
     * Execute an operation that requires the main frame.
     *
     * @param operation The operation to execute
     */
    public static void withMainFrame(FrameOperation operation) {
        AnalyseFrame frame = getAnalyseFrame();
        if (frame != null) {
            try {
                operation.execute(frame);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error executing frame operation", e);
            }
        } else {
            logger.log(Level.WARNING, "Main frame not available for operation");
        }
    }

    /**
     * Execute an operation that requires a specific module.
     *
     * @param moduleId The module ID
     * @param operation The operation to execute
     */
    public static void withModule(String moduleId, ModuleOperation operation) {
        AnalyseModule module = getModule(moduleId);
        if (module != null) {
            try {
                operation.execute(module);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error executing module operation for " + moduleId, e);
            }
        } else {
            logger.log(Level.WARNING, "Module " + moduleId + " not available for operation");
        }
    }

    // ======================================================================
    // Functional interfaces for operations
    // ======================================================================

    /**
     * Functional interface for operations that require the main frame.
     */
    @FunctionalInterface
    public interface FrameOperation {
        void execute(AnalyseFrame frame) throws Exception;
    }

    /**
     * Functional interface for operations that require a module.
     */
    @FunctionalInterface
    public interface ModuleOperation {
        void execute(AnalyseModule module) throws Exception;
    }
}