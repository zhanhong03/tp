package seedu.equipmentmaster.commands;

import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;

public class SetMinCommand extends Command {
    private final String name;
    private final int minQty;

    public SetMinCommand(String name, int minQty) {
        this.name = name;
        this.minQty = minQty;
    }

    /**
     * Executes the set minimum threshold command.
     * Updates the minimum stock threshold for a specific equipment to trigger future low-stock alerts.
     *
     * @param context The application context containing the equipment list, UI, and storage.
     * @throws EquipmentMasterException If the equipment is not found or saving fails.
     */
    @Override
    public void execute(Context context) throws EquipmentMasterException {
        EquipmentList equipments = context.getEquipments();
        Ui ui = context.getUi();
        Storage storage = context.getStorage();

        Equipment target = equipments.findByName(name);
        if (target == null) {
            throw new EquipmentMasterException("Equipment not found: " + name);
        }

        target.setMinQuantity(minQty);
        ui.showMessage("Success: Minimum threshold for " + name + " set to " + minQty);

        if (target.getQuantity() < target.getMinQuantity()) {
            ui.showMessage("Warning: Item is currently below this new threshold!");
        }
        storage.save(equipments.getAllEquipments());
    }

    public static Command parse(String command) throws EquipmentMasterException {
        try {
            String name = command.split(" n/")[1].split(" min/")[0].trim();
            int min = Integer.parseInt(command.split("min/")[1].trim());
            if (min < 0) {
                throw new EquipmentMasterException("Minimum threshold cannot be negative.");
            }
            return new SetMinCommand(name, min);
        } catch (EquipmentMasterException e) {
            throw e;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new EquipmentMasterException("Invalid format! Use: setmin n/NAME min/QUANTITY");
        }
    }
}
