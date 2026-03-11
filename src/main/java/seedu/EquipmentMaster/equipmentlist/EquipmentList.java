package seedu.EquipmentMaster.equipmentlist;

import java.util.ArrayList;

import seedu.EquipmentMaster.equipment.Equipment;

public class EquipmentList {
    private ArrayList<Equipment> equipments;

    public EquipmentList(ArrayList<Equipment> equipments) {
        this.equipments = equipments;
    }

    public EquipmentList() {
        equipments = new ArrayList<>();
    }

    public void addEquipment(Equipment newEquipment) {
        equipments.add(newEquipment);
    }

    public Equipment getEquipment(int index) {
        return equipments.get(index);
    }

    public int getSize() {
        return equipments.size();
    }

    public boolean isEmpty() {
        return equipments.isEmpty();
    }

    public ArrayList<Equipment> getAllEquipments() {
        return equipments;
    }
}
