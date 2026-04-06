//@@author Hongyu1231
package seedu.equipmentmaster.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.module.Module;
import seedu.equipmentmaster.ui.Ui;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit tests for the {@code UpdateModCommand} class.
 */
public class UpdateModCommandTest {

    private ModuleList moduleList;

    @BeforeEach
    public void setUp() {
        Ui ui = new Ui();
        moduleList = new ModuleList();
        try {
            // Add a dummy module for execution tests
            moduleList.addModule(new Module("CG2271", 100));
        } catch (EquipmentMasterException e) {
            ui.showMessage(e.getMessage());
        }
    }

    @Test
    public void parse_validInput_success() throws EquipmentMasterException {
        String input = "updatemod n/CG2271 pax/200";
        UpdateModCommand command = UpdateModCommand.parse(input);
        assertEquals(UpdateModCommand.class, command.getClass());
    }

    @Test
    public void parse_missingNamePrefix_throwsException() {
        // Missing the "n/" prefix
        String input = "updatemod CG2271 pax/200";
        assertThrows(EquipmentMasterException.class, () -> {
            UpdateModCommand.parse(input);
        });
    }

    @Test
    public void parse_paxWithDecimals_throwsException() {
        // Pax cannot be a decimal number
        String input = "updatemod n/CG2271 pax/150.5";
        assertThrows(EquipmentMasterException.class, () -> {
            UpdateModCommand.parse(input);
        });
    }

    @Test
    public void parse_invalidFormat_throwsException() {
        // Triggers the branch where the regex pattern does not match
        String input = "updatemod n/CG2271";
        assertThrows(EquipmentMasterException.class, () -> UpdateModCommand.parse(input));
    }

    @Test
    public void parse_negativePax_throwsException() {
        // Triggers the check for negative enrollment numbers (pax < 0)
        String input = "updatemod n/CG2271 pax/-50";
        assertThrows(EquipmentMasterException.class, () -> UpdateModCommand.parse(input));
    }

    @Test
    public void execute_validUpdate_success(@TempDir Path tempDir) throws EquipmentMasterException {
        // 1. ARRANGE: Create isolated, temporary paths for this specific test run.
        // These files will be physically created inside a temporary system folder
        // and automatically deleted after the test finishes.
        String eqPath = tempDir.resolve("temp_e.txt").toString();
        String setPath = tempDir.resolve("temp_s.txt").toString();
        String modPath = tempDir.resolve("temp_m.txt").toString();

        seedu.equipmentmaster.ui.Ui ui = new seedu.equipmentmaster.ui.Ui();
        seedu.equipmentmaster.storage.Storage storage = new seedu.equipmentmaster.storage.Storage(
                eqPath, ui, setPath, modPath);

        // We assume moduleList is already initialized in your @BeforeEach setup
        seedu.equipmentmaster.context.Context context = new seedu.equipmentmaster.context.Context(
                null, moduleList, ui, storage, null);

        // 2. ACT: Execute the update command
        UpdateModCommand command = new UpdateModCommand("CG2271", 150);
        command.execute(context);

        // 3. ASSERT: Verify the state change and persistence
        assertEquals(150, moduleList.getModule("CG2271").getPax(),
                "The module enrollment (pax) should be updated to 150.");

        // Check if the storage layer actually triggered a write to the temp file
        java.io.File savedFile = new java.io.File(modPath);
        assertTrue(savedFile.exists(), "The module list should be successfully persisted to disk.");
    }

    @Test
    public void execute_moduleNotFound_throwsException() {
        // Verifies that moduleList.updateModule throws an exception when the module is missing
        UpdateModCommand command = new UpdateModCommand("UNKNOWN_MOD", 150);
        seedu.equipmentmaster.context.Context context = new seedu.equipmentmaster.context.Context(
                null, moduleList, new seedu.equipmentmaster.ui.Ui(), null, null);

        assertThrows(EquipmentMasterException.class, () -> command.execute(context));
    }

    @Test
    public void execute_storageSaveFailure_triggersCatch() throws EquipmentMasterException {
        // Triggers the inner catch block on line 58 by simulating a storage failure
        seedu.equipmentmaster.ui.Ui ui = new seedu.equipmentmaster.ui.Ui();
        seedu.equipmentmaster.storage.Storage faultyStorage = new seedu.equipmentmaster.storage.Storage(
                "e.txt", ui, "s.txt", "m.txt") {
            @Override
            public void saveModules(ModuleList list) throws EquipmentMasterException {
                throw new EquipmentMasterException("Simulated save failure");
            }
        };

        seedu.equipmentmaster.context.Context context = new seedu.equipmentmaster.context.Context(
                null, moduleList, ui, faultyStorage, null);

        UpdateModCommand command = new UpdateModCommand("CG2271", 120);
        // Should execute and catch the exception internally, showing a message via UI
        command.execute(context);
    }
}
