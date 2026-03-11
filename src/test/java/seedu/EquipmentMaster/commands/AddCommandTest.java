package seedu.EquipmentMaster.commands;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import seedu.EquipmentMaster.equipment.Equipment;
import seedu.EquipmentMaster.equipmentlist.EquipmentList;
import seedu.EquipmentMaster.storage.Storage;
import seedu.EquipmentMaster.ui.Ui;

public class AddCommandTest {

    @Test
    public void execute_validEquipment_addsToList() {
        EquipmentList equipments = new EquipmentList();
        Ui ui = new Ui();
        Storage storage = null;

        AddCommand command = new AddCommand("STM32", 5);

        command.execute(equipments, ui, storage);

        assertEquals(1, equipments.getSize());

        Equipment added = equipments.getEquipment(0);
        assertEquals("STM32", added.getName());
        assertEquals(5, added.getQuantity());
    }
}