package services;

import impl.FoodDB;
import models.Food;
import javax.swing.*;
import java.io.*;
import java.util.List;

public class FoodImporter extends SwingWorker<Void, String> {
    private JLabel statusLabel;
    private FoodDB foodDB;

    public FoodImporter(JLabel statusLabel) {
        this.statusLabel = statusLabel;
        this.foodDB = new FoodDB();
    }

    @Override
    protected Void doInBackground() throws Exception {
        publish("Loading food database...");
        
        // Failas turi būti src/foods_seed.csv
        try (InputStream is = getClass().getResourceAsStream("/foods_seed.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            
            if (is == null) {
                publish("Error: foods_seed.csv not found in src!");
                return null;
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    try {
                        Food f = new Food(
                            data[0].trim(),
                            Double.parseDouble(data[1].trim()),
                            Double.parseDouble(data[2].trim()),
                            Double.parseDouble(data[3].trim()),
                            Double.parseDouble(data[4].trim())
                        );
                        foodDB.insert(f);
                    } catch (NumberFormatException e) {
                        continue; // Praleidžiame blogas eilutes
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            publish("Import failed!");
        }
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        for (String msg : chunks) {
            statusLabel.setText(msg);
        }
    }

    @Override
    protected void done() {
        statusLabel.setText("Food database ready.");
    }
}
