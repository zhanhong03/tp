package seedu.equipmentmaster.storage;

import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.semester.AcademicSemester;
import seedu.equipmentmaster.ui.Ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that handles the storage of the equipment list.
 * It reads data from and writes data to a specified .txt file.
 */
public class Storage {
    private String filePath;
    private Ui ui;
    private String settingsPath = "data/settings.txt";

    /**
     * Constructor.
     * @param filePath The relative path to the .txt storage file.
     */
    public Storage(String filePath, Ui ui) {
        this.filePath = filePath;
        this.ui = ui;
    }

    /**
     * Saves the current list of equipment to the .txt file.
     * @param equipments The current list of equipment.
     */
    public void save(ArrayList<Equipment> equipments){
        try {
            File file = new File(filePath);
            File directory = file.getParentFile();
            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }

            try (FileWriter writer = new FileWriter(filePath)) {
                for (Equipment equipment : equipments) {
                    writer.write(equipment.toFileString() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            ui.showMessage("Error saving equipment data: " + e.getMessage());
        }
    }

    /**
     * Loads the equipment list stored in the .txt file.
     * @return The list of equipment from the file. Returns an empty list if the file is not found.
     */
    public ArrayList<Equipment> load() {
        ArrayList<Equipment> equipments = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return equipments;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Equipment eq = parseEquipment(line);
                if (eq != null) {
                    equipments.add(eq);
                }
            }
        } catch (Exception e) {
            ui.showMessage("Error loading equipment data: " + e.getMessage());
        }
        return equipments;
    }

    /**
     * Converts a formatted string from the .txt file into an Equipment object.
     * Handles names containing "|" by parsing data columns from the end of the string.
     */
    private Equipment parseEquipment(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        final String delim = " | ";
        int s1 = line.lastIndexOf(delim);
        if (s1 == -1) {
            return null;
        }
        int s2 = line.lastIndexOf(delim, s1 - 1);
        int s3 = (s2 == -1) ? -1 : line.lastIndexOf(delim, s2 - 1);
        int s4 = (s3 == -1) ? -1 : line.lastIndexOf(delim, s3 - 1);
        int s5 = (s4 == -1) ? -1 : line.lastIndexOf(delim, s4 - 1);
        int s6 = (s5 == -1) ? -1 : line.lastIndexOf(delim, s5 - 1);

        if (s6 != -1) {
            try {
                String name = line.substring(0, s6).trim();
                int q = Integer.parseInt(line.substring(s6 + 3, s5).trim());
                int a = Integer.parseInt(line.substring(s5 + 3, s4).trim());
                int l = Integer.parseInt(line.substring(s4 + 3, s3).trim());
                int min = Integer.parseInt(line.substring(s3 + 3, s2).trim());
                AcademicSemester sem = new AcademicSemester(line.substring(s2 + 3, s1).trim());
                double life = Double.parseDouble(line.substring(s1 + 3).trim());
                return new Equipment(name, q, a, l, sem, life, min);
            } catch (Exception e) {
                // Fall through to Case 2 if parsing fails
            }
        }

        if (s5 != -1) {
            try {
                String name = line.substring(0, s5).trim();
                int q = Integer.parseInt(line.substring(s5 + 3, s4).trim());
                int a = Integer.parseInt(line.substring(s4 + 3, s3).trim());
                int l = Integer.parseInt(line.substring(s3 + 3, s2).trim());
                AcademicSemester sem = new AcademicSemester(line.substring(s2 + 3, s1).trim());
                double life = Double.parseDouble(line.substring(s1 + 3).trim());
                return new Equipment(name, q, a, l, sem, life, 0);
            } catch (Exception e) {
                // Fall through to Case 3 if parsing fails
            }
        }

        if (s3 != -1) {
            try {
                String name = line.substring(0, s3).trim();
                int q = Integer.parseInt(line.substring(s3 + 3, s2).trim());
                int a = Integer.parseInt(line.substring(s2 + 3, s1).trim());
                int l = Integer.parseInt(line.substring(s1 + 3).trim());
                return new Equipment(name, q, a, l);
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    /**
     * Saves the current system semester to the settings file.
     * @param currentSem The semester to be saved.
     */
    public void saveSettings(AcademicSemester currentSem) {
        Logger storageLogger = Logger.getLogger(Storage.class.getName());
        try {
            File file = new File(settingsPath);
            File directory = file.getParentFile();
            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }

            try (FileWriter writer = new FileWriter(settingsPath)) {
                writer.write(currentSem.toString());
            }
            storageLogger.log(Level.INFO, "Successfully saved semester settings to file.");
        } catch (IOException e) {
            ui.showMessage("Error saving settings: " + e.getMessage());
            storageLogger.log(Level.SEVERE, "Failed to save settings: " + e.getMessage());
        }
    }

    /**
     * Loads the system semester from the settings file.
     * @return The saved semester as a String, or a default value if not found.
     */
    public String loadSettings() {
        File file = new File(settingsPath);
        if (!file.exists()) {
            return "AY2024/25 Sem1"; // Default value
        }

        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextLine()) {
                return scanner.nextLine().trim();
            }
        } catch (FileNotFoundException e) {
            // Fallback to default
        }
        return "AY2024/25 Sem1";
    }
}
