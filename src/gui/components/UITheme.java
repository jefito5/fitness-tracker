package gui.components;

import java.awt.Color;
import java.awt.Font;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Centralised design tokens for the Fitness Tracker UI.
 * Single source of truth for colours, typography, spacing, and borders.
 *
 * Foundation for the UI redesign initiative (Jira: UI-1).
 * All redesigned screens should pull from these constants rather than
 * hardcoding values.
 */
public final class UITheme {

    private UITheme() {}

    // ── Colour palette ──────────────────────────────────────────────────
    /** Primary brand colour — emerald (fitness vibe) */
    public static final Color PRIMARY         = new Color(0x10, 0xB9, 0x81);
    public static final Color PRIMARY_HOVER   = new Color(0x05, 0x96, 0x69);
    public static final Color PRIMARY_PRESSED = new Color(0x04, 0x7A, 0x55);
    public static final Color ON_PRIMARY      = Color.WHITE;

    /** Secondary — slate-700 for neutral filled buttons */
    public static final Color SECONDARY        = new Color(0x33, 0x41, 0x55);
    public static final Color SECONDARY_HOVER  = new Color(0x1E, 0x29, 0x3B);

    /** Surface colours */
    public static final Color BACKGROUND  = new Color(0xF1, 0xF5, 0xF9); // slate-100
    public static final Color SURFACE     = Color.WHITE;
    public static final Color SURFACE_ALT = new Color(0xF8, 0xFA, 0xFC); // slate-50

    /** Text colours */
    public static final Color TEXT_PRIMARY   = new Color(0x0F, 0x17, 0x2A); // slate-900
    public static final Color TEXT_SECONDARY = new Color(0x47, 0x55, 0x69); // slate-600
    public static final Color TEXT_MUTED     = new Color(0x94, 0xA3, 0xB8); // slate-400
    public static final Color TEXT_ON_DARK   = Color.WHITE;

    /** Borders & dividers */
    public static final Color BORDER       = new Color(0xE2, 0xE8, 0xF0); // slate-200
    public static final Color BORDER_FOCUS = PRIMARY;

    /** Status colours */
    public static final Color SUCCESS = new Color(0x16, 0xA3, 0x4A);
    public static final Color WARNING = new Color(0xF5, 0x9E, 0x0B);
    public static final Color DANGER  = new Color(0xDC, 0x26, 0x26);
    public static final Color INFO    = new Color(0x25, 0x63, 0xEB);

    /** Tag chip pastel pairings (background, foreground) */
    public static final Color TAG_CARDIO_BG    = new Color(0xDB, 0xEA, 0xFE);
    public static final Color TAG_CARDIO_FG    = new Color(0x1E, 0x40, 0xAF);
    public static final Color TAG_STRENGTH_BG  = new Color(0xFE, 0xF3, 0xC7);
    public static final Color TAG_STRENGTH_FG  = new Color(0x92, 0x40, 0x0E);
    public static final Color TAG_MUSCLE_BG    = new Color(0xE0, 0xE7, 0xFF);
    public static final Color TAG_MUSCLE_FG    = new Color(0x37, 0x30, 0x9A);
    public static final Color TAG_NEUTRAL_BG   = new Color(0xF1, 0xF5, 0xF9);
    public static final Color TAG_NEUTRAL_FG   = new Color(0x33, 0x41, 0x55);
    public static final Color TAG_PRESET_BG    = new Color(0xDC, 0xFC, 0xE7);
    public static final Color TAG_PRESET_FG    = new Color(0x14, 0x53, 0x2D);
    public static final Color TAG_CUSTOM_BG    = new Color(0xFC, 0xE7, 0xF3);
    public static final Color TAG_CUSTOM_FG    = new Color(0x83, 0x18, 0x43);

    // ── Typography ──────────────────────────────────────────────────────
    /** Primary font family. Segoe UI is standard on Windows; Swing falls back gracefully. */
    public static final String FONT_FAMILY = "Segoe UI";

    public static final Font FONT_DISPLAY    = new Font(FONT_FAMILY, Font.BOLD,  26);
    public static final Font FONT_TITLE      = new Font(FONT_FAMILY, Font.BOLD,  20);
    public static final Font FONT_HEADING    = new Font(FONT_FAMILY, Font.BOLD,  16);
    public static final Font FONT_SUBHEADING = new Font(FONT_FAMILY, Font.BOLD,  13);
    public static final Font FONT_BODY       = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD  = new Font(FONT_FAMILY, Font.BOLD,  13);
    public static final Font FONT_CAPTION    = new Font(FONT_FAMILY, Font.PLAIN, 11);
    public static final Font FONT_BUTTON     = new Font(FONT_FAMILY, Font.BOLD,  13);
    public static final Font FONT_STAT_VALUE = new Font(FONT_FAMILY, Font.BOLD,  22);

    // ── Spacing (4-pt scale) ────────────────────────────────────────────
    public static final int SPACE_XS  = 4;
    public static final int SPACE_SM  = 8;
    public static final int SPACE_MD  = 12;
    public static final int SPACE_LG  = 16;
    public static final int SPACE_XL  = 24;
    public static final int SPACE_2XL = 32;

    // ── Border radii ────────────────────────────────────────────────────
    public static final int RADIUS_SM = 6;
    public static final int RADIUS_MD = 10;
    public static final int RADIUS_LG = 14;

    // ── Helper builders ─────────────────────────────────────────────────
    public static Border padding(int all) {
        return new EmptyBorder(all, all, all, all);
    }

    public static Border padding(int v, int h) {
        return new EmptyBorder(v, h, v, h);
    }

    public static Border padding(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }
}
