package it.unical.informatica.progettouid.util;

import java.util.prefs.Preferences;

public class ThemeManager {
    private static String currentTheme = "light";
    private static final String THEME_PREFERENCE_KEY = "app_theme";

    public static String getCurrentTheme() {
        // Load from preferences
        Preferences prefs = Preferences.userNodeForPackage(ThemeManager.class);
        currentTheme = prefs.get(THEME_PREFERENCE_KEY, "light");
        return currentTheme;
    }

    public static void setTheme(String theme) {
        currentTheme = theme;
        // Save to preferences
        Preferences prefs = Preferences.userNodeForPackage(ThemeManager.class);
        prefs.put(THEME_PREFERENCE_KEY, theme);
    }
}

