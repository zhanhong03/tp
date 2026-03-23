package seedu.equipmentmaster.commands;

import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.parser.Parser;
import seedu.equipmentmaster.ui.Ui;
import seedu.equipmentmaster.ui.UiTable;
import seedu.equipmentmaster.ui.UiTableRow;

/**
 * Represents a command that displays a help message listing all available commands
 * and their usage formats.
 *
 * @author XiaoGeNekidora
 */
public class HelpCommand extends Command {

    /**
     * Constructs a new HelpCommand object.
     */
    public HelpCommand() {
    }

    /**
     * Executes the help command.
     * Displays a help message listing all available commands and their usage formats to the user.
     *
     * @param context The application context containing the UI.
     */
    @Override
    public void execute(Context context) {
        Ui ui = context.getUi();
        UiTable table = new UiTable(true);
        table.addRow(new UiTableRow("Command","Format"));
        for(Parser.CommandSpec spec: Parser.getCommandSpecs()){
            table.addRow(new UiTableRow(spec.getKeyword(),spec.getFormat()));
        }

        ui.showMessage("Here are the available commands:");
        ui.showMessage(table.toString().trim());
    }
}
