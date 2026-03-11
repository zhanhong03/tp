package seedu.EquipmentMaster.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.EquipmentMaster.equipment.Equipment;
import seedu.EquipmentMaster.equipmentlist.EquipmentList;
import java.util.ArrayList;

public class FindCommandTest {
    @Test
    public void getMatchingEquipments_keywordMatches_returnsMatchedList() {
        // Arrange
        EquipmentList equipments = new EquipmentList();
        equipments.addEquipment(new Equipment("STM32 Development Board", 50));
        equipments.addEquipment(new Equipment("HDMI Cable", 100));

        FindCommand command = new FindCommand("stm32");

        // Act: Directly test the finding logic
        ArrayList<Equipment> matches = command.getMatchingEquipments(equipments);

        // Assert: Check the size and contents of the returned list
        assertEquals(1, matches.size());
        assertEquals("STM32 Development Board", matches.get(0).getName());
        assertEquals(50, matches.get(0).getQuantity());
    }

    @Test
    public void getMatchingEquipments_keywordNoMatch_returnsEmptyList() {
        // Arrange
        EquipmentList equipments = new EquipmentList();
        equipments.addEquipment(new Equipment("STM32 Development Board", 50));

        FindCommand command = new FindCommand("Basys3");

        // Act
        ArrayList<Equipment> matches = command.getMatchingEquipments(equipments);

        // Assert: The list should be completely empty
        assertTrue(matches.isEmpty());
        assertEquals(0, matches.size());
    }
}