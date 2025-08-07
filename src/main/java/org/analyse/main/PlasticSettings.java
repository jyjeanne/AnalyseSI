package org.analyse.main;

import javax.swing.LookAndFeel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticTheme;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;

public final class PlasticSettings {

    private LookAndFeel selectedLookAndFeel;
    private PlasticTheme selectedTheme;
    private Boolean useSystemFonts;
    private String fontSizeHints;
    private boolean useNarrowButtons;
    private boolean tabIconsEnabled;
    private Boolean popupDropShadowEnabled;
    private String plasticTabStyle;
    private boolean plasticHighContrastFocusEnabled;

    private PlasticSettings() {}

    public static PlasticSettings createDefault() {
        PlasticSettings settings = new PlasticSettings();
        settings.selectedLookAndFeel = new PlasticXPLookAndFeel();
        settings.selectedTheme = new ExperienceBlue();
        settings.useSystemFonts = Boolean.TRUE;
        settings.fontSizeHints = "mixed";
        settings.useNarrowButtons = false;
        settings.tabIconsEnabled = true;
        settings.popupDropShadowEnabled = Boolean.TRUE;
        settings.plasticTabStyle = PlasticLookAndFeel.TAB_STYLE_DEFAULT_VALUE;
        settings.plasticHighContrastFocusEnabled = false;
        return settings;
    }

    public LookAndFeel getSelectedLookAndFeel() { return selectedLookAndFeel; }
    public PlasticTheme getSelectedTheme() { return selectedTheme; }
    public Boolean isUseSystemFonts() { return useSystemFonts; }
    public String getFontSizeHints() { return fontSizeHints; }
    public boolean isUseNarrowButtons() { return useNarrowButtons; }
    public boolean isTabIconsEnabled() { return tabIconsEnabled; }
    public Boolean isPopupDropShadowEnabled() { return popupDropShadowEnabled; }
    public String getPlasticTabStyle() { return plasticTabStyle; }
    public boolean isPlasticHighContrastFocusEnabled() { return plasticHighContrastFocusEnabled; }
}