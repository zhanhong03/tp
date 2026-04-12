package seedu.equipmentmaster.commands;

import static seedu.equipmentmaster.parser.Parser.CommandSpec.extractArgument;

import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.module.Module;
import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;


/**
 * Removes a specific equipment requirement from a module.
 */
public class UntagCommand extends Command {
    private final String moduleName;
    private final String equipmentName;

    public UntagCommand(String moduleName, String equipmentName) {
        this.moduleName = moduleName;
        this.equipmentName = equipmentName;
    }

    /**
     * Parses the raw command string and returns a ready-to-execute UntagCommand.
     *
     * @param fullCommand The raw input from the user (e.g., "untag m/CG2111A n/STM32").
     * @return A constructed UntagCommand object.
     * @throws EquipmentMasterException If the format or prefixes are invalid.
     */
    public static UntagCommand parse(String fullCommand) throws EquipmentMasterException {
        String[] prefixes = {" m/", " n/"};

        String parsedModuleName = extractArgument(fullCommand, "m/", prefixes);
        String parsedEquipmentName = extractArgument(fullCommand, "n/", prefixes);

        if (parsedModuleName.isEmpty() || parsedEquipmentName.isEmpty()) {
            throw new EquipmentMasterException(
                    "Invalid command format. \nExpected: untag m/MOD_NAME n/EQ_NAME"
            );
        }

        return new UntagCommand(parsedModuleName, parsedEquipmentName);
    }

    @Override
    public void execute(Context context) throws EquipmentMasterException {
        ModuleList modules = context.getModuleList();
        EquipmentList equipments = context.getEquipments();
        Ui ui = context.getUi();
        Storage storage = context.getStorage();

        // 1. Double Ghost Reference Check
        boolean moduleExists = modules.hasModule(moduleName);
        boolean equipmentExists = equipments.hasEquipment(equipmentName);

        if (!moduleExists && !equipmentExists) {
            throw new EquipmentMasterException("Aborted: Neither the module '" + moduleName +
                    "' nor the equipment '" + equipmentName + "' exists.");
        } else if (!moduleExists) {
            throw new EquipmentMasterException("Aborted: Module '" + moduleName + "' does not exist.");
        } else if (!equipmentExists) {
            throw new EquipmentMasterException("Aborted: Equipment '" + equipmentName + "' does not exist.");
        }

        // 2. Retrieve the objects
        Module targetModule = modules.getModule(moduleName);
        Equipment targetEquipment = equipments.findByName(equipmentName);

        // Grab the official names to ensure a perfect match in the internal lists
        String officialEquipmentName = targetEquipment.getName();
        String officialModuleName = targetModule.getName();

        // 3. Bidirectional Removal (Safe Dereferencing)
        // Remove from Module's requirement map
        boolean isRemovedFromModule = targetModule.removeEquipmentRequirement(officialEquipmentName);

        // FIX 1: Also remove the module code from the Equipment's internal tag list
        targetEquipment.removeModuleCode(officialModuleName);

        // 4. Output the result
        if (isRemovedFromModule) {
            ui.showMessage("Successfully untagged equipment from module:\n" +
                    officialModuleName + " no longer requires " + officialEquipmentName + ".");
        } else {
            // Throw an exception if they tried to untag something that wasn't tagged in the first place
            throw new EquipmentMasterException("Aborted: " + officialModuleName +
                    " does not currently have " + officialEquipmentName + " as a requirement.");
        }

        // 5. FIX 2: Dual-Saving (Ensure both files on the hard disk are updated)
        try {
            storage.saveModules(modules);
            storage.saveEquipments(equipments); // CRITICAL: Save equipment to remove the tag on disk
        } catch (EquipmentMasterException e) {
            ui.showMessage("Warning: Failed to save the updated tags to the data file. " + e.getMessage());
        }
    }
}
