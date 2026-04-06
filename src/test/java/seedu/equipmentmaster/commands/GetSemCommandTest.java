package seedu.equipmentmaster.commands;

import org.junit.jupiter.api.Test;
import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.semester.AcademicSemester;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;
import seedu.equipmentmaster.exception.EquipmentMasterException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetSemCommandTest {

    @Test
    public void execute_nullSemester_showsInitializationMessage() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Ui ui = new Ui(System.in, new PrintStream(outputStream));
        EquipmentList equipments = new EquipmentList();
        ModuleList moduleList = new ModuleList();
        Storage storage = new Storage("test.txt", ui, "set.txt", "mod.txt");
        Context context = new Context(equipments, moduleList, ui, storage, null);

        GetSemCommand command = new GetSemCommand();
        command.execute(context);

        String output = outputStream.toString();
        assertTrue(output.contains("The system time has not been initialized yet."));
    }

    @Test
    public void execute_validSemester_showsSemesterMessage() throws EquipmentMasterException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Ui ui = new Ui(System.in, new PrintStream(outputStream));
        EquipmentList equipments = new EquipmentList();
        ModuleList moduleList = new ModuleList();
        Storage storage = new Storage("test.txt", ui, "set.txt", "mod.txt");
        AcademicSemester sem = new AcademicSemester("AY2024/25 Sem1");
        Context context = new Context(equipments, moduleList, ui, storage, sem);

        GetSemCommand command = new GetSemCommand();
        command.execute(context);

        String output = outputStream.toString();
        assertTrue(output.contains("The current system time is: AY2024/25 Sem1"));
    }
}

