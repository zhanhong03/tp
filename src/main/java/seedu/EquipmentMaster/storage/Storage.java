package seedu.EquipmentMaster.storage;

import seedu.EquipmentMaster.equipment.Equipment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class that handles the storage of the equipment list.
 * It reads data from and writes data to a specified .txt file.
 */
public class Storage {
    private String filePath;

    /**
     * Constructor.
     * @param filePath The relative path to the .txt storage file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the current list of equipment to the .txt file.
     * @param equipments The current list of equipment.
     */
    public void save(ArrayList<Equipment> equipments) {
        try {
            File file = new File(filePath);
            File directory = file.getParentFile();

            // Create directory if it doesn't exist (e.g., creating the "data" folder)
            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }

            try (FileWriter writer = new FileWriter(filePath)) {
                for (Equipment equipment : equipments) {
                    // Make sure your Equipment class has a toFileString() method!
                    writer.write(equipment.toFileString() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving equipment data: " + e.getMessage());
        }
    }

    /**
     * Loads the equipment list stored in the .txt file.
     * @return The list of equipment from the file. Returns an empty list if the file is not found.
     */
    public ArrayList<Equipment> load() {
        ArrayList<Equipment> equipments = new ArrayList<>();
        File file = new File(filePath);

        // If file doesn't exist, return an empty list to start fresh
        if (!file.exists()) {
            return equipments;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Equipment equipment = parseEquipment(line);

                // Only add if the line was not corrupted
                if (equipment != null) {
                    equipments.add(equipment);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        return equipments;
    }

    /**
     * Converts a formatted string from the .txt file into an Equipment object.
     * @param line A single line of text from the save file.
     * @return An Equipment object, or null if the string format is corrupted.
     */
    private Equipment parseEquipment(String line) {
        // Expected format: Name | Quantity | Available | Loaned
        // Example line: STM32 Development Board | 45 | 5 | 30
        String[] parts = line.split(" \\| ");

        // Stretch goal: Handle corrupted data
        if (parts.length < 3) {
            return null;
        }

        try {
            String name = parts[0];
            int availableQuantity = Integer.parseInt(parts[1]);
            int loanedQuantity = Integer.parseInt(parts[2]);

            // Create the equipment (assuming your constructor takes name and TOTAL quantity)
            int totalQuantity = availableQuantity + loanedQuantity;
            Equipment equipment = new Equipment(name, totalQuantity, availableQuantity, loanedQuantity);

            return equipment;

        } catch (NumberFormatException e) {
            // If the quantities in the text file are not valid numbers, ignore the line
            return null;
        }
    }
}