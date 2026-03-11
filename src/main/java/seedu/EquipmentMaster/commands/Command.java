package seedu.EquipmentMaster.commands;


import seedu.EquipmentMaster.equipmentlist.EquipmentList;
import seedu.EquipmentMaster.exception.EquipmentMasterException;
import seedu.EquipmentMaster.storage.Storage;
import seedu.EquipmentMaster.ui.Ui;

public abstract class Command {
    public abstract void execute(EquipmentList equipments, Ui ui, Storage storage)
            throws EquipmentMasterException;

    public boolean isExit() {
        return false;
    }
}
