package seedu.EquipmentMaster.commands;

import seedu.EquipmentMaster.equipmentlist.EquipmentList;
import seedu.EquipmentMaster.storage.Storage;
import seedu.EquipmentMaster.ui.Ui;
import seedu.EquipmentMaster.ui.UiTable;
import seedu.EquipmentMaster.ui.UiTableRow;

import java.util.stream.IntStream;

public class ListCommand extends Command {

    public ListCommand() {
    }

    @Override
    public void execute(EquipmentList equipments, Ui ui, Storage storage) {
        UiTable table = new UiTable();

        IntStream.range(0, equipments.getSize())
                .mapToObj(i -> new UiTableRow(equipments.getEquipment(i)))
                .forEach(table::addRow);

        ui.showMessage("Here is the equipment log:");
        ui.showMessage(table.toString().trim());
    }
}
