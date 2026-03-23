package seedu.equipmentmaster.commands;

import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.ui.Ui;
import seedu.equipmentmaster.ui.UiTable;
import seedu.equipmentmaster.ui.UiTableRow;

import java.util.stream.IntStream;

public class ListCommand extends Command {

    public ListCommand() {
    }

    /**
     * Executes the list equipment command.
     * Displays a formatted table of all equipment currently tracked in the system.
     *
     * @param context The application context containing the equipment list and UI.
     */
    @Override
    public void execute(Context context) {
        EquipmentList equipments = context.getEquipments();
        Ui ui = context.getUi();
        UiTable table = new UiTable();

        IntStream.range(0, equipments.getSize())
                .mapToObj(i -> new UiTableRow(equipments.getEquipment(i)))
                .forEach(table::addRow);

        ui.showMessage("Here is the equipment log:");
        ui.showMessage(table.toString().trim());
    }
}
