package seedu.EquipmentMaster.commands;

import seedu.EquipmentMaster.equipmentlist.EquipmentList;
import seedu.EquipmentMaster.storage.Storage;
import seedu.EquipmentMaster.ui.Ui;

public class ByeCommand extends Command {

    @Override
    public void execute(EquipmentList equipments, Ui ui, Storage storage) {
        ui.showGoodByeMessage();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
