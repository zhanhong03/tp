package seedu.equipmentmaster.commands;

import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.ui.Ui;
import seedu.equipmentmaster.storage.Storage;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a command to delete an existing course module from the system.
 * This command also ensures data integrity by safely dereferencing the deleted
 * module from all associated equipment.
 */
public class DelModCommand extends Command {
    private static final Logger logger = Logger.getLogger(DelModCommand.class.getName());
    private static final String FLAG_NAME = "n/";

    // Using regex for parsing to maintain consistency with other command classes
    private static final Pattern COMMAND_FORMAT = Pattern.compile("n/(.+)");

    private final String moduleName;

    /**
     * Constructs a {@code DelModCommand} with the specified module name.
     *
     * @param moduleName The name of the module to be deleted.
     */
    public DelModCommand(String moduleName) {
        String trimmedModuleName = moduleName == null ? null : moduleName.trim();
        assert trimmedModuleName != null && !trimmedModuleName.isEmpty()
                : "Module name cannot be null or empty";
        this.moduleName = trimmedModuleName;
    }

    /**
     * Executes the delete module command.
     *
     * @param context The application context containing lists and storage.
     * @throws EquipmentMasterException If the module is not found in the registry.
     */
    @Override
    public void execute(Context context) throws EquipmentMasterException {
        assert context != null : "Context should not be null during execution";
        logExecution("DelModCommand");

        ModuleList moduleList = context.getModuleList();
        EquipmentList equipmentList = context.getEquipments();
        Ui ui = context.getUi();

        logger.log(Level.INFO, "Attempting to delete module: " + moduleName);

        // 1. Verify module existence early to prevent unnecessary operations
        if (!moduleList.hasModule(moduleName)) {
            throw new EquipmentMasterException("Module '" + moduleName + "' not found in registry.");
        }

        // 2. Safe Dereferencing: Remove the module tag from all associated equipment
        boolean isEquipmentModified = false;
        for (int i = 0; i < equipmentList.getSize(); i++) {
            Equipment eq = equipmentList.getEquipment(i);

            // Checks and removes the module code from the equipment's tag list
            if (eq.hasModuleCode(moduleName)) {
                eq.removeModuleCode(moduleName);
                isEquipmentModified = true;
            }
        }

        // 3. Execute module deletion from the registry
        moduleList.deleteModule(moduleName);

        // 4. Provide UI feedback to the user
        ui.showMessage("Successfully deleted module: " + moduleName);
        if (isEquipmentModified) {
            ui.showMessage("All associated equipment have been safely untagged.");
        }

        // 5. Pass to Storage for dual-saving
        saveToStorage(context.getStorage(), moduleList, equipmentList, isEquipmentModified, ui);
    }

    /**
     * Updates the storage files with the latest module list and equipment list.
     */
    private void saveToStorage(Storage storage, ModuleList moduleList, EquipmentList equipmentList,
                               boolean isEquipmentModified, Ui ui) {
        try {
            if (storage != null) {
                storage.saveModules(moduleList);

                // If equipment tags were modified, the equipment storage MUST be updated
                if (isEquipmentModified) {
                    storage.saveEquipments(equipmentList);
                }

                logger.log(Level.INFO, "Storage updated successfully.");
            }
        } catch (EquipmentMasterException e) {
            logger.log(Level.SEVERE, "Failed to save to disk", e);
            ui.showMessage("Warning: Data deleted in memory but failed to save to disk.");
        }
    }

    /**
     * Parses the user input to create a {@code DelModCommand}.
     *
     * @param fullCommand The raw user input.
     * @return A DelModCommand instance.
     * @throws EquipmentMasterException If the input format is invalid.
     */
    public static DelModCommand parse(String fullCommand) throws EquipmentMasterException {
        logger.log(Level.INFO, "Parsing DelModCommand.");

        String args = fullCommand.replaceFirst("(?i)^delmod\\s*", "").trim();
        Matcher matcher = COMMAND_FORMAT.matcher(args);

        if (!matcher.matches()) {
            logger.log(Level.WARNING, "Parse failed: invalid format for delmod.");
            throw new EquipmentMasterException("Invalid command format.\nExpected: delmod n/NAME");
        }

        // Force uppercase to prevent case-sensitivity bugs when targeting modules for deletion
        String moduleName = matcher.group(1).trim().toUpperCase();

        return new DelModCommand(moduleName);
    }
}
