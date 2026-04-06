package seedu.equipmentmaster.commands;

import org.junit.jupiter.api.Test;
import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.semester.AcademicSemester;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;
import seedu.equipmentmaster.exception.EquipmentMasterException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListCommandTest {

    @Test
    public void execute_emptyList_showsEmptyMessage() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Ui ui = new Ui(System.in, new PrintStream(outputStream));
        EquipmentList equipments = new EquipmentList();
        ModuleList moduleList = new ModuleList();
        Storage storage = new Storage("test.txt", ui, "set.txt", "mod.txt");
        Context context = new Context(equipments, moduleList, ui, storage, null);

        ListCommand listCommand = new ListCommand();
        listCommand.execute(context);

        String output = outputStream.toString();
        assertTrue(output.contains("Here is the equipment log:"));
        assertTrue(output.contains("<empty table>"));
    }

    @Test
    public void execute_nonEmptyList_showsTable() throws EquipmentMasterException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Ui ui = new Ui(System.in, new PrintStream(outputStream));
        EquipmentList equipments = new EquipmentList();
        
        Equipment eq = new Equipment("Arduino", 20);
        equipments.addEquipment(eq);
        
        ModuleList moduleList = new ModuleList();
        Storage storage = new Storage("test.txt", ui, "set.txt", "mod.txt");
        Context context = new Context(equipments, moduleList, ui, storage, null);

        ListCommand listCommand = new ListCommand();
        listCommand.execute(context);

        String output = outputStream.toString();
        assertTrue(output.contains("Here is the equipment log:"));
        assertTrue(output.contains("Arduino"));
        assertTrue(output.contains("Total: 20"));
    }
}

