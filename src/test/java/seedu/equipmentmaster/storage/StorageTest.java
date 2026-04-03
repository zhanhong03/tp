//@@author Hongyu1231
package seedu.equipmentmaster.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.semester.AcademicSemester;
import seedu.equipmentmaster.ui.Ui;
import seedu.equipmentmaster.module.Module;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class StorageTest {

    private static final Ui ui = new Ui();

    @TempDir
    Path tempDir; // JUnit creates a temporary directory for file tests

    /**
     * Helper method to create a Storage instance with isolated temporary files for all paths.
     */
    private Storage createStorage() {
        return new Storage(
                tempDir.resolve("test_eq.txt").toString(),
                ui,
                tempDir.resolve("test_set.txt").toString(),
                tempDir.resolve("test_mod.txt").toString()
        );
    }

    @Test
    public void saveAndLoadSettings_validSemester_success() throws EquipmentMasterException {
        Storage storage = createStorage();

        AcademicSemester originalSem = new AcademicSemester("AY2025/26 Sem2");
        storage.saveSettings(originalSem);

        String loadedSemStr = storage.loadSettings();
        assertEquals("AY2025/26 Sem2", loadedSemStr);
    }

    @Test
    public void saveAndLoad_validEquipmentList_success() throws EquipmentMasterException {
        Storage storage = createStorage();
        ArrayList<Equipment> originalList = new ArrayList<>();

        AcademicSemester sem1 = new AcademicSemester("AY2025/26 Sem1");
        AcademicSemester sem2 = new AcademicSemester("AY2025/26 Sem2");

        originalList.add(new Equipment("STM32 Board", 50, 45, 5, sem1, 5.0, 0));
        originalList.add(new Equipment("HDMI Cable", 100, 100, 0, sem2, 2.5, 0));

        // Act: Save the list to the text file, then immediately load it back
        storage.save(originalList);
        ArrayList<Equipment> loadedList = storage.load();

        // Assert: Verify that the loaded list has the exact same data
        assertEquals(2, loadedList.size());

        Equipment firstEquipment = loadedList.get(0);
        assertEquals("STM32 Board", firstEquipment.getName());
        assertEquals(50, firstEquipment.getQuantity());
        assertEquals(45, firstEquipment.getAvailable());
        assertEquals(5, firstEquipment.getLoaned());
        assertEquals(sem1.toString(), firstEquipment.getPurchaseSem().toString());
        assertEquals(5.0, firstEquipment.getLifespanYears());
    }

    @Test
    public void load_noExistingFile_returnsEmptyList() {
        // Arrange: Pass the nonexistent file as the first argument (equipment path)
        Storage storage = new Storage(
                tempDir.resolve("nonexistent.txt").toString(),
                ui,
                "dummy_set.txt",
                "dummy_mod.txt"
        );

        // Act: Attempt to load from the non-existent file
        ArrayList<Equipment> loadedList = storage.load();

        // Assert: The returned list should be empty, not null
        assertTrue(loadedList.isEmpty());
    }

    @Test
    public void parseEquipment_nameWithDelimiters_success() {
        Path testFile = tempDir.resolve("test.txt");
        Storage storage = new Storage(
                testFile.toString(),
                ui,
                "dummy_set.txt",
                "dummy_mod.txt"
        );

        String trickyLine = "Special | Adapter | 50 | 45 | 5 | 0 | AY2025/26 Sem1 | 3.5 | ";

        try (FileWriter writer = new FileWriter(testFile.toFile())) {
            writer.write(trickyLine + System.lineSeparator());
        } catch (IOException e) {
            fail("Setup failed: " + e.getMessage());
        }

        ArrayList<Equipment> loaded = storage.load();

        assertEquals(1, loaded.size());

        Equipment loadedEquipment = loaded.get(0);
        assertEquals("Special | Adapter", loadedEquipment.getName());
        assertEquals(50, loadedEquipment.getQuantity());
        assertEquals("AY2025/26 Sem1", loadedEquipment.getPurchaseSem().toString());
        assertEquals(3.5, loadedEquipment.getLifespanYears());
        assertTrue(loadedEquipment.getModuleCodes().isEmpty());
    }

    @Test
    public void loadModules_fileDoesNotExist_returnsEmptyList() {
        File tempFile = tempDir.resolve("missing_modules.txt").toFile();

        // Pass tempFile.getAbsolutePath() as the 4th argument so it reads the temp file
        Storage storage = new Storage("dummy_eq.txt", ui, "dummy_set.txt", tempFile.getAbsolutePath());

        ModuleList loadedList = storage.loadModules();

        assertTrue(loadedList.getModules().isEmpty(), "Loaded list should be empty when file is missing.");
    }

    @Test
    public void loadModules_corruptedData_returnsEmptyList() throws IOException {
        File tempFile = tempDir.resolve("corrupted_modules.txt").toFile();
        FileWriter fw = new FileWriter(tempFile);
        fw.write("CG2111A | oneHundredAndFifty\n");
        fw.close();

        // Pass tempFile.getAbsolutePath() as the 4th argument
        Storage storage = new Storage("dummy_eq.txt", ui, "dummy_set.txt", tempFile.getAbsolutePath());

        ModuleList loadedList = storage.loadModules();

        assertTrue(loadedList.getModules().isEmpty(), "Loaded list should be empty when data is corrupted.");
    }

    @Test
    public void saveAndLoadModules_validData_success() throws EquipmentMasterException {
        File tempFile = tempDir.resolve("valid_modules.txt").toFile();

        // Pass tempFile.getAbsolutePath() as the 4th argument
        Storage storage = new Storage("dummy_eq.txt", ui, "dummy_set.txt", tempFile.getAbsolutePath());

        ModuleList originalList = new ModuleList();
        originalList.addModule(new Module("CG2028", 120));

        storage.saveModules(originalList);
        ModuleList loadedList = storage.loadModules();

        assertEquals(1, loadedList.getModules().size());
        assertEquals("CG2028", loadedList.getModules().get(0).getName());
        assertEquals(120, loadedList.getModules().get(0).getPax());
    }

    @Test
    public void save_ioException_showsErrorMessage() {
        Path barrierFile = tempDir.resolve("barrier.txt");
        try {
            barrierFile.toFile().createNewFile();
        } catch (IOException e) {
            fail("Setup failed: " + e.getMessage());
        }

        Path invalidPath = barrierFile.resolve("equipment.txt");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        Ui testUi = new Ui(System.in, printStream);

        Storage storage = new Storage(
                invalidPath.toString(),
                testUi,
                tempDir.resolve("test_set.txt").toString(),
                tempDir.resolve("test_mod.txt").toString()
        );

        ArrayList<Equipment> equipments = new ArrayList<>();
        equipments.add(new Equipment("TestItem", 10, 10, 0));

        storage.save(equipments);

        String output = outputStream.toString();
        assertTrue(output.contains("Error saving equipment data:"));
    }

    @Test
    public void parseEquipment_nullLine_returnsNull() throws IOException {
        Path testFile = tempDir.resolve("null_test.txt");

        try (FileWriter writer = new FileWriter(testFile.toFile())) {
            writer.write("\n"); // Empty line
        }

        Storage storage = new Storage(
                testFile.toString(),
                ui,
                tempDir.resolve("test_set.txt").toString(),
                tempDir.resolve("test_mod.txt").toString()
        );

        ArrayList<Equipment> loaded = storage.load();
        assertEquals(0, loaded.size());
    }

    @Test
    public void parseEquipment_missingFields_returnsNull() throws IOException {
        Path testFile = tempDir.resolve("missing_fields.txt");

        // Line missing some fields (should cause parse to fail and return null)
        try (FileWriter writer = new FileWriter(testFile.toFile())) {
            writer.write("STM32 | 50 | 45\n"); // Missing many fields
        }

        Storage storage = new Storage(
                testFile.toString(),
                ui,
                tempDir.resolve("test_set.txt").toString(),
                tempDir.resolve("test_mod.txt").toString()
        );

        ArrayList<Equipment> loaded = storage.load();

        // Should skip corrupted line
        assertEquals(0, loaded.size());
    }

    @Test
    public void saveSettings_ioException_showsErrorMessage() {
        Path barrierFile = tempDir.resolve("settings_barrier.txt");
        try {
            barrierFile.toFile().createNewFile();
        } catch (IOException e) {
            fail("Setup failed: " + e.getMessage());
        }

        Path invalidPath = barrierFile.resolve("setting.txt");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        Ui testUi = new Ui(System.in, printStream);

        Storage storage = new Storage(
                tempDir.resolve("test_eq.txt").toString(),
                testUi,
                invalidPath.toString(),
                tempDir.resolve("test_mod.txt").toString()
        );

        try {
            AcademicSemester sem = new AcademicSemester("AY2025/26 Sem1");
            storage.saveSettings(sem);
        } catch (EquipmentMasterException e) {
            fail("Should not throw exception: " + e.getMessage());
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Error saving settings:"));
    }

    @Test
    public void loadSettings_fileExistsWithEmptyContent_returnsDefault() throws IOException {
        Path settingFile = tempDir.resolve("empty_setting.txt");

        try (FileWriter writer = new FileWriter(settingFile.toFile())) {
            writer.write(""); // Empty file
        }

        Storage storage = new Storage(
                tempDir.resolve("test_eq.txt").toString(),
                ui,
                settingFile.toString(),
                tempDir.resolve("test_mod.txt").toString()
        );

        String loaded = storage.loadSettings();
        assertEquals("AY2024/25 Sem1", loaded);
    }

    @Test
    public void loadSettings_fileExistsWithWhitespace_returnsTrimmed() throws IOException {
        Path settingFile = tempDir.resolve("whitespace_setting.txt");

        try (FileWriter writer = new FileWriter(settingFile.toFile())) {
            writer.write("  AY2025/26 Sem2  \n");
        }

        Storage storage = new Storage(
                tempDir.resolve("test_eq.txt").toString(),
                ui,
                settingFile.toString(),
                tempDir.resolve("test_mod.txt").toString()
        );

        String loaded = storage.loadSettings();
        assertEquals("AY2025/26 Sem2", loaded);
    }

    @Test
    public void loadModules_fileWithEmptyLines_skipsEmptyLines() throws IOException, EquipmentMasterException {
        Path moduleFile = tempDir.resolve("empty_lines_modules.txt");

        try (FileWriter writer = new FileWriter(moduleFile.toFile())) {
            writer.write("\n");
            writer.write("CG2111A | 150\n");
            writer.write("\n");
            writer.write("EE2026 | 200\n");
            writer.write("\n");
        }

        Storage storage = new Storage(
                tempDir.resolve("test_eq.txt").toString(),
                ui,
                tempDir.resolve("test_set.txt").toString(),
                moduleFile.toString()
        );

        ModuleList loaded = storage.loadModules();

        assertEquals(2, loaded.getModules().size());
    }

    @Test
    public void loadModules_withRequirements_success() throws IOException, EquipmentMasterException {
        Path moduleFile = tempDir.resolve("modules_with_reqs.txt");

        try (FileWriter writer = new FileWriter(moduleFile.toFile())) {
            writer.write("CG2111A | 150 | STM32=1.0,HDMI=0.5\n");
        }

        Storage storage = new Storage(
                tempDir.resolve("test_eq.txt").toString(),
                ui,
                tempDir.resolve("test_set.txt").toString(),
                moduleFile.toString()
        );

        ModuleList loaded = storage.loadModules();

        assertEquals(1, loaded.getModules().size());
        Module module = loaded.getModules().get(0);
        assertEquals("CG2111A", module.getName());
        assertEquals(150, module.getPax());
        assertEquals(2, module.getEquipmentRequirements().size());
    }

    @Test
    public void loadModules_withInvalidRatio_skipsThatRequirement() throws IOException, EquipmentMasterException {
        Path moduleFile = tempDir.resolve("invalid_ratio_modules.txt");

        try (FileWriter writer = new FileWriter(moduleFile.toFile())) {
            writer.write("CG2111A | 150 | STM32=1.0,HDMI=-0.5,OSC=2.0\n");
        }

        Storage storage = new Storage(
                tempDir.resolve("test_eq.txt").toString(),
                ui,
                tempDir.resolve("test_set.txt").toString(),
                moduleFile.toString()
        );

        ModuleList loaded = storage.loadModules();

        Module module = loaded.getModules().get(0);
        // Should only have valid ratios (STM32 and OSC, not HDMI with negative)
        assertEquals(2, module.getEquipmentRequirements().size());
    }

    @Test
    public void loadModules_withMalformedRequirement_skipsIt() throws IOException, EquipmentMasterException {
        Path moduleFile = tempDir.resolve("malformed_req_modules.txt");

        try (FileWriter writer = new FileWriter(moduleFile.toFile())) {
            writer.write("CG2111A | 150 | STM32=1.0,HDMI=abc,OSC=2.0\n");
        }

        Storage storage = new Storage(
                tempDir.resolve("test_eq.txt").toString(),
                ui,
                tempDir.resolve("test_set.txt").toString(),
                moduleFile.toString()
        );

        ModuleList loaded = storage.loadModules();

        Module module = loaded.getModules().get(0);
        // Should only have valid numeric ratios
        assertEquals(2, module.getEquipmentRequirements().size());
    }

    @Test
    public void saveModules_withRequirements_success() throws EquipmentMasterException, IOException {
        Path moduleFile = tempDir.resolve("save_with_reqs.txt");

        Storage storage = new Storage(
                tempDir.resolve("test_eq.txt").toString(),
                ui,
                tempDir.resolve("test_set.txt").toString(),
                moduleFile.toString()
        );

        ModuleList moduleList = new ModuleList();
        Module module = new Module("CG2111A", 150);
        module.addEquipmentRequirement("STM32", 1.0);
        module.addEquipmentRequirement("HDMI", 0.5);
        moduleList.addModule(module);

        storage.saveModules(moduleList);

        // Verify file content
        try (Scanner scanner = new Scanner(moduleFile)) {
            String line = scanner.nextLine();
            assertTrue(line.contains("CG2111A | 150 |"));
            assertTrue(line.contains("STM32=1.0"));
            assertTrue(line.contains("HDMI=0.5"));
        }
    }
}

