package seedu.equipmentmaster.commands;

import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.ui.Ui;

/**
 * Represents a command that terminates the program.
 * When executed, it displays a goodbye message to the user
 * and signals that the application should exit.
 */
public class ByeCommand extends Command {

    /**
     * Executes the add module command using the provided application context.
     *
     * @param context The Context object containing global states like moduleList, Ui, and Storage.
     */
    @Override
    public void execute(Context context) {
        Ui ui = context.getUi();
        ui.showGoodByeMessage();
    }

    /**
     * Returns true to indicate that the program should terminate.
     *
     * @return {@code true} since this command exits the program.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
