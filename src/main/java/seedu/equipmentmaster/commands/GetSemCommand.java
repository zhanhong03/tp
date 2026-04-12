//@@author Hongyu1231
package seedu.equipmentmaster.commands;

import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.ui.Ui;
import seedu.equipmentmaster.semester.AcademicSemester;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command to display the current global academic semester set in the system.
 */
public class GetSemCommand extends Command {
    private static final Logger logger = Logger.getLogger(GetSemCommand.class.getName());

    /**
     * Executes the get semester command.
     * Retrieves and displays the current global academic semester from the application context.
     *
     * @param context The application context containing the global semester state and UI.
     */
    @Override
    public void execute(Context context) {
        assert context != null : "Context should not be null during execution";
        logExecution("GetSemCommand");

        Ui ui = context.getUi();
        AcademicSemester current = context.getCurrentSemester();

        if (current == null) {
            logger.log(Level.WARNING, "GetSemCommand executed but semester is uninitialized.");
            ui.showMessage("The system time has not been initialized yet.");
        } else {
            logger.log(Level.INFO, "Successfully displayed current semester: " + current);

            if (current.toString().equals("AY2024/25 Sem1")) {
                ui.showMessage("The current system time is: " + current + " (System Default)");
                ui.showMessage("Tip: You can update this to your actual current semester using the 'setsem' command.");
            } else {
                ui.showMessage("The current system time is: " + current);
            }
        }
    }
}
