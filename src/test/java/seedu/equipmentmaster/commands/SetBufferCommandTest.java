package seedu.equipmentmaster.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SetBufferCommandTest {
    private static final String TEST_FILE_PATH = "test_equipment.txt";
    private static final String TEST_SETTING_FILE_PATH = "test_setting.txt";
    private static final String TEST_MODULE_FILE_PATH = "test_module.txt";

    @Test
    public void execute_validBuffer_setsBuffer() throws EquipmentMasterException {
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        ModuleList moduleList = new ModuleList();

        AddCommand addCommand = new AddCommand("STM32", 10);
        addCommand.execute(equipments, moduleList, ui, storage);

        SetBufferCommand command = new SetBufferCommand("STM32", 15.0);
        command.execute(equipments, moduleList, ui, storage);

        Equipment equipment = equipments.getEquipment(0);
        assertEquals(15.0, equipment.getBufferPercentage(), 0.0001);
    }

    @Test
    public void execute_equipmentNotFound_showsErrorMessage() {
        EquipmentList equipments = new EquipmentList();
        ModuleList moduleList = new ModuleList();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Ui ui = new Ui(System.in, new PrintStream(outputStream));
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);

        SetBufferCommand command = new SetBufferCommand("NonExistent", 10.0);
        command.execute(equipments, moduleList, ui, storage);

        String output = outputStream.toString();
        assertTrue(output.contains("Equipment 'NonExistent' not found."));
    }

    @Test
    public void parse_inputWithPercentSymbol_createsCommand() throws EquipmentMasterException {
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = new Storage(TEST_FILE_PATH, ui, TEST_SETTING_FILE_PATH, TEST_MODULE_FILE_PATH);
        ModuleList moduleList = new ModuleList();

        AddCommand addCommand = new AddCommand("STM32", 10);
        addCommand.execute(equipments, moduleList, ui, storage);

        SetBufferCommand command = SetBufferCommand.parse("setbuffer n/STM32 b/10%");
        command.execute(equipments, moduleList, ui, storage);

        Equipment equipment = equipments.getEquipment(0);
        assertEquals(10.0, equipment.getBufferPercentage(), 0.0001);
    }

    @Test
    public void parse_negativePercentage_throwsException() {
        try {
            SetBufferCommand.parse("setbuffer n/STM32 b/-10");
        } catch (EquipmentMasterException e) {
            assertTrue(e.getMessage().contains("Buffer percentage cannot be negative"));
        }
    }
}
