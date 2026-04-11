package seedu.equipmentmaster.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.module.Module;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.semester.AcademicSemester;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;

import java.nio.file.Path;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class AddCommandTest {
    private static final String TEST_FILE_PATH = "test_equipment.txt";
    private static final String TEST_SETTING_FILE_PATH = "test_setting.txt";
    private static final String TEST_MODULE_FILE_PATH = "test_module.txt";

    @TempDir
    Path tempDir;

    private Context context;
    private EquipmentList equipmentList;
    private ModuleList moduleList;
    private Ui ui;
    private Storage storage;

    @BeforeEach
    public void setUp() throws EquipmentMasterException {
        equipmentList = new EquipmentList();
        moduleList = new ModuleList();
        ui = new Ui();
        storage = new Storage(tempDir.resolve("test_equipment.txt").toString(),
                ui, tempDir.resolve("test_setting.txt").toString(), tempDir.resolve("test_module.txt").toString());
        // Replace UiStub and StorageStub with however you mock these in your project
        context = new Context(equipmentList, moduleList, ui,storage, new AcademicSemester("AY2024/25 Sem1"));
    }

    @Test
    public void execute_validEquipment_addsToList() throws EquipmentMasterException {
        // Arrange
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        ModuleList moduleList = new ModuleList();
        AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
        Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);

        // Test basic add (no modules, no semester/lifespan)
        AddCommand command = new AddCommand("STM32", 5);

        // Act
        command.execute(context);

        // Assert
        assertEquals(1, equipments.getSize());

        Equipment added = equipments.getEquipment(0);
        assertEquals("STM32", added.getName());
        assertEquals(5, added.getQuantity());
        assertEquals(null, added.getPurchaseSem());
        assertEquals(0.0, added.getLifespanYears(), 0.0001);
        assertTrue(added.getModuleCodes().isEmpty());
    }

    @Test
    public void execute_validEquipmentWithSemesterAndLifespan_addsToList() throws EquipmentMasterException {

        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        ModuleList moduleList = new ModuleList();
        AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
        Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);

        // 1. Create dummy data for your new fields
        AcademicSemester testSem = new AcademicSemester("AY2023/24 Sem1");
        double testLifespan = 5.5;

        // 2. Pass all 4 arguments into the AddCommand constructor
        AddCommand command = new AddCommand("STM32", 5, testSem, testLifespan, 0,
                new ArrayList<>());

        command.execute(context);

        // Assert
        assertEquals(1, equipments.getSize());

        Equipment added = equipments.getEquipment(0);
        assertEquals("STM32", added.getName());
        assertEquals(5, added.getQuantity());
        assertEquals(testSem, added.getPurchaseSem());
        assertEquals(testLifespan, added.getLifespanYears(), 0.0001);
        assertTrue(added.getModuleCodes().isEmpty());
    }

    @Test
    public void execute_validEquipmentWithModules_addsToList() {
        // Arrange
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        ModuleList moduleList = new ModuleList();
        try {
            AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
            Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);

            ArrayList<String> modules = new ArrayList<>();
            modules.add("EE2026");
            modules.add("CG2028");

            AddCommand command = new AddCommand("FPGA", 40, modules);

            // Act
            command.execute(context);

            // Assert
            assertEquals(1, equipments.getSize());

            Equipment added = equipments.getEquipment(0);
            assertEquals("FPGA", added.getName());
            assertEquals(40, added.getQuantity());
            assertEquals(2, added.getModuleCodes().size());
            assertTrue(added.getModuleCodes().contains("EE2026"));
            assertTrue(added.getModuleCodes().contains("CG2028"));
            assertEquals(null, added.getPurchaseSem());
            assertEquals(0.0, added.getLifespanYears(), 0.0001);
        } catch (EquipmentMasterException e) {
            fail("Test setup failed unexpectedly: " + e.getMessage());
        }
    }

    @Test
    public void execute_validEquipmentWithAllFields_addsToList() throws EquipmentMasterException {
        // Arrange
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        ModuleList moduleList = new ModuleList();
        AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
        Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);

        AcademicSemester testSem = new AcademicSemester("AY2023/24 Sem1");
        double testLifespan = 5.5;
        ArrayList<String> modules = new ArrayList<>();
        modules.add("EE2026");
        modules.add("CG2028");

        AddCommand command = new AddCommand("FPGA", 40, testSem, testLifespan, 0, modules);

        // Act
        command.execute(context);

        // Assert
        assertEquals(1, equipments.getSize());

        Equipment added = equipments.getEquipment(0);
        assertEquals("FPGA", added.getName());
        assertEquals(40, added.getQuantity());
        assertEquals(testSem, added.getPurchaseSem());
        assertEquals(testLifespan, added.getLifespanYears(), 0.0001);
        assertEquals(2, added.getModuleCodes().size());
        assertTrue(added.getModuleCodes().contains("EE2026"));
        assertTrue(added.getModuleCodes().contains("CG2028"));
    }

    @Test
    public void execute_addAtMinThreshold_showsLowStockAlert() {
        EquipmentList equipments = new EquipmentList();
        ModuleList moduleList = new ModuleList();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Ui ui = new Ui(System.in, new PrintStream(outputStream));
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        try {
            AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
            Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);


            AddCommand command = new AddCommand("Resistor", 5, null, 0.0, 5, new ArrayList<>());

            command.execute(context);

            String output = outputStream.toString();
            assertTrue(output.contains("!!! LOW STOCK ALERT: Resistor"));
        } catch (EquipmentMasterException e) {
            fail("Test setup failed unexpectedly: " + e.getMessage());
        }
    }

    @Test
    public void execute_addAboveMinThreshold_noLowStockAlert() {
        EquipmentList equipments = new EquipmentList();
        ModuleList moduleList = new ModuleList();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Ui ui = new Ui(System.in, new PrintStream(outputStream));
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        try {
            AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
            Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);

            AddCommand command = new AddCommand("Resistor", 10, null, 0.0, 5, new ArrayList<>());

            command.execute(context);

            String output = outputStream.toString();
            assertTrue(!output.contains("!!! LOW STOCK ALERT:"));
        } catch (EquipmentMasterException e) {
            fail("Test setup failed unexpectedly: " + e.getMessage());
        }
    }

    @Test
    public void addEquipment_duplicateEquipmentWithNewModules_mergesQuantitiesAndModules()
            throws EquipmentMasterException {
        // Setup
        EquipmentList equipmentList = new EquipmentList();
        AcademicSemester sem = new AcademicSemester("AY2024/25 Sem1");

        // 1. Create and add initial equipment (10 units, module: CG2211)
        ArrayList<String> initialModules = new ArrayList<>(Arrays.asList("CG2211"));
        Equipment firstEquipment = new Equipment("STM32", 10, 10, 0, sem, 5.0, initialModules, 0, 0.0);
        equipmentList.addEquipment(firstEquipment);

        // 2. Create and add the same equipment (5 units, modules: CG2211 [duplicate], EE2211 [new])
        ArrayList<String> newModules = new ArrayList<>(Arrays.asList("CG2211", "EE2211"));
        Equipment secondEquipment = new Equipment("STM32", 5, 5, 0, sem, 5.0, newModules, 0, 0.0);
        equipmentList.addEquipment(secondEquipment);

        // 3. Verify the list size hasn't grown (it should have merged)
        assertEquals(1, equipmentList.getSize(), "Equipment list size should remain 1 after merging.");

        // 4. Verify the quantities merged correctly
        Equipment mergedEquipment = equipmentList.getEquipment(0);
        assertEquals(15, mergedEquipment.getQuantity(), "Total quantity should be 10 + 5 = 15.");
        assertEquals(15, mergedEquipment.getAvailable(), "Available quantity should be 10 + 5 = 15.");

        // 5. Verify the modules merged correctly without duplicates
        ArrayList<String> mergedModules = mergedEquipment.getModuleCodes();
        assertEquals(2, mergedModules.size(), "Module list should contain exactly 2 distinct modules.");
        assertTrue(mergedModules.contains("CG2211"), "Merged modules should contain CG2211.");
        assertTrue(mergedModules.contains("EE2211"), "Merged modules should contain EE2211.");
    }

    @Test
    public void parse_missingCompulsoryFlags_throwsException() {
        assertThrows(EquipmentMasterException.class, () ->
                AddCommand.parse("add n/Microscope"));
    }

    @Test
    public void parse_invalidNameCharacters_throwsException() {
        assertThrows(EquipmentMasterException.class, () ->
                AddCommand.parse("add n/Pipe|ette q/10"));
    }

    @Test
    public void parse_negativeQuantity_throwsException() {
        assertThrows(EquipmentMasterException.class, () ->
                AddCommand.parse("add n/Beaker q/-5"));
    }

    @Test
    public void parse_nonIntegerQuantity_throwsException() {
        assertThrows(EquipmentMasterException.class, () ->
                AddCommand.parse("add n/Beaker q/abc"));
    }

    @Test
    public void parse_validFullInput_success() throws EquipmentMasterException {
        AddCommand command = AddCommand.parse(
                "add n/Centrifuge q/2 bought/AY2024/25 Sem1 life/5.5 min/1 m/CS2113 m/CG2023");
        assertNotNull(command);
    }

    @Test
    public void execute_addExistingItem_cumulativeQuantityUpdated() throws EquipmentMasterException {
        // Bug Fix #1: Ensure total quantity is properly merged, not overwritten

        // 1. Add first batch
        AddCommand firstAdd = new AddCommand("Beer", 5);
        firstAdd.execute(context);

        // 2. Add second batch
        AddCommand secondAdd = new AddCommand("Beer", 3);
        secondAdd.execute(context);

        // 3. Verify total is 8, and there is only 1 entry in the list
        assertEquals(1, equipmentList.getSize());
        Equipment savedEquipment = equipmentList.getEquipment(0);
        assertEquals(8, savedEquipment.getAvailable());
        assertEquals(8, savedEquipment.getQuantity());
    }

    @Test
    public void parse_negativeMinThreshold_throwsException() {
        // Bug Fix #2: Reject negative min values in parser

        String input = "add n/Beer q/5 min/-2";

        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class, () -> {
            AddCommand.parse(input);
        });

        assertTrue(exception.getMessage().contains("cannot be negative") ||
                exception.getMessage().contains("valid whole number"));
    }

    @Test
    public void execute_withNewModule_ghostModuleCreatedAndTagged() throws EquipmentMasterException {
        // Bug Fix #3: Auto-create missing modules and tag equipment to them

        ArrayList<String> modules = new ArrayList<>();
        modules.add("CS2113");

        // Note: Using the constructor that accepts the module list
        AddCommand command = new AddCommand("Laptop", 10, modules);
        command.execute(context);

        // 1. Verify the module was auto-created in the global list
        assertTrue(moduleList.hasModule("CS2113"));

        // 2. Verify the equipment was successfully tagged to the module with default ratio 1.0
        Module createdModule = moduleList.getModule("CS2113");
        assertTrue(createdModule.getEquipmentRequirements().containsKey("Laptop"));
        assertEquals(1.0, createdModule.getEquipmentRequirements().get("Laptop"));
    }

    @Test
    public void parse_flagLikeSubstrings_parsedCorrectly() throws EquipmentMasterException {
        // Bug Fix #4: Valid equipment names rejected when containing flag-like substrings

        String input = "add n/alpha q/beta q/5";

        // If your regex/parser fix worked, this should NOT throw an exception
        AddCommand command = AddCommand.parse(input);

        // To strictly verify this, you might need to add package-private getters in AddCommand
        // Or simply rely on the fact that no Exception was thrown to prove it bypassed the bug.
        assertNotNull(command);
    }
}

