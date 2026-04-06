//@@author Hongyu1231
package seedu.equipmentmaster.commands;

import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.exception.EquipmentMasterException;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.ui.Ui;
import seedu.equipmentmaster.storage.Storage;

/**
 * Represents a command to update the enrollment number (pax) of an existing course module.
 */
public class UpdateModCommand extends Command {
    private static final Logger logger = Logger.getLogger(UpdateModCommand.class.getName());

    private final String moduleName;
    private final int newPax;

    /**
     * Constructs an {@code UpdateModCommand} with the specified module name and new pax.
     *
     * @param moduleName The name of the module to be updated.
     * @param newPax     The new student enrollment number.
     */
    public UpdateModCommand(String moduleName, int newPax) {
        assert moduleName != null && !moduleName.trim().isEmpty() : "Module name cannot be null or empty";
        assert newPax >= 0 : "Pax cannot be negative";

        this.moduleName = moduleName;
        this.newPax = newPax;
    }

    /**
     * Executes the update module command.
     * Updates the enrollment pax of an existing course module and saves the changes to storage.
     *
     * @param context The application context containing the module list, UI, and storage.
     * @throws EquipmentMasterException If the module is not found or saving fails.
     */
    @Override
    public void execute(Context context) throws EquipmentMasterException {
        assert context != null : "Context should not be null during execution";
        logExecution("UpdateModCommand");

        Ui ui = context.getUi();
        ModuleList moduleList = context.getModuleList();

        // 1. Update the module. If it doesn't exist, this will throw an EquipmentMasterException.
        moduleList.updateModule(moduleName, newPax);

        // 2. Print success message to the console.
        ui.showMessage("Successfully updated module:");
        ui.showMessage(moduleName + " | Enrollment: " + newPax + " students");

        // 3. SLAP: Extract storage saving logic to keep execute() clean
        saveToStorage(context.getStorage(), moduleList, ui);
    }

    /**
     * Helper method to synchronize module updates to the storage file.
     */
    private void saveToStorage(Storage storage, ModuleList moduleList, Ui ui) {
        try {
            if (storage != null) {
                storage.saveModules(moduleList);
                logger.log(Level.INFO, "Storage updated after module pax modification.");
            }
        } catch (EquipmentMasterException e) {
            logger.log(Level.SEVERE, "Failed to save module list after update", e);
            ui.showMessage("Warning: Module updated in memory but failed to save to disk.");
        }
    }

    /**
     * Parses the full command string provided by the user to create an {@code UpdateModCommand}.
     * Extracts the module name and the updated pax (enrollment number) using regular expressions.
     *
     * @param fullCommand The complete user input string (e.g., "updatemod n/CG2111A pax/180").
     * @return An {@code UpdateModCommand} initialized with the parsed module name and new pax.
     * @throws EquipmentMasterException If the command format is invalid or the pax is not an integer.
     */
    public static UpdateModCommand parse(String fullCommand) throws EquipmentMasterException {
        logger.log(Level.INFO, "Parsing UpdateModCommand input.");

        // Strip the starting command word to isolate the arguments
        String args = fullCommand.replaceFirst("(?i)^updatemod\\s*", "").trim();

        Pattern pattern = Pattern.compile("n/(.+?)\\s+pax/(.+)");
        Matcher matcher = pattern.matcher(args);

        if (!matcher.matches()) {
            logger.log(Level.WARNING, "Parse failed: Invalid command format.");
            throw new EquipmentMasterException("Invalid command format.\nExpected: updatemod n/NAME pax/QTY");
        }

        String moduleName = matcher.group(1).trim();
        String paxString = matcher.group(2).trim();

        try {
            int pax = Integer.parseInt(paxString);
            if (pax < 0) {
                logger.log(Level.WARNING, "Parse failed: Negative pax value provided.");
                throw new EquipmentMasterException("Pax cannot be a negative number.");
            }
            return new UpdateModCommand(moduleName, pax);
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Parse failed: Non-integer pax value provided.");
            throw new EquipmentMasterException("Invalid pax value. Please enter a valid integer.");
        }
    }
}
