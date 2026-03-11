package seedu.EquipmentMaster.commands;


import seedu.EquipmentMaster.equipment.Equipment;
import seedu.EquipmentMaster.equipmentlist.EquipmentList;
import seedu.EquipmentMaster.storage.Storage;
import seedu.EquipmentMaster.ui.Ui;

public class AddCommand extends Command{
    private final String name;
    private final int quantity;

    public AddCommand(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public void execute(EquipmentList equipments, Ui ui, Storage storage) {
        Equipment equipment = new Equipment(name, quantity);
        equipments.addEquipment(equipment);
        storage.save(equipments.getAllEquipments());
        ui.showMessage("Added " + quantity + " of " + name + ". (Total Available: " + equipment.getAvailable() + ")" );
    }
}
