package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
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

import theme.UITheme;
import components.*;

public class TrendLine {

    private JFrame frmTrendLine;
    private StyledComboBox<Integer> comboBox, comboBox_1, comboBox_2, comboBox_3, comboBox_4, comboBox_5;
    private int ids;
    private CardPanel chartContainer;

    public TrendLine(int id) {
        this.ids = id;
        initialize();
    }

    private void initialize() {
        frmTrendLine = new JFrame("Weight Trend Analysis");
        frmTrendLine.setBounds(100, 100, 1100, 650); 
        frmTrendLine.setLocationRelativeTo(null);
        frmTrendLine.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frmTrendLine.getContentPane().setLayout(null);
        frmTrendLine.getContentPane().setBackground(UITheme.BACKGROUND);
        
        // Atgal mygtukas
        RoundedButton btnBack = new RoundedButton("Back");
        btnBack.setBounds(20, 15, 80, 35);
        frmTrendLine.getContentPane().add(btnBack);
        btnBack.addActionListener(e -> {
            frmTrendLine.dispose(); 
            new PeriodSelect(ids); 
        });

        SectionHeader lblHeader = new SectionHeader("WEIGHT PROGRESS & TREND ANALYSIS");
        lblHeader.setBounds(280, 15, 600, 35);
        frmTrendLine.getContentPane().add(lblHeader);

// --- FILTRAI ---
        int filterY = 75;
        JLabel lblFrom = new JLabel("FROM:"); 
        lblFrom.setFont(UITheme.FONT_SUBTITLE); 
        lblFrom.setBounds(40, filterY, 60, 30); 
        frmTrendLine.getContentPane().add(lblFrom);
        
        Integer[] years = {2026, 2027, 2028, 2029, 2030};
        Integer[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        Integer[] days = new Integer[31]; for(int i=0; i<31; i++) days[i] = i+1;

        comboBox = new StyledComboBox<>(years); 
        comboBox.setBounds(105, filterY, 90, 30); 
        
        comboBox_1 = new StyledComboBox<>(months); 
        comboBox_1.setBounds(200, filterY, 75, 30); 
        
        comboBox_2 = new StyledComboBox<>(days); 
        comboBox_2.setBounds(280, filterY, 75, 30); 
        
        frmTrendLine.add(comboBox); frmTrendLine.add(comboBox_1); frmTrendLine.add(comboBox_2);

        JLabel lblTo = new JLabel("TO:"); 
        lblTo.setFont(UITheme.FONT_SUBTITLE); 
        lblTo.setBounds(370, filterY, 40, 30); 
        frmTrendLine.add(lblTo);
        
        comboBox_3 = new StyledComboBox<>(years); 
        comboBox_3.setBounds(410, filterY, 90, 30); 
        
        comboBox_4 = new StyledComboBox<>(months); 
        comboBox_4.setBounds(505, filterY, 75, 30); 
        
        comboBox_5 = new StyledComboBox<>(days); 
        comboBox_5.setBounds(585, filterY, 75, 30); 
        
        frmTrendLine.add(comboBox_3); frmTrendLine.add(comboBox_4); frmTrendLine.add(comboBox_5);

        RoundedButton btnView = new RoundedButton("VIEW GRAPH");
        btnView.setBounds(675, filterY, 160, 32);      
        frmTrendLine.getContentPane().add(btnView);

        // Pagrindinis konteineris grafikui
        chartContainer = new CardPanel();
        chartContainer.setBounds(20, 130, 1040, 450);
        chartContainer.setLayout(new BorderLayout());
        frmTrendLine.getContentPane().add(chartContainer);
        
        btnView.addActionListener(e -> updateChart());
        
        frmTrendLine.setVisible(true);
    }

    private void updateChart() {
        String startDate = String.format("%04d-%02d-%02d", comboBox.getSelectedItem(), comboBox_1.getSelectedItem(), comboBox_2.getSelectedItem());
        String endDate = String.format("%04d-%02d-%02d", comboBox_3.getSelectedItem(), comboBox_4.getSelectedItem(), comboBox_5.getSelectedItem());
        
        WeightDB wdb = new WeightDB();
        ArrayList<Weight> weights = wdb.getWeightsByDateRange(ids, startDate, endDate);
        
        if(weights.isEmpty()) {
            StyledMessage.show("Info", "No records found for this period.");
            return;
        }

        TimeSeries series = new TimeSeries("Daily Average");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Weight w : weights) {
            try {
                // Konvertuojame SQL datą į JFreeChart suprantamą formatą
                Date d = java.sql.Date.valueOf(w.getDate().toString());
                double val = w.getAverage() > 0 ? w.getAverage() : w.getWeightM();
                series.addOrUpdate(new Day(d), val);
            } catch (Exception ignored) {}
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        // Tendencijos linija (Regresija), jei yra bent 2 įrašai
        if (series.getItemCount() > 1) {
            double[] regression = Regression.getOLSRegression(dataset, 0);
            TimeSeries trend = new TimeSeries("Trendline");
            long t1 = series.getTimePeriod(0).getFirstMillisecond();
            long t2 = series.getTimePeriod(series.getItemCount() - 1).getFirstMillisecond();
            trend.add(series.getTimePeriod(0), regression[0] + regression[1] * t1);
            trend.add(series.getTimePeriod(series.getItemCount() - 1), regression[0] + regression[1] * t2);
            dataset.addSeries(trend);
        }

        JFreeChart chart = ChartFactory.createTimeSeriesChart("Weight Change Analysis", "Date", "Weight (kg)", dataset, true, true, false);
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(UITheme.BORDER);
        plot.setRangeGridlinePaint(UITheme.BORDER);

        // --- X AŠIS (Išmanusis datų valdymas) ---
        DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
        domainAxis.setDateFormatOverride(sdf);
        domainAxis.setVerticalTickLabels(true); // Kad nesuliptų
        domainAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);

        // --- Y AŠIS ---
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0.25); 

        // --- RENDERER (Išmanusis atvaizdavimas) ---
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        
        // Išmanioji logika: rodome taškus ir skaičius TIK jei duomenų nėra per daug
        boolean showDetails = weights.size() < 40; 
        
        renderer.setSeriesShapesVisible(0, showDetails);
        renderer.setSeriesItemLabelsVisible(0, showDetails);
        
        if (showDetails) {
            java.text.DecimalFormat df = new java.text.DecimalFormat("##.#");
            renderer.setDefaultItemLabelGenerator(new StandardXYItemLabelGenerator("{2}", sdf, df));
            renderer.setDefaultItemLabelFont(new Font("SansSerif", Font.BOLD, 11));
        }

        renderer.setSeriesPaint(0, UITheme.PRIMARY); // Mėlyna progresui
        
        if (dataset.getSeriesCount() > 1) {
            renderer.setSeriesPaint(1, UITheme.ERROR); // Raudona tendencijai
            renderer.setSeriesStroke(1, new java.awt.BasicStroke(2.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND, 1.0f, new float[]{8.0f, 8.0f}, 0.0f));
            renderer.setSeriesShapesVisible(1, false);
        }
        
        plot.setRenderer(renderer);

        // Pridedame ChartPanel su Zoom funkcija (pelės ratuku)
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setDisplayToolTips(true);

        chartContainer.removeAll();
        chartContainer.add(chartPanel, BorderLayout.CENTER);
        chartContainer.validate();
    }
}