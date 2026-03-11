package seedu.EquipmentMaster.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import seedu.EquipmentMaster.equipment.Equipment;
import seedu.EquipmentMaster.ui.Ui;

import java.io.File;
import java.util.ArrayList;

public class StorageTest {

    // Define a specific file path just for testing purposes
    private static final String TEST_FILE_PATH = "test_equipment.txt";

    @AfterEach
    public void tearDown() {
        // Clean up: Delete the test file after each test to ensure a fresh environment
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void saveAndLoad_validEquipmentList_success() {
        Ui ui = new Ui();
        // Arrange: Create a Storage object and a dummy list of equipment
        Storage storage = new Storage(TEST_FILE_PATH, ui);
        ArrayList<Equipment> originalList = new ArrayList<>();

        // Using your newly created 4-argument constructor!
        originalList.add(new Equipment("STM32 Board", 50, 45, 5));
        originalList.add(new Equipment("HDMI Cable", 100, 100, 0));

        // Act: Save the list to the text file, then immediately load it back into a new list
        storage.save(originalList);
        ArrayList<Equipment> loadedList = storage.load();

        // Assert: Verify that the loaded list has the exact same data as the original list
        assertEquals(2, loadedList.size());

        // Check the attributes of the first equipment
        Equipment firstEquipment = loadedList.get(0);
        assertEquals("STM32 Board", firstEquipment.getName());
        assertEquals(50, firstEquipment.getQuantity());
        assertEquals(45, firstEquipment.getAvailable());
        assertEquals(5, firstEquipment.getLoaned());
    }

    @Test
    public void load_noExistingFile_returnsEmptyList() {
        Ui ui = new Ui();
        // Arrange: Ensure the test file definitely does not exist
        Storage storage = new Storage(TEST_FILE_PATH, ui);
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }

        // Act: Attempt to load from the non-existent file
        ArrayList<Equipment> loadedList = storage.load();

        // Assert: The returned list should be empty, not null, preventing NullPointerExceptions
        assertTrue(loadedList.isEmpty());
    }
}