package com.nationwar.core.client.gui.theme;

/**
 * A central place for shared GUI style tokens like colors, spacing, and fonts.
 * TODO: These values should be loaded from a configuration file to allow user customization and persistence.
 */
public final class Theme {

    public enum ThemeType {
        DARK,
        LIGHT
    }

    private static ThemeType currentTheme = ThemeType.DARK;

    private Theme() {
        // Prevent instantiation
    }

    /**
     * Applies a new theme, updating all color values.
     * @param type The theme to apply (DARK or LIGHT).
     */
    public static void applyTheme(ThemeType type) {
        currentTheme = type;
        switch (type) {
            case LIGHT:
                Colors.BACKGROUND_PRIMARY = 0xFFFFFFFF;
                Colors.BACKGROUND_SECONDARY = 0xFFF0F0F0;
                Colors.BORDER_PRIMARY = 0xFFCCCCCC;
                Colors.TEXT_PRIMARY = 0xFF000000;
                Colors.TEXT_SECONDARY = 0xFF555555;
                Colors.ACCENT_PRIMARY = 0xFF7A53B2; // Slightly adjusted for light background
                Colors.ACCENT_SECONDARY = 0xFF5A3392;
                break;
            case DARK:
            default:
                Colors.BACKGROUND_PRIMARY = 0xFF2A2A2A;
                Colors.BACKGROUND_SECONDARY = 0xFF3C3C3C;
                Colors.BORDER_PRIMARY = 0xFF555555;
                Colors.TEXT_PRIMARY = 0xFFFFFFFF;
                Colors.TEXT_SECONDARY = 0xFFAAAAAA;
                Colors.ACCENT_PRIMARY = 0xFF8A63D2;
                Colors.ACCENT_SECONDARY = 0xFF6A43A2;
                break;
        }
    }

    public static ThemeType getCurrentTheme() {
        return currentTheme;
    }

    public static final class Colors {
        public static int BACKGROUND_PRIMARY = 0xFF2A2A2A;
        public static int BACKGROUND_SECONDARY = 0xFF3C3C3C;
        public static int BORDER_PRIMARY = 0xFF555555;
        public static int TEXT_PRIMARY = 0xFFFFFFFF;
        public static int TEXT_SECONDARY = 0xFFAAAAAA;
        public static int ACCENT_PRIMARY = 0xFF8A63D2;
        public static int ACCENT_SECONDARY = 0xFF6A43A2;

        private Colors() {}
    }

    public static final class Spacing {
        public static int PADDING_SMALL = 2;
        public static int PADDING_MEDIUM = 4;
        public static int PADDING_LARGE = 8;
        public static int GAP_SMALL = 2;
        public static int GAP_MEDIUM = 5;
        public static int GAP_LARGE = 10;

        private Spacing() {}
    }
}
