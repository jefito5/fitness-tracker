package services;

import impl.FoodDB;
import models.Food;
import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.List;

public class FoodImporter extends SwingWorker<Void, String> {
    private JLabel statusLabel;
    private FoodDB foodDB;

    public FoodImporter(JLabel statusLabel) {
        this.statusLabel = statusLabel;
        this.foodDB = new FoodDB();
    }

    /**
     * Checks whether a food with the given name already exists in foods_seed.csv
     * (case-insensitive). If not found, appends a new line in the same
     * name,calories,protein,carbs,fat format.
     *
     * @return true if the entry was appended, false if it already existed or the file was not found.
     */
    public static boolean appendIfNew(String name, double calories,
                                      double protein, double carbs, double fat) {
        URL url = FoodImporter.class.getResource("/foods_seed.csv");
        if (url == null) {
            System.err.println("FoodImporter.appendIfNew: foods_seed.csv not found on classpath");
            return false;
        }

        File csvFile;
        try {
            csvFile = new File(url.toURI());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // Check whether the name is already present (case-insensitive)
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].trim().equalsIgnoreCase(name.trim())) {
                    
                    return false; // Already exists – nothing to do
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Not found – append a new line
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(csvFile, true), "UTF-8"))) {
            // Ensure we start on a new line
            bw.newLine();
            bw.write(String.format("%s,%.1f,%.1f,%.1f,%.1f",
                    name.trim(), calories, protein, carbs, fat));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
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
