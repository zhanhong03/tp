package seedu.equipmentmaster.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.equipmentmaster.common.Messages.MESSAGE_INVALID_SETBUFFER_FORMAT;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.semester.AcademicSemester;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;

public class SetBufferCommandTest {
    private static final String TEST_FILE_PATH = "test_equipment.txt";
    private static final String TEST_SETTING_FILE_PATH = "test_setting.txt";
    private static final String TEST_MODULE_FILE_PATH = "test_module.txt";

    private EquipmentList equipments;
    private Ui ui;
    private Storage storage;
    private ModuleList moduleList;
    private Context context;

    @BeforeEach
    public void setUp() throws EquipmentMasterException {
        equipments = new EquipmentList();
        ui = new Ui();
        storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        moduleList = new ModuleList();
        AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
        context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);
    }

    private void addEquipment(String name, int quantity) throws EquipmentMasterException {
        AddCommand addCommand = new AddCommand(name, quantity);
        addCommand.execute(context);
    }

    @Test
    public void execute_validBuffer_setsBuffer() throws EquipmentMasterException {
        addEquipment("STM32", 10);

        SetBufferCommand command = new SetBufferCommand("STM32", 15.0);
        command.execute(context);

        Equipment equipment = equipments.getEquipment(0);
        assertEquals(15.0, equipment.getBufferPercentage(), 0.0001);
    }

    @Test
    public void execute_equipmentNotFound_showsErrorMessage() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Ui testUi = new Ui(System.in, new PrintStream(outputStream));
        Storage testStorage = new Storage(TEST_FILE_PATH, testUi, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);

        try {
            AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
            Context testContext = new Context(equipments, moduleList, testUi, testStorage, currentSystemSemester);

            SetBufferCommand command = new SetBufferCommand("NonExistent", 10.0);
            command.execute(testContext);

            String output = outputStream.toString();
            assertTrue(output.contains("Equipment 'NonExistent' not found."));
        } catch (EquipmentMasterException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void parse_inputWithPercentSymbol_createsCommand() throws EquipmentMasterException {
        addEquipment("STM32", 10);

        SetBufferCommand command = SetBufferCommand.parse("setbuffer n/STM32 b/10%");
        command.execute(context);

        Equipment equipment = equipments.getEquipment(0);
        assertEquals(10.0, equipment.getBufferPercentage(), 0.0001);
    }

    @Test
    public void parseEquipment_withBuffer_setsCorrectBuffer() throws EquipmentMasterException {
        addEquipment("STM32", 10);

        SetBufferCommand command = new SetBufferCommand("STM32", 15.5);
        command.execute(context);

        storage.save(equipments.getAllEquipments());
        ArrayList<Equipment> loadedList = storage.load();

        assertEquals(1, loadedList.size());
        Equipment loadedEquipment = loadedList.get(0);
        assertEquals(15.5, loadedEquipment.getBufferPercentage(), 0.0001);
    }

    @Test
    public void parseEquipment_withoutBuffer_setsDefaultBuffer() throws EquipmentMasterException {
        addEquipment("STM32", 10);

        Equipment equipment = equipments.getEquipment(0);
        assertEquals(0.0, equipment.getBufferPercentage(), 0.0001);
    }

    @Test
    public void execute_validIndex_setsBuffer() throws EquipmentMasterException {
        addEquipment("STM32", 10);
        addEquipment("Arduino", 5);
        addEquipment("RaspberryPi", 3);

        SetBufferCommand command = new SetBufferCommand(2, 20.0);
        command.execute(context);

        Equipment equipment = equipments.getEquipment(1);
        assertEquals(20.0, equipment.getBufferPercentage(), 0.0001);
        assertEquals("Arduino", equipment.getName());
    }

    @Test
    public void execute_invalidIndex_showsErrorMessage() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Ui testUi = new Ui(System.in, new PrintStream(outputStream));
        Storage testStorage = new Storage(TEST_FILE_PATH, testUi, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);

        try {
            AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
            Context testContext = new Context(equipments, moduleList, testUi, testStorage, currentSystemSemester);

            addEquipment("STM32", 10);

            SetBufferCommand command = new SetBufferCommand(99, 10.0);
            command.execute(testContext);

            String output = outputStream.toString();
            assertTrue(output.contains("Equipment at index 99 not found. (Total: 1 equipment(s))"));
        } catch (EquipmentMasterException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void parse_inputWithIndex_createsCommand() throws EquipmentMasterException {
        addEquipment("STM32", 10);

        SetBufferCommand command = SetBufferCommand.parse("setbuffer i/1 b/25");
        command.execute(context);

        Equipment equipment = equipments.getEquipment(0);
        assertEquals(25.0, equipment.getBufferPercentage(), 0.0001);
    }

    @Test
    public void parse_bothNameAndIndexFlags_throwsException() {
        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class,
                () -> SetBufferCommand.parse("setbuffer n/STM32 i/1 b/10"));
        assertTrue(exception.getMessage().contains("Please specify either name (n/) OR index (i/), not both"));
    }

    @Test
    public void parse_negativeBufferPercentage_throwsException() {
        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class,
                () -> SetBufferCommand.parse("setbuffer n/STM32 b/-10"));
        assertEquals("Buffer percentage cannot be negative.", exception.getMessage());
    }

    @Test
    public void parse_missingBFlag_throwsException() {
        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class,
                () -> SetBufferCommand.parse("setbuffer n/STM32"));
        assertEquals(MESSAGE_INVALID_SETBUFFER_FORMAT, exception.getMessage());
    }

    @Test
    public void parse_missingNameAndIndexFlags_throwsException() {
        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class,
                () -> SetBufferCommand.parse("setbuffer b/10"));
        assertEquals(MESSAGE_INVALID_SETBUFFER_FORMAT, exception.getMessage());
    }

    @Test
    public void parse_emptyName_throwsException() {
        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class,
                () -> SetBufferCommand.parse("setbuffer n/ b/10"));
        assertEquals("Equipment name cannot be empty.", exception.getMessage());
    }

    @Test
    public void parse_emptyIndex_throwsException() {
        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class,
                () -> SetBufferCommand.parse("setbuffer i/ b/10"));
        assertEquals("Equipment index cannot be empty.", exception.getMessage());
    }

    @Test
    public void parse_zeroIndex_throwsException() {
        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class,
                () -> SetBufferCommand.parse("setbuffer i/0 b/10"));
        assertEquals("Index must be a positive number.", exception.getMessage());
    }

    @Test
    public void parse_nonNumericIndex_throwsException() {
        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class,
                () -> SetBufferCommand.parse("setbuffer i/abc b/10"));
        assertEquals("Please enter a valid positive integer for index.", exception.getMessage());
    }

    @Test
    public void parse_nonNumericBuffer_throwsException() {
        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class,
                () -> SetBufferCommand.parse("setbuffer n/STM32 b/abc"));
        assertEquals("Please enter a valid number for buffer percentage.", exception.getMessage());
    }

    @Test
    public void parse_emptyBufferValue_throwsException() {
        EquipmentMasterException exception = assertThrows(EquipmentMasterException.class,
                () -> SetBufferCommand.parse("setbuffer n/STM32 b/"));
        assertEquals(MESSAGE_INVALID_SETBUFFER_FORMAT, exception.getMessage());
    }

    @Test
    public void parse_bufferValueAtEnd_extractsCorrectly() throws EquipmentMasterException {
        addEquipment("TestDevice", 10);

        SetBufferCommand command = SetBufferCommand.parse("setbuffer n/TestDevice b/30");
        command.execute(context);

        Equipment equipment = equipments.getEquipment(0);
        assertEquals(30.0, equipment.getBufferPercentage(), 0.0001);
    }

    @Test
    public void constructor_nonPositiveIndex_throwsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new SetBufferCommand(0, 10.0));
        assertTrue(exception.getMessage().contains("Index must be positive"));
    }

    @Test
    public void constructor_negativePercentage_throwsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new SetBufferCommand(1, -5.0));
        assertEquals("Buffer percentage cannot be negative.", exception.getMessage());
    }
}
