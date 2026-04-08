package gui;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.Regression;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import database.ConnectionFactory;
import impl.WeightDB;
import models.Weight;

public class TrendLine {

    private JFrame frmTrendLine;
    private JComboBox<Integer> comboBox;
    private JComboBox<Integer> comboBox_1;
    private JComboBox<Integer> comboBox_2;
    private JComboBox<Integer> comboBox_3;
    private JComboBox<Integer> comboBox_4;
    private JComboBox<Integer> comboBox_5;
    private JTextField textField;
    private JLabel lblDay;
    private JLabel label_2;
    private JLabel lblMonth_1;
    private JLabel lblDay_1;
    private Connection connect;
    private int ids;
    private JPanel panel;

    public TrendLine(int id) {
        ids = id;
        connect = ConnectionFactory.getConnection();
        initialize();
    }

    private void initialize() {
        frmTrendLine = new JFrame();
        frmTrendLine.setTitle("Trend Line");
        frmTrendLine.setBounds(100, 100, 1020, 520); 
        frmTrendLine.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frmTrendLine.getContentPane().setLayout(null);
        
        JButton btnBack = new JButton("Back");
        btnBack.setBounds(20, 15, 80, 30);
        frmTrendLine.getContentPane().add(btnBack);
        
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frmTrendLine.dispose(); 
                new PeriodSelect(ids); 
            }
        });

        JLabel lblYourProgressReport = new JLabel("YOUR PROGRESS REPORT ON CHART");
        lblYourProgressReport.setFont(new Font("Verdana", Font.BOLD, 22));
        lblYourProgressReport.setBounds(280, 15, 500, 30);
        frmTrendLine.getContentPane().add(lblYourProgressReport);

        JLabel lblFrom = new JLabel("FROM:");
        lblFrom.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblFrom.setBounds(100, 70, 60, 20);
        frmTrendLine.getContentPane().add(lblFrom);
        
        JLabel lblYear = new JLabel("Year:");
        lblYear.setBounds(160, 70, 40, 20);
        frmTrendLine.getContentPane().add(lblYear);
        
        comboBox = new JComboBox<>();
        comboBox.setBounds(195, 68, 70, 25);
        for(int i=2026;i<=2032;i++) comboBox.addItem(i);
        frmTrendLine.getContentPane().add(comboBox);
        
        JLabel lblMonth = new JLabel("Month:");
        lblMonth.setBounds(275, 70, 50, 20);
        frmTrendLine.getContentPane().add(lblMonth);
        
        comboBox_1 = new JComboBox<>();
        comboBox_1.setBounds(320, 68, 50, 25);
        for(int i=1;i<=12;i++) comboBox_1.addItem(i);
        frmTrendLine.getContentPane().add(comboBox_1);
        
        lblDay = new JLabel("Day:");
        lblDay.setBounds(380, 70, 40, 20);
        frmTrendLine.getContentPane().add(lblDay);
        
        comboBox_2 = new JComboBox<>();
        comboBox_2.setBounds(415, 68, 50, 25);
        for(int i=1;i<=31;i++) comboBox_2.addItem(i);
        frmTrendLine.getContentPane().add(comboBox_2);

        JLabel lblTo = new JLabel("TO:");
        lblTo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTo.setBounds(490, 70, 40, 20);
        frmTrendLine.getContentPane().add(lblTo);
        
        label_2 = new JLabel("Year:");
        label_2.setBounds(530, 70, 40, 20);
        frmTrendLine.getContentPane().add(label_2);
        
        comboBox_3 = new JComboBox<>();
        comboBox_3.setBounds(565, 68, 70, 25);
        for(int i=2026;i<=2032;i++) comboBox_3.addItem(i);
        frmTrendLine.getContentPane().add(comboBox_3);
        
        lblMonth_1 = new JLabel("Month:");
        lblMonth_1.setBounds(645, 70, 50, 20);
        frmTrendLine.getContentPane().add(lblMonth_1);
        
        comboBox_4 = new JComboBox<>();
        comboBox_4.setBounds(690, 68, 50, 25);
        for(int i=1;i<=12;i++) comboBox_4.addItem(i);
        frmTrendLine.getContentPane().add(comboBox_4);
        
        lblDay_1 = new JLabel("Day:");
        lblDay_1.setBounds(750, 70, 40, 20);
        frmTrendLine.getContentPane().add(lblDay_1);
        
        comboBox_5 = new JComboBox<>();
        comboBox_5.setBounds(785, 68, 50, 25);
        for(int i=1;i<=31;i++) comboBox_5.addItem(i);
        frmTrendLine.getContentPane().add(comboBox_5);

        JButton btnNewButton = new JButton("VIEW GRAPH");
        btnNewButton.setBounds(860, 65, 120, 30);      
        frmTrendLine.getContentPane().add(btnNewButton);

        panel = new JPanel();
        panel.setBounds(20, 120, 960, 340);
        panel.setLayout(new BorderLayout(0, 0));
        frmTrendLine.getContentPane().add(panel);

        textField = new JTextField();
        textField.setBounds(3, 11, 86, 20);
        textField.setText(String.valueOf(ids));
        textField.setVisible(false);
        frmTrendLine.getContentPane().add(textField);
        
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                String startYear = comboBox.getSelectedItem().toString();
                String startMonth = String.format("%02d", comboBox_1.getSelectedItem());
                String startDay = String.format("%02d", comboBox_2.getSelectedItem());
                
                String endYear = comboBox_3.getSelectedItem().toString();
                String endMonth = String.format("%02d", comboBox_4.getSelectedItem());
                String endDay = String.format("%02d", comboBox_5.getSelectedItem());
                
                String startDate = startYear + "-" + startMonth + "-" + startDay;
                String endDate = endYear + "-" + endMonth + "-" + endDay;
                
                WeightDB wdb = new WeightDB();
                ArrayList<Weight> weights = wdb.getWeightsByDateRange(ids, startDate, endDate);
                
                if(weights.isEmpty()) {
                    JOptionPane.showMessageDialog(frmTrendLine, "No weight records found for this period!");
                    return;
                }

                TimeSeries weightSeries = new TimeSeries("Average Weight");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                for (Weight w : weights) {
                    try {
                        Date parsedDate = sdf.parse(w.getDate().toString()); 
                        double weightValue = w.getAverage() > 0 ? w.getAverage() : w.getWeightM();
                        weightSeries.addOrUpdate(new Day(parsedDate), weightValue);
                    } catch (Exception ex) {
                        System.out.println("Nepavyko perskaityti datos: " + w.getDate());
                    }
                }

                TimeSeriesCollection dataset = new TimeSeriesCollection();
                dataset.addSeries(weightSeries);

                if (weightSeries.getItemCount() > 1) {
                    double[] regression = Regression.getOLSRegression(dataset, 0);
                    TimeSeries trendSeries = new TimeSeries("Trendline");
                    
                    long firstTime = weightSeries.getTimePeriod(0).getFirstMillisecond();
                    long lastTime = weightSeries.getTimePeriod(weightSeries.getItemCount() - 1).getFirstMillisecond();
                    
                    double firstExpectedWeight = regression[0] + regression[1] * firstTime;
                    double lastExpectedWeight = regression[0] + regression[1] * lastTime;
                    
                    trendSeries.add(weightSeries.getTimePeriod(0), firstExpectedWeight);
                    trendSeries.add(weightSeries.getTimePeriod(weightSeries.getItemCount() - 1), lastExpectedWeight);
                    
                    dataset.addSeries(trendSeries);
                }

                JFreeChart chart = ChartFactory.createTimeSeriesChart(
                        "Average Weight Change", "Date", "Weight (kg)", 
                        dataset, true, true, false);

                XYPlot plot = chart.getXYPlot();

                // --- 1. VERTIKALI AŠIS (WEIGHT) ---
                NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
                rangeAxis.setAutoRangeIncludesZero(false);
                rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
                // PADIDINAME TARPĄ VIRŠUJE, KAD REIKŠMĖ NEBŪTŲ NUPJAUTA
                rangeAxis.setUpperMargin(0.20); 

                // --- 2. HORIZONTALI AŠIS (DATE) ---
                org.jfree.chart.axis.DateAxis domainAxis = (org.jfree.chart.axis.DateAxis) plot.getDomainAxis();

                // Sukuriame savo datų rodymo taisykles (neleidžiame skaidyti į valandas!)
                org.jfree.chart.axis.TickUnits tickUnits = new org.jfree.chart.axis.TickUnits();
                // Trumpam laikotarpiui: rodys kiekvieną dieną
                tickUnits.add(new org.jfree.chart.axis.DateTickUnit(org.jfree.chart.axis.DateTickUnitType.DAY, 1, new java.text.SimpleDateFormat("yyyy-MM-dd")));
                // Vidutiniam laikotarpiui: rodys kas savaitę (kas 7 dienas)
                tickUnits.add(new org.jfree.chart.axis.DateTickUnit(org.jfree.chart.axis.DateTickUnitType.DAY, 7, new java.text.SimpleDateFormat("yyyy-MM-dd")));
                // Ilgam laikotarpiui: rodys kas mėnesį
                tickUnits.add(new org.jfree.chart.axis.DateTickUnit(org.jfree.chart.axis.DateTickUnitType.MONTH, 1, new java.text.SimpleDateFormat("yyyy-MM-dd")));

                // Pritaikome šias taisykles ašiai
                domainAxis.setStandardTickUnits(tickUnits);
                domainAxis.setAutoTickUnitSelection(true); // Programa pati parinks tinkamiausią tarpą iš mūsų sąrašo

                // Paraštės ir vertikalus tekstas
                domainAxis.setLowerMargin(0.15);
                domainAxis.setUpperMargin(0.15);
                domainAxis.setVerticalTickLabels(true); // Datos bus vertikalios

                // --- 3. RENDERER (LINIJOS IR REIKŠMĖS) ---
                XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
                
                // Formatas reikšmėms virš taškų
                java.text.NumberFormat weightFormat = java.text.DecimalFormat.getNumberInstance();
                weightFormat.setMaximumFractionDigits(1); 

                renderer.setDefaultItemLabelGenerator(
                    new StandardXYItemLabelGenerator("{2}", new SimpleDateFormat("yyyy-MM-dd"), weightFormat)
                );
                
                // --- SVARBI DALIS: Rodyti svorį TIK mėlynai serijai (0), ne raudonai (1) ---
                renderer.setSeriesItemLabelsVisible(0, true);
                renderer.setSeriesItemLabelsVisible(1, false); 
                renderer.setDefaultItemLabelFont(new Font("SansSerif", Font.BOLD, 12));
                
                renderer.setSeriesLinesVisible(0, true);
                renderer.setSeriesShapesVisible(0, true); 
                renderer.setSeriesPaint(0, Color.BLUE);
                
                if (dataset.getSeriesCount() > 1) {
                    renderer.setSeriesLinesVisible(1, true);
                    renderer.setSeriesShapesVisible(1, false); 
                    renderer.setSeriesPaint(1, Color.RED);
                    renderer.setSeriesStroke(1, new java.awt.BasicStroke(2.5f)); 
                }
                
                plot.setRenderer(renderer);

                ChartPanel chartPanel = new ChartPanel(chart);
                panel.removeAll();
                panel.add(chartPanel, BorderLayout.CENTER);
                panel.validate();
                panel.repaint();
            }
        });
        
        frmTrendLine.setVisible(true);
    }
}