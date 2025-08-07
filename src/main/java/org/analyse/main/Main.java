package org.analyse.main;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.UIManager;
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
    private Main() {}

    public static final String SETTINGS_DIRECTORY = System
            .getProperty("user.home")
            + File.separator + ".analyseSI" + File.separator;

    public static final String USER_PROPS = SETTINGS_DIRECTORY + "analyseSI.properties";

    public static AnalyseFrame analyseFrame;
    public static AnalyseStatusbar statusbar;
    public static SplashScreen splash;
    public static AboutWindow aboutWindow;
    public static ParametrageWindow parametrageWindow;
    public static Map<String, AnalyseModule> modules = new HashMap<String, AnalyseModule>();
    public static GlobalActionCollection globalActionCollection;

    public static void main(String[] args)
    {
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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        splash = new SplashScreen();
        splash.setProgress(0);

        globalActionCollection = new GlobalActionCollection();

        statusbar = new AnalyseStatusbar();
        splash.setProgress(10);

        AnalyseModule mod;
        mod = new MeriseModule();
        modules.put(mod.getID(), mod);
        splash.setProgress(20);

        analyseFrame = new AnalyseFrame();

        for (Iterator<Entry<String, AnalyseModule>> e = modules.entrySet().iterator(); e.hasNext();) {
            e.next().getValue().initGUI(analyseFrame);
        }

        splash.setProgress(90);
        aboutWindow = new AboutWindow(analyseFrame);
        parametrageWindow = new ParametrageWindow(analyseFrame);
        splash.setProgress(100);
        analyseFrame.setVisible(true);
        splash.setVisible(false);

        if (args.length > 0)
            analyseFrame.getAnalyseSave().open(args[0]);
    }

    public static AnalyseModule getModule(String id)
    {
        return modules.get(id);
    }
}