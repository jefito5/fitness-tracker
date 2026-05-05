package theme;

import java.awt.Color;
import java.awt.Font;

public class UITheme {
    
    // --- 1. COLOUR PALETTE (Documented) ---
    public static final Color PRIMARY = new Color(41, 128, 185);     // Pagrindinė mėlyna (Mygtukams, akcentams)
    public static final Color ON_PRIMARY = Color.WHITE;              // Tekstas ant pagrindinės spalvos
    public static final Color SURFACE = Color.WHITE;                 // Kortelių, panelių fonas
    public static final Color BACKGROUND = new Color(240, 242, 245); // Bendras lango fonas (šviesiai pilkas)
    public static final Color ERROR = new Color(231, 76, 60);        // Klaidos / Ištrynimo mygtukai
    public static final Color SUCCESS = new Color(46, 204, 113);     // Sėkmės pranešimai / Patvirtinimai
    public static final Color TEXT_MAIN = new Color(44, 62, 80);     // Pagrindinis tamsus tekstas
    public static final Color TEXT_MUTED = new Color(127, 140, 141); // Antraeilis, pilkšvas tekstas
    public static final Color BORDER = new Color(223, 230, 233);     // Rėmelių ir linijų spalva

    // --- 2. TYPOGRAPHY (Fonts) ---
    public static final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.BOLD, 16);
    public static final Font FONT_REGULAR = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 12);

    // --- 3. SPACING & BORDER RADII ---
    public static final int RADIUS_SMALL = 5;
    public static final int RADIUS_MEDIUM = 10;
    public static final int RADIUS_LARGE = 15;
    
    public static final int PADDING_SMALL = 8;
    public static final int PADDING_MEDIUM = 16;
    public static final int PADDING_LARGE = 24;
}