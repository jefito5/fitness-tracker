package gui;

import impl.UserDB;
import models.User;
import theme.UITheme;
import components.RoundedButton;
import components.StyledTextField;
import components.StyledPasswordField;
import components.SectionHeader;
import components.StyledMessage;
import components.StyledComboBox;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Redesigned Login & Registration screen.
 * Split layout: branded left panel + toggled form on the right.
 * Inline validation — no JOptionPane for field errors.
 * Resizable, minimum 800×500.
 */
public class Iud {

    // ── Design tokens ──────────────────────────────────────────
    static final Color BG         = new Color(0x0D1117);
    static final Color SURFACE    = new Color(0x161B22);
    static final Color SURFACE_HI = new Color(0x21262D);
    static final Color ACCENT     = new Color(0x00D4AA);
    static final Color TEXT_MAIN  = new Color(0xE6EDF3);
    static final Color TEXT_MUTED = new Color(0x8B949E);
    static final Color DANGER     = new Color(0xFF6B6B);
    static final Color SUCCESS    = new Color(0x3FB950);
    static final Color BORDER_COL = new Color(0x30363D);
    static final Color LEFT_BG    = new Color(0x0A2B22);
    static final Color LEFT_TINT  = new Color(0x7EC8B8);

    static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 20);
    static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font FONT_BTN     = new Font("Segoe UI", Font.BOLD, 13);
    static final Font FONT_TAB     = new Font("Segoe UI", Font.PLAIN, 14);

    // ── State ──────────────────────────────────────────────────
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel formArea;
    private JButton btnLoginTab, btnRegTab;

    // Login
    private JTextField  loginName;
    private JPasswordField loginPassword;
    private JLabel loginFeedback;

    // Register
    private JTextField    regName, regAge, regHeight;
    private JPasswordField regPassword;
    private JComboBox<String> regGender;
    private JLabel regFeedback;

    public Iud() { initialize(); }

    // ── Frame setup ────────────────────────────────────────────
    private void initialize() {
        frame = new JFrame("NutriTrack — Welcome");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 560);
        frame.setMinimumSize(new Dimension(800, 500));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().setBackground(BG);

        frame.getContentPane().add(buildLeftPanel(),  BorderLayout.WEST);
        frame.getContentPane().add(buildRightPanel(), BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // ── Left: branded panel ────────────────────────────────────
    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x0A2B22), 0, getHeight(), new Color(0x061510));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Decorative circles
                g2.setColor(new Color(0, 212, 170, 18));
                g2.fillOval(-60, getHeight() - 200, 280, 280);
                g2.fillOval(getWidth() - 80, -60, 200, 200);
                g2.dispose();
            }
        };
        panel.setPreferredSize(new Dimension(300, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(56, 36, 56, 36));

        JLabel logo = new JLabel("🥗");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = new JLabel("NutriTrack");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 30));
        appName.setForeground(ACCENT);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("<html><center>Your personal nutrition<br>&amp; fitness companion</center></html>");
        tagline.setFont(FONT_BODY);
        tagline.setForeground(LEFT_TINT);
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        tagline.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(Box.createVerticalGlue());
        panel.add(logo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(appName);
        panel.add(Box.createVerticalStrut(8));
        panel.add(tagline);
        panel.add(Box.createVerticalStrut(36));

        String[] features = {
            "📊  Track daily calories",
            "🎯  Set macro goals",
            "📈  View progress charts",
            "⚖  Monitor your BMI"
        };
        for (String f : features) {
            JLabel lbl = new JLabel(f);
            lbl.setFont(FONT_SMALL);
            lbl.setForeground(LEFT_TINT);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(lbl);
            panel.add(Box.createVerticalStrut(9));
        }
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    // ── Right: tab toggle + card forms ─────────────────────────
    private JPanel buildRightPanel() {
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(BG);

        // Tab bar
        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 22));
        tabs.setBackground(BG);

        btnLoginTab = makeTabButton("Sign In");
        btnRegTab   = makeTabButton("Register");
        selectTab(btnLoginTab, true);
        selectTab(btnRegTab, false);

        JLabel sep = new JLabel("  ·  ");
        sep.setForeground(BORDER_COL);
        sep.setFont(FONT_TAB);

        tabs.add(btnLoginTab);
        tabs.add(sep);
        tabs.add(btnRegTab);
        right.add(tabs, BorderLayout.NORTH);

        // Card forms
        cardLayout = new CardLayout();
        formArea   = new JPanel(cardLayout);
        formArea.setBackground(BG);
        formArea.add(buildLoginCard(),    "login");
        formArea.add(buildRegisterCard(), "register");
        right.add(formArea, BorderLayout.CENTER);

        // Wire tabs
        btnLoginTab.addActionListener(e -> {
            cardLayout.show(formArea, "login");
            selectTab(btnLoginTab, true);
            selectTab(btnRegTab, false);
        });
        btnRegTab.addActionListener(e -> {
            cardLayout.show(formArea, "register");
            selectTab(btnRegTab, true);
            selectTab(btnLoginTab, false);
        });

        return right;
    }

    // ── Login card ─────────────────────────────────────────────
    private JPanel buildLoginCard() {
        JPanel inner = new JPanel();
        inner.setBackground(BG);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(new EmptyBorder(10, 60, 10, 60));
        inner.setMaximumSize(new Dimension(420, Integer.MAX_VALUE));

        addTitle(inner, "Welcome back");
        addSubtitle(inner, "Sign in to continue tracking your progress");
        inner.add(Box.createVerticalStrut(26));

        loginName     = addField(inner, "Username");
        loginPassword = addPassField(inner, "Password");

        loginFeedback = makeFeedbackLabel();
        inner.add(loginFeedback);
        inner.add(Box.createVerticalStrut(18));

        JButton btn = makeAccentButton("Sign In  →");
        btn.addActionListener(new LoginListener());
        inner.add(btn);

        return wrapCentered(inner);
    }

    // ── Register card ──────────────────────────────────────────
    private JPanel buildRegisterCard() {
        JPanel inner = new JPanel();
        inner.setBackground(BG);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(new EmptyBorder(10, 60, 10, 60));
        inner.setMaximumSize(new Dimension(420, Integer.MAX_VALUE));

        addTitle(inner, "Create account");
        addSubtitle(inner, "Start your nutrition journey today");
        inner.add(Box.createVerticalStrut(22));

        regName     = addField(inner, "Full name");
        regAge      = addField(inner, "Age");

        // Gender dropdown
        JLabel gLbl = fieldLabel("Gender");
        gLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        inner.add(gLbl);
        inner.add(Box.createVerticalStrut(4));
        regGender = new JComboBox<>(new String[]{"male", "female"});
        styleCombo(regGender);
        inner.add(regGender);
        inner.add(Box.createVerticalStrut(12));

        regHeight   = addField(inner, "Height in cm  (optional)");
        regPassword = addPassField(inner, "Password");

        regFeedback = makeFeedbackLabel();
        inner.add(regFeedback);
        inner.add(Box.createVerticalStrut(14));

        JButton btn = makeAccentButton("Create Account");
        btn.addActionListener(new RegisterListener());
        inner.add(btn);

        return wrapCentered(inner);
    }

    // ── Form helpers ───────────────────────────────────────────
    private JTextField addField(JPanel p, String label) {
        p.add(fieldLabel(label));
        p.add(Box.createVerticalStrut(4));
        JTextField f = new JTextField();
        styleTextField(f);
        p.add(f);
        p.add(Box.createVerticalStrut(12));
        return f;
    }

    private JPasswordField addPassField(JPanel p, String label) {
        p.add(fieldLabel(label));
        p.add(Box.createVerticalStrut(4));
        JPasswordField f = new JPasswordField();
        styleTextField(f);
        p.add(f);
        p.add(Box.createVerticalStrut(12));
        return f;
    }

    private void addTitle(JPanel p, String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEADING);
        l.setForeground(TEXT_MAIN);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);
    }

    private void addSubtitle(JPanel p, String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_SMALL);
        l.setForeground(TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(Box.createVerticalStrut(5));
        p.add(l);
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_SMALL);
        l.setForeground(TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel makeFeedbackLabel() {
        JLabel l = new JLabel(" ");
        l.setFont(FONT_SMALL);
        l.setForeground(DANGER);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void styleTextField(JComponent f) {
        f.setBackground(SURFACE_HI);
        f.setForeground(TEXT_MAIN);
        f.setFont(FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(7, 12, 7, 12)
        ));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (f instanceof JTextField) ((JTextField) f).setCaretColor(ACCENT);
        if (f instanceof JPasswordField) ((JPasswordField) f).setCaretColor(ACCENT);
    }

    private void styleCombo(JComboBox<?> cb) {
        cb.setBackground(SURFACE_HI);
        cb.setForeground(TEXT_MAIN);
        cb.setFont(FONT_BODY);
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        cb.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JButton makeAccentButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? ACCENT.brighter() : ACCENT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(BG);
        btn.setFont(FONT_BTN);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 28, 10, 28));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        return btn;
    }

    private JButton makeTabButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_TAB);
        btn.setBackground(BG);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void selectTab(JButton btn, boolean active) {
        btn.setForeground(active ? ACCENT : TEXT_MUTED);
        btn.setFont(active
            ? new Font("Segoe UI", Font.BOLD, 14)
            : FONT_TAB);
    }

    private JPanel wrapCentered(JPanel inner) {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(BG);
        outer.add(inner);
        return outer;
    }

    // ── Login action ───────────────────────────────────────────
    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loginFeedback.setForeground(DANGER);
            String name = loginName.getText().trim();
            String pass = new String(loginPassword.getPassword());

            if (name.isEmpty() || pass.isEmpty()) {
                loginFeedback.setText("Username and password are required.");
                return;
            }
            try {
                UserDB udb = new UserDB();
                User u = udb.getByName(name);
                if (u != null && u.getName().equals(name) && u.getPassword().equals(pass)) {
                    loginFeedback.setForeground(SUCCESS);
                    loginFeedback.setText("✓  Login successful!");
                    new MealIUD(u.getId(), u.getName(), u.getAge(), u.getGender(), u.getPassword());
                    frame.dispose();
                } else {
                    loginFeedback.setText("Incorrect username or password.");
                }
            } catch (Exception ex) {
                loginFeedback.setText("Login failed — please try again.");
            }
        }
    }

    // ── Register action ────────────────────────────────────────
    class RegisterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            regFeedback.setForeground(DANGER);
            String name = regName.getText().trim();
            String age  = regAge.getText().trim();
            String pass = new String(regPassword.getPassword());

            if (name.isEmpty() || age.isEmpty() || pass.isEmpty()) {
                regFeedback.setText("Name, age, and password are required.");
                return;
            }
            if (pass.length() < 4) {
                regFeedback.setText("Password must be at least 4 characters.");
                return;
            }
            try {
                int ageInt = Integer.parseInt(age);
                if (ageInt < 5 || ageInt > 120) {
                    regFeedback.setText("Please enter a valid age (5–120).");
                    return;
                }
                User u = new User();
                u.setName(name);
                u.setAge(ageInt);
                u.setGender((String) regGender.getSelectedItem());
                u.setPassword(pass);
                String hStr = regHeight.getText().trim();
                if (!hStr.isEmpty()) u.setHeight(Double.parseDouble(hStr));

                new UserDB().insert(u);

                regFeedback.setForeground(SUCCESS);
                regFeedback.setText("✓  Account created! Switch to Sign In.");
                regName.setText(""); regAge.setText("");
                regPassword.setText(""); regHeight.setText("");
                regGender.setSelectedIndex(0);
            } catch (NumberFormatException ex) {
                regFeedback.setText("Age and height must be numbers.");
            }
        }
    }
}