package seedu.equipmentmaster.commands;


import seedu.equipmentmaster.equipment.Equipment;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.semester.AcademicSemester;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;

import static seedu.equipmentmaster.common.Messages.MESSAGE_INVALID_ADD_FORMAT;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a command that adds new equipment to the equipment list.
 * The command creates a new {@code Equipment} object with the specified
 * name and quantity, adds it to the equipment list, saves the updated
 * list to storage, and displays a confirmation message to the user.
 */
public class AddCommand extends Command{
    private static final Logger logger = Logger.getLogger(AddCommand.class.getName());

    private final String name;
    private final int quantity;
    private final AcademicSemester purchaseSem;
    private final double lifespanYears;

    /**
     * Constructs an {@code AddCommand} with the specified equipment name and quantity.
     *
     * @param name Name of the equipment to add.
     * @param quantity Number of items to add.
     * @param purchaseSem Sem that the item was bought
     * @param lifespanYears Lifespan of the item in year
     */
    public AddCommand(String name, int quantity, AcademicSemester purchaseSem, double lifespanYears) {
        this.name = name;
        this.quantity = quantity;
        this.purchaseSem = purchaseSem;
        this.lifespanYears = lifespanYears;
    }

    /**
     * Parses the arguments for the 'add' command and creates an AddCommand object.
     *
     * @param fullCommand The complete input string containing the 'add' command and its arguments.
     * @return An AddCommand object containing the parsed equipment name and quantity.
     * @throws EquipmentMasterException If the format is incorrect, quantity is missing/invalid, or negative.
     */
    public static AddCommand parse(String fullCommand) throws EquipmentMasterException {
        logger.log(Level.INFO, "Starting to parse add command input.");

        if (!fullCommand.contains("n/") || (!fullCommand.contains("q/")) ||
                (!fullCommand.contains("bought/") || (!fullCommand.contains("life/")))) {
            logger.log(Level.WARNING,
                    "Missing compulsory flags (n/, q/, bought/, or life/) in user input.");
            throw new EquipmentMasterException(
                    "Invalid add command format.\n"
                            + "Usage: add n/NAME q/QUANTITY bought/SEMESTER life/LIFESPAN_YEARS");
        }

        String name = extractArgument(fullCommand, "n/");
        String qtString = extractArgument(fullCommand, "q/");
        String purchaseSemStr = extractArgument(fullCommand, "bought/");
        String lifespanYearsStr = extractArgument(fullCommand, "life/");

        if (name.isEmpty() || qtString.isEmpty() || purchaseSemStr.isEmpty() || lifespanYearsStr.isEmpty()) {
            logger.log(Level.WARNING, "One or more parsed fields are empty.");
            throw new EquipmentMasterException(
                    "Invalid add command format.\n"
                            + "Usage: add n/NAME q/QUANTITY bought/SEMESTER life/LIFESPAN_YEARS");
        }
        int quantity;
        try {
            quantity = Integer.parseInt(qtString);
            if (quantity <= 0) {
                logger.log(Level.WARNING, "Parsed quantity is zero or negative: " + quantity);
                throw new EquipmentMasterException("Equipment quantity must be positive.");
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Failed to parse quantity as an integer: " + qtString, e);
            throw new EquipmentMasterException("Please enter a valid whole number for quantity");
        }
        AcademicSemester purchaseSem = new AcademicSemester(purchaseSemStr.trim());
        double lifespanYear;
        try {
            lifespanYear = Double.parseDouble(lifespanYearsStr.trim());
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Failed to parse lifespan in years as a number: " + lifespanYearsStr, e);
            throw new EquipmentMasterException("Please enter a valid number for lifespan in years");
        }
        logger.log(Level.INFO, "Successfully parsed AddCommand for equipment: " + name);
        return new AddCommand(name, quantity, purchaseSem, lifespanYear);
    }

    /**
     * Extracts the argument value following the given prefix, up to the next known prefix or end of string.
     *
     * @param fullCommand The full user input string.
     * @param prefix The prefix whose value should be extracted (e.g., "n/", "q/").
     * @return The trimmed value associated with the prefix, or an empty string if none.
     * @throws EquipmentMasterException If the prefix cannot be found.
     */
    private static String extractArgument(String fullCommand, String prefix) throws EquipmentMasterException {
        int prefixIndex = fullCommand.indexOf(prefix);
        if (prefixIndex < 0) {
            throw new EquipmentMasterException(MESSAGE_INVALID_ADD_FORMAT);
        }
        int valueStart = prefixIndex + prefix.length();
        int valueEnd = fullCommand.length();
        String[] allPrefixes = { "n/", "q/", "bought/", "life/" };
        for (String otherPrefix : allPrefixes) {
            if (otherPrefix.equals(prefix)) {
                continue;
            }
            int idx = fullCommand.indexOf(otherPrefix, valueStart);
            if (idx != -1 && idx < valueEnd) {
                valueEnd = idx;
            }
        }
        if (valueStart >= valueEnd) {
            return "";
        }
        return fullCommand.substring(valueStart, valueEnd).trim();
    }

    /**
     * Executes the add command by creating the equipment,
     * adding it to the equipment list, saving the updated list,
     * and displaying a message to the user.
     *
     * @param equipments The equipment list to add the equipment to.
     * @param ui The user interface used to display messages.
     * @param storage The storage system used to persist data.
     */
    @Override
    public void execute(EquipmentList equipments, Ui ui, Storage storage) {
        Equipment equipment = new Equipment(name, quantity, quantity, 0, purchaseSem, lifespanYears);
        equipments.addEquipment(equipment);
        storage.save(equipments.getAllEquipments());
        ui.showMessage("Added " + quantity + " of " + name + ". (Total Available: "
                + equipment.getAvailable() + ") Purchase: "
                + purchaseSem.toString() + " | Lifespan: " +
                lifespanYears + (lifespanYears == 1.0 ? " year" : " years"));
    }
}
