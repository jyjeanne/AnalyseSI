package org.analyse.main;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.swing.UIManager;
import org.analyse.core.context.ApplicationContext;
import org.analyse.core.gui.ParametrageWindow;
import org.analyse.core.gui.AboutWindow;
import org.analyse.core.gui.AnalyseFrame;
import org.analyse.core.gui.SplashScreen;
import org.analyse.core.gui.action.GlobalActionCollection;
import org.analyse.core.gui.statusbar.AnalyseStatusbar;
import org.analyse.core.modules.AnalyseModule;
import org.analyse.merise.main.MeriseModule;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;

public final class Main
{
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private Main() {}

    public static final String SETTINGS_DIRECTORY = System
            .getProperty("user.home")
            + File.separator + ".analyseSI" + File.separator;

    public static final String USER_PROPS = SETTINGS_DIRECTORY + "analyseSI.properties";

    // Legacy static variables for backward compatibility - DEPRECATED
    // Use ApplicationContext.getInstance() instead
    @Deprecated
    public static AnalyseFrame analyseFrame;
    @Deprecated
    public static AnalyseStatusbar statusbar;
    @Deprecated
    public static SplashScreen splash;
    @Deprecated
    public static AboutWindow aboutWindow;
    @Deprecated
    public static ParametrageWindow parametrageWindow;
    @Deprecated
    public static GlobalActionCollection globalActionCollection;

    public static void main(String[] args) {
        logger.log(Level.INFO, "Starting AnalyseSI application");

        try {
            // Initialize application context
            ApplicationContext context = ApplicationContext.getInstance();
            context.initialize();

            // Initialize Look and Feel
            initializeLookAndFeel();

            // Create and configure splash screen
            SplashScreen splashScreen = new SplashScreen();
            splashScreen.setProgress(0);
            context.setSplash(splashScreen);

            // Set legacy static variable for backward compatibility
            splash = splashScreen;

            // Create global action collection
            GlobalActionCollection actions = new GlobalActionCollection();
            context.setGlobalActionCollection(actions);
            globalActionCollection = actions; // Legacy compatibility

            // Create status bar
            AnalyseStatusbar statusBar = new AnalyseStatusbar();
            context.setStatusbar(statusBar);
            statusbar = statusBar; // Legacy compatibility
            splashScreen.setProgress(10);

            // Initialize modules
            initializeModules(context, splashScreen);

            // Create main frame
            AnalyseFrame frame = new AnalyseFrame();
            context.setAnalyseFrame(frame);
            analyseFrame = frame; // Legacy compatibility

            // Initialize GUI for all modules
            initializeModuleGUI(context, frame);
            splashScreen.setProgress(90);

            // Create dialogs
            AboutWindow about = new AboutWindow(frame);
            context.setAboutWindow(about);
            aboutWindow = about; // Legacy compatibility

            ParametrageWindow params = new ParametrageWindow(frame);
            context.setParametrageWindow(params);
            parametrageWindow = params; // Legacy compatibility

            // Finalize application startup
            splashScreen.setProgress(100);
            frame.setVisible(true);
            splashScreen.setVisible(false);

            // Open file if specified in command line
            if (args.length > 0) {
                frame.getAnalyseSave().open(args[0]);
            }

            logger.log(Level.INFO, "AnalyseSI application started successfully");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to start application", e);
            throw new RuntimeException("Application startup failed: " + e.getMessage(), e);
        }
    }

    /**
     * Initialize the Look and Feel settings.
     */
    private static void initializeLookAndFeel() {
        try {
            PlasticSettings settings = PlasticSettings.createDefault();

            Options.setDefaultIconSize(new java.awt.Dimension(16, 16));

            UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, settings.isUseSystemFonts());
            Options.setUseNarrowButtons(settings.isUseNarrowButtons());

            Options.setTabIconsEnabled(settings.isTabIconsEnabled());
            UIManager.put(Options.POPUP_DROP_SHADOW_ENABLED_KEY, settings.isPopupDropShadowEnabled());

            PlasticLookAndFeel.setCurrentTheme(settings.getSelectedTheme());
            PlasticLookAndFeel.setTabStyle(settings.getPlasticTabStyle());
            PlasticLookAndFeel.setHighContrastFocusColorsEnabled(settings.isPlasticHighContrastFocusEnabled());

            UIManager.setLookAndFeel(settings.getSelectedLookAndFeel());

            logger.log(Level.INFO, "Look and Feel initialized successfully");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to initialize look and feel", e);
            throw new RuntimeException("Failed to initialize look and feel: " + e.getMessage(), e);
        }
    }

    /**
     * Initialize application modules.
     */
    private static void initializeModules(ApplicationContext context, SplashScreen splash) {
        logger.log(Level.INFO, "Initializing modules");

        // Add Merise module
        AnalyseModule meriseModule = new MeriseModule();
        context.addModule(meriseModule);

        splash.setProgress(20);
        logger.log(Level.INFO, "Modules initialized");
    }

    /**
     * Initialize GUI for all modules.
     */
    private static void initializeModuleGUI(ApplicationContext context, AnalyseFrame frame) {
        logger.log(Level.INFO, "Initializing module GUIs");

        Map<String, AnalyseModule> modules = context.getModules();
        for (AnalyseModule module : modules.values()) {
            module.initGUI(frame);
        }

        logger.log(Level.INFO, "Module GUIs initialized");
    }

    /**
     * Get a module by its ID.
     * This method is deprecated. Use ApplicationContext.getInstance().getModule(id) instead.
     *
     * @param id The module ID
     * @return The module instance or null if not found
     * @deprecated Use ApplicationContext.getInstance().getModule(id) instead
     */
    @Deprecated
    public static AnalyseModule getModule(String id) {
        try {
            return ApplicationContext.getInstance().getModule(id);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error getting module " + id + " via legacy method", e);
            return null;
        }
    }
}