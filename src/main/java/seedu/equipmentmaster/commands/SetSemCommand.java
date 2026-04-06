package seedu.equipmentmaster.commands;

import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.module.Module;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;
import seedu.equipmentmaster.semester.AcademicSemester;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command to update the global academic semester of the application.
 */
public class SetSemCommand extends Command {
    private static final Logger logger = Logger.getLogger(SetSemCommand.class.getName());
    private static final String MESSAGE_USAGE = "Please specify a semester. " +
            "Usage: setsem AY[YYYY]/[YY] Sem[1/2]";
    private final String rawSem;

    /**
     * Initializes the command with the raw semester string provided by the user.
     * @param rawSem The user input string for the new semester.
     */
    public SetSemCommand(String rawSem) {
        assert rawSem != null && !rawSem.trim().isEmpty() :
                "Semester string cannot be null or empty";
        this.rawSem = rawSem;
    }

    /**
     * Parses the arguments for the 'setsem' command and creates a SetSemCommand object.
     *
     * @param fullCommand The complete input string containing the 'setsem' command and the semester.
     * @return A SetSemCommand object containing the target academic semester.
     * @throws EquipmentMasterException If the semester argument is missing.
     */
    public static Command parse(String fullCommand) throws EquipmentMasterException {
        logger.log(Level.INFO, "Parsing SetSemCommand input.");
        String[] words = fullCommand.trim().split("\\s+", 2);

        // Check if the user provided the semester string after "setsem"
        if (words.length < 2 || words[1].trim().isEmpty()) {
            logger.log(Level.WARNING, "Parse failed: Missing semester argument.");
            throw new EquipmentMasterException(MESSAGE_USAGE);
        }

        String rawSemester = words[1].trim();
        return new SetSemCommand(rawSemester);
    }

    /**
     * Executes the set semester command.
     * Updates the global system academic semester in the context and saves the new setting to the settings file.
     *
     * @param context The application context containing the global semester state, UI, and storage.
     */
    @Override
    public void execute(Context context) throws EquipmentMasterException {
        assert context != null : "Context should not be null during execution";
        logExecution("SetSemCommand");

        Ui ui = context.getUi();
        AcademicSemester oldSem = context.getCurrentSemester();

        logger.log(Level.INFO, "Attempting to set new semester from raw input: " + rawSem);

        // This will throw EquipmentMasterException if format is wrong, which we proudly pass up!
        AcademicSemester newSem = new AcademicSemester(rawSem);

        context.setCurrentSemester(newSem);
        ui.showMessage("System time updated. Current academic semester is now set to " + newSem);

        saveToStorage(context.getStorage(), newSem, ui);
        warnIfSemesterChanged(oldSem, newSem, context.getModuleList(), ui);
    }

    /**
     * Helper method to save the updated semester to the settings file.
     */
    private void saveToStorage(Storage storage, AcademicSemester newSem, Ui ui) {
        try {
            if (storage != null) {
                storage.saveSettings(newSem);
                logger.log(Level.INFO, "New semester successfully saved to storage.");
            }
        } catch (EquipmentMasterException e) {
            logger.log(Level.SEVERE, "Failed to save semester settings", e);
            ui.showMessage("Warning: Semester updated in memory but failed to save to disk.");
        }
    }

    /**
     * Helper method to issue a warning for module enrollment updates if the semester changed.
     */
    private void warnIfSemesterChanged(AcademicSemester oldSem, AcademicSemester newSem,
                                       ModuleList moduleList, Ui ui) {
        // Guard clause: If no modules exist, no warning is needed
        if (moduleList == null || moduleList.getModules().isEmpty()) {
            return;
        }

        // Issue warning if this is the first initialization OR the semester has changed
        if (oldSem == null || !newSem.equals(oldSem)) {
            logger.log(Level.INFO, "Semester shift detected. Issuing enrollment update warning.");
            ui.showMessage("[!] WARNING: Semester changed. Please remember to update the enrollment "
                    + "numbers for the following modules using the 'updatemod' command:");

            for (Module m : moduleList.getModules()) {
                ui.showMessage("    - " + m.getName() + " (Current: " + m.getPax() + ")");
            }
        }
    }
}
