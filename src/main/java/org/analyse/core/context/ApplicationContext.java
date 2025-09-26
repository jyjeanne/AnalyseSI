package org.analyse.core.context;

import java.util.HashMap;
import java.util.Map;
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
 * Application context singleton that manages global application state.
 * Replaces static public variables with proper encapsulation and thread safety.
 *
 * @author Refactored from Main.java
 * @version 1.0
 */
public final class ApplicationContext {

    private static final Logger logger = Logger.getLogger(ApplicationContext.class.getName());

    // Singleton instance with thread-safe lazy initialization
    private static volatile ApplicationContext instance;
    private static final Object lock = new Object();

    // Application components
    private AnalyseFrame analyseFrame;
    private AnalyseStatusbar statusbar;
    private SplashScreen splash;
    private AboutWindow aboutWindow;
    private ParametrageWindow parametrageWindow;
    private final Map<String, AnalyseModule> modules = new HashMap<>();
    private GlobalActionCollection globalActionCollection;

    // Application state
    private boolean initialized = false;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private ApplicationContext() {
        logger.log(Level.FINE, "ApplicationContext created");
    }

    /**
     * Get the singleton instance of ApplicationContext.
     * Uses double-checked locking pattern for thread safety.
     *
     * @return The singleton instance
     */
    public static ApplicationContext getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ApplicationContext();
                }
            }
        }
        return instance;
    }

    /**
     * Initialize the application context.
     * Should be called once during application startup.
     *
     * @throws IllegalStateException if already initialized
     */
    public synchronized void initialize() {
        if (initialized) {
            throw new IllegalStateException("ApplicationContext already initialized");
        }

        logger.log(Level.INFO, "Initializing ApplicationContext");
        initialized = true;
    }

    /**
     * Check if the application context is initialized.
     *
     * @return true if initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    // ======================================================================
    // Component getters and setters with null checks
    // ======================================================================

    /**
     * Get the main application frame.
     *
     * @return The AnalyseFrame instance
     * @throws IllegalStateException if not set
     */
    public AnalyseFrame getAnalyseFrame() {
        if (analyseFrame == null) {
            throw new IllegalStateException("AnalyseFrame not initialized");
        }
        return analyseFrame;
    }

    /**
     * Set the main application frame.
     *
     * @param analyseFrame The AnalyseFrame instance
     * @throws IllegalArgumentException if frame is null
     */
    public void setAnalyseFrame(AnalyseFrame analyseFrame) {
        if (analyseFrame == null) {
            throw new IllegalArgumentException("AnalyseFrame cannot be null");
        }
        this.analyseFrame = analyseFrame;
        logger.log(Level.FINE, "AnalyseFrame set");
    }

    /**
     * Get the application status bar.
     *
     * @return The AnalyseStatusbar instance
     * @throws IllegalStateException if not set
     */
    public AnalyseStatusbar getStatusbar() {
        if (statusbar == null) {
            throw new IllegalStateException("Statusbar not initialized");
        }
        return statusbar;
    }

    /**
     * Set the application status bar.
     *
     * @param statusbar The AnalyseStatusbar instance
     * @throws IllegalArgumentException if statusbar is null
     */
    public void setStatusbar(AnalyseStatusbar statusbar) {
        if (statusbar == null) {
            throw new IllegalArgumentException("Statusbar cannot be null");
        }
        this.statusbar = statusbar;
        logger.log(Level.FINE, "Statusbar set");
    }

    /**
     * Get the splash screen (may be null after startup).
     *
     * @return The SplashScreen instance or null
     */
    public SplashScreen getSplash() {
        return splash;
    }

    /**
     * Set the splash screen.
     *
     * @param splash The SplashScreen instance (can be null)
     */
    public void setSplash(SplashScreen splash) {
        this.splash = splash;
        logger.log(Level.FINE, "SplashScreen " + (splash != null ? "set" : "cleared"));
    }

    /**
     * Get the about window.
     *
     * @return The AboutWindow instance
     * @throws IllegalStateException if not set
     */
    public AboutWindow getAboutWindow() {
        if (aboutWindow == null) {
            throw new IllegalStateException("AboutWindow not initialized");
        }
        return aboutWindow;
    }

    /**
     * Set the about window.
     *
     * @param aboutWindow The AboutWindow instance
     * @throws IllegalArgumentException if window is null
     */
    public void setAboutWindow(AboutWindow aboutWindow) {
        if (aboutWindow == null) {
            throw new IllegalArgumentException("AboutWindow cannot be null");
        }
        this.aboutWindow = aboutWindow;
        logger.log(Level.FINE, "AboutWindow set");
    }

    /**
     * Get the parameters window.
     *
     * @return The ParametrageWindow instance
     * @throws IllegalStateException if not set
     */
    public ParametrageWindow getParametrageWindow() {
        if (parametrageWindow == null) {
            throw new IllegalStateException("ParametrageWindow not initialized");
        }
        return parametrageWindow;
    }

    /**
     * Set the parameters window.
     *
     * @param parametrageWindow The ParametrageWindow instance
     * @throws IllegalArgumentException if window is null
     */
    public void setParametrageWindow(ParametrageWindow parametrageWindow) {
        if (parametrageWindow == null) {
            throw new IllegalArgumentException("ParametrageWindow cannot be null");
        }
        this.parametrageWindow = parametrageWindow;
        logger.log(Level.FINE, "ParametrageWindow set");
    }

    /**
     * Get the global action collection.
     *
     * @return The GlobalActionCollection instance
     * @throws IllegalStateException if not set
     */
    public GlobalActionCollection getGlobalActionCollection() {
        if (globalActionCollection == null) {
            throw new IllegalStateException("GlobalActionCollection not initialized");
        }
        return globalActionCollection;
    }

    /**
     * Set the global action collection.
     *
     * @param globalActionCollection The GlobalActionCollection instance
     * @throws IllegalArgumentException if collection is null
     */
    public void setGlobalActionCollection(GlobalActionCollection globalActionCollection) {
        if (globalActionCollection == null) {
            throw new IllegalArgumentException("GlobalActionCollection cannot be null");
        }
        this.globalActionCollection = globalActionCollection;
        logger.log(Level.FINE, "GlobalActionCollection set");
    }

    // ======================================================================
    // Module management
    // ======================================================================

    /**
     * Add a module to the application.
     *
     * @param module The module to add
     * @throws IllegalArgumentException if module is null or ID is null/empty
     */
    public synchronized void addModule(AnalyseModule module) {
        if (module == null) {
            throw new IllegalArgumentException("Module cannot be null");
        }
        if (module.getID() == null || module.getID().trim().isEmpty()) {
            throw new IllegalArgumentException("Module ID cannot be null or empty");
        }

        modules.put(module.getID(), module);
        logger.log(Level.INFO, "Module added: " + module.getID());
    }

    /**
     * Get a module by its ID.
     *
     * @param id The module ID
     * @return The module instance or null if not found
     */
    public AnalyseModule getModule(String id) {
        if (id == null || id.trim().isEmpty()) {
            logger.log(Level.WARNING, "Attempted to get module with null/empty ID");
            return null;
        }
        return modules.get(id);
    }

    /**
     * Remove a module by its ID.
     *
     * @param id The module ID
     * @return The removed module or null if not found
     */
    public synchronized AnalyseModule removeModule(String id) {
        if (id == null || id.trim().isEmpty()) {
            logger.log(Level.WARNING, "Attempted to remove module with null/empty ID");
            return null;
        }

        AnalyseModule removed = modules.remove(id);
        if (removed != null) {
            logger.log(Level.INFO, "Module removed: " + id);
        }
        return removed;
    }

    /**
     * Get all registered modules.
     *
     * @return A copy of the modules map
     */
    public Map<String, AnalyseModule> getModules() {
        return new HashMap<>(modules);
    }

    /**
     * Clear all modules.
     */
    public synchronized void clearModules() {
        int count = modules.size();
        modules.clear();
        logger.log(Level.INFO, "Cleared " + count + " modules");
    }

    // ======================================================================
    // Lifecycle management
    // ======================================================================

    /**
     * Shutdown the application context and clean up resources.
     */
    public synchronized void shutdown() {
        logger.log(Level.INFO, "Shutting down ApplicationContext");

        // Clear splash screen reference
        splash = null;

        // Clear modules
        clearModules();

        // Don't null out other components as they may be needed during shutdown
        initialized = false;

        logger.log(Level.INFO, "ApplicationContext shutdown complete");
    }

    /**
     * Get debug information about the application context.
     *
     * @return Debug information string
     */
    public String getDebugInfo() {
        StringBuilder info = new StringBuilder();
        info.append("ApplicationContext Debug Info:\n");
        info.append("- Initialized: ").append(initialized).append("\n");
        info.append("- AnalyseFrame: ").append(analyseFrame != null ? "set" : "null").append("\n");
        info.append("- Statusbar: ").append(statusbar != null ? "set" : "null").append("\n");
        info.append("- Splash: ").append(splash != null ? "set" : "null").append("\n");
        info.append("- AboutWindow: ").append(aboutWindow != null ? "set" : "null").append("\n");
        info.append("- ParametrageWindow: ").append(parametrageWindow != null ? "set" : "null").append("\n");
        info.append("- GlobalActionCollection: ").append(globalActionCollection != null ? "set" : "null").append("\n");
        info.append("- Modules count: ").append(modules.size()).append("\n");

        return info.toString();
    }
}