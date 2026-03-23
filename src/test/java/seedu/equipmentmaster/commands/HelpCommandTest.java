package seedu.equipmentmaster.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.parser.Parser;
import seedu.equipmentmaster.semester.AcademicSemester;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class HelpCommandTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private Ui ui;
    private Storage storage;
    private EquipmentList equipmentList;

    @BeforeEach
    public void setUp() {
        ui = new Ui(System.in, new PrintStream(outputStreamCaptor));
        storage = new Storage("dummy/path.txt", ui, "dummy/settingPath.txt", "dummy/modulePath.txt");
        equipmentList = new EquipmentList();
    }

    @Test
    public void execute_helpCommand_displaysAllCommands() {
        HelpCommand helpCommand = new HelpCommand();
        ModuleList moduleList = new ModuleList();
        try {
            AcademicSemester currentSystemSemester = new AcademicSemester("AY2024/25 Sem1");
            Context context = new Context(equipmentList, moduleList, ui, storage, currentSystemSemester);
            helpCommand.execute(context);

            String output = outputStreamCaptor.toString();

            // Check standard header
            assertTrue(output.contains("Command"));
            assertTrue(output.contains("Format"));
            assertTrue(output.contains("Here are the available commands:"));

            // Check for presence of all registered commands
            for (Parser.CommandSpec spec : Parser.getCommandSpecs()) {
                assertTrue(output.contains(spec.getKeyword()), "Output should contain command keyword: "
                        + spec.getKeyword());
                assertTrue(output.contains(spec.getFormat()), "Output should contain command format: "
                        + spec.getFormat());
            }
        } catch (EquipmentMasterException e) {
            fail("Test setup failed unexpectedly: " + e.getMessage());
        }
    }
}

