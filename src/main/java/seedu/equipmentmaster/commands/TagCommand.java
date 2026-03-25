package seedu.equipmentmaster.commands;

import static seedu.equipmentmaster.parser.Parser.CommandSpec.extractArgument;

import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.module.Module;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;

public class TagCommand extends Command {
    private final String equipmentName;
    private final String moduleName;
    private final double requirementRatio;

    public TagCommand(String moduleName, String equipmentName, double requirementRatio) {
        this.equipmentName = equipmentName;
        this.moduleName = moduleName;
        this.requirementRatio = requirementRatio;
    }

    @Override
    public void execute(Context context) throws EquipmentMasterException {
        ModuleList modules = context.getModuleList();
        EquipmentList equipments = context.getEquipments();
        Ui ui = context.getUi();
        Storage storage = context.getStorage();
        boolean moduleExists = modules.hasModule(moduleName);
        boolean equipmentExists = equipments.hasEquipment(equipmentName);

        if (!moduleExists && !equipmentExists) {
            throw new EquipmentMasterException("Aborted: Neither the module '" +
                    moduleName + "' nor the equipment '" + equipmentName + "' exists.");
        } else if (!moduleExists) {
            throw new EquipmentMasterException("Aborted: Module '" + moduleName + "' does not exist.");
        } else if (!equipmentExists) {
            throw new EquipmentMasterException("Aborted: Equipment '" + equipmentName + "' does not exist.");
        }

        Module targetModule = modules.getModule(moduleName);
        String officialEquipmentName = equipments.findByName(equipmentName).getName();

        // Use the officialEquipmentName and the module's canonical name
        targetModule.addEquipmentRequirement(officialEquipmentName, requirementRatio);
        String moduleDisplayName = targetModule.getName();
        String equipmentDisplayName = officialEquipmentName;
        StringBuilder successMessage = new StringBuilder();
        successMessage.append("Successfully linked equipment to module:\n")
                .append(moduleDisplayName)
                .append(" now requires ")
                .append(requirementRatio)
                .append(" x ")
                .append(equipmentDisplayName)
                .append(" per student");
        if (requirementRatio > 0.0 && requirementRatio <= 1.0) {
            int studentsPerItem = (int) Math.round(1.0 / requirementRatio);
            successMessage.append(" (1 item per ")
                    .append(studentsPerItem)
                    .append(" students)");
        }
        successMessage.append(".");
        ui.showMessage(successMessage.toString());

        try {
            storage.saveModules(modules);
        } catch (EquipmentMasterException e) {
            ui.showMessage("Warning: Failed to save the updated tags to the data file. " + e.getMessage());
        }
    }

    /**
     * Parses the raw command string and returns a ready-to-execute TagCommand.
     *
     * @param fullCommand The raw input from the user (e.g., "tag m/CG2111A n/STM32 req/0.2").
     * @return A constructed TagCommand object.
     * @throws EquipmentMasterException If the format, prefixes, or decimals are invalid.
     */
    public static TagCommand parse(String fullCommand) throws EquipmentMasterException {
        String[] prefixes = {" m/", " n/", " req/"};

        // Extract using the shared method
        String moduleName = extractArgument(fullCommand, "m/", prefixes);
        String equipmentName = extractArgument(fullCommand, "n/", prefixes);
        String reqString = extractArgument(fullCommand, "req/", prefixes);

        if (moduleName.isEmpty() || equipmentName.isEmpty() || reqString.isEmpty()) {
            throw new EquipmentMasterException(
                    "Invalid command format. \nExpected: tag m/MOD_NAME n/EQ_NAME req/FRACTION"
            );
        }

        // 3. Handle the Invalid Decimals edge case
        double requirementRatio;
        try {
            requirementRatio = Double.parseDouble(reqString);
        } catch (NumberFormatException e) {
            throw new EquipmentMasterException(
                    "Invalid requirement ratio format. Please provide a valid decimal number (e.g., 0.2)."
            );
        }
        if (!Double.isFinite(requirementRatio) || requirementRatio <= 0.0) {
            throw new EquipmentMasterException(
                    "Invalid requirement ratio. Please provide a positive, finite decimal number (e.g., 0.2)."
            );
        }
        // 4. Return the constructed command
        return new TagCommand(moduleName, equipmentName, requirementRatio);
    }

}
