//@@author Hongyu1231
package seedu.equipmentmaster.commands;

import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.module.Module;
import seedu.equipmentmaster.ui.Ui;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a command to list all current course modules tracked in the system.
 */
public class ListModCommand extends Command {
    private static final Logger logger = Logger.getLogger(ListModCommand.class.getName());

    /**
     * Constructs a {@code ListModCommand}.
     * This command requires no additional arguments to execute.
     */
    public ListModCommand() {
        // No initialization required
    }

    /**
     * Executes the list module command.
     * Displays all currently tracked course modules and their respective enrollment numbers.
     *
     * @param context The application context containing the module list and UI.
     * @throws EquipmentMasterException If an error occurs while formatting the output.
     */
    @Override
    public void execute(Context context) throws EquipmentMasterException {
        assert context != null : "Context should not be null during execution";
        logExecution("ListModCommand");

        ModuleList moduleList = context.getModuleList();
        Ui ui = context.getUi();

        // 1. Check if the module list is empty
        if (moduleList.getModules().isEmpty()) {
            logger.log(Level.INFO, "ListModCommand executed but module list is empty.");
            ui.showMessage("There are currently no modules tracked in the system.");
            return;
        }
        logger.log(Level.INFO, "Listing " + moduleList.getModules().size() + " module(s).");

        displayModules(ui, moduleList);
    }

    /**
     * Helper method to handle the UI presentation of the module list.
     * @param ui The user interface handler.
     * @param moduleList The list of modules to display.
     */
    private void displayModules(Ui ui, ModuleList moduleList) {
        ui.showMessage("Here are the current course modules in your system:");

        int index = 1;
        for (Module m : moduleList.getModules()) {
            ui.showMessage(index + ". " + m.toString());
            index++;
        }
    }
}
