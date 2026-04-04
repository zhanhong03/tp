package seedu.equipmentmaster.modulelist;

import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.module.Module;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the collection of all course modules in the system.
 * Provides operations to add, update, delete, and retrieve modules.
 */
public class ModuleList {
    private static final Logger logger = Logger.getLogger(ModuleList.class.getName());

    private final ArrayList<Module> modules;

    /**
     * Constructs an empty {@code ModuleList}.
     */
    public ModuleList() {
        this.modules = new ArrayList<>();
    }

    /**
     * Adds a new module to the list.
     * If a module with the same name (case-insensitive) already exists,
     * the new module is not added to avoid ambiguous duplicates.
     *
     * @param module The {@code Module} to be added.
     */
    public void addModule(Module module) {
        if (module == null) {
            logger.log(Level.WARNING, "Attempted to add a null module to ModuleList.");
            return;
        }
        if (findModule(module.getName()) != null) {
            logger.log(Level.INFO, "Duplicate module addition ignored: " + module.getName());
            // A module with this name already exists; do not add a duplicate.
            return;
        }
        logger.log(Level.INFO, "Successfully added module: " + module.getName());
        modules.add(module);
    }

    /**
     * Updates the pax of an existing module in the list.
     *
     * @param moduleName The name of the module to update.
     * @param newPax     The new student enrollment number.
     * @throws EquipmentMasterException If the specified module does not exist in the list.
     */
    public void updateModule(String moduleName, int newPax) throws EquipmentMasterException {
        assert moduleName != null && !moduleName.trim().isEmpty() : "Module name cannot be null or empty";
        assert newPax >= 0 : "Pax cannot be negative";

        Module module = findModule(moduleName);
        if (module == null) {
            logger.log(Level.WARNING, "Update failed. Module not found: " + moduleName);
            throw new EquipmentMasterException("Module not found: " + moduleName);
        }
        module.setPax(newPax);
        logger.log(Level.INFO, "Updated module " + moduleName + " with new pax: " + newPax);
    }

    /**
     * Deletes a module from the list by its name.
     *
     * @param moduleName The name of the module to delete.
     * @throws EquipmentMasterException If the specified module does not exist in the list.
     */
    public void deleteModule(String moduleName) throws EquipmentMasterException {
        assert moduleName != null && !moduleName.trim().isEmpty() : "Module name cannot be null or empty";

        Module module = findModule(moduleName);
        if (module == null) {
            logger.log(Level.WARNING, "Delete failed. Module not found: " + moduleName);
            throw new EquipmentMasterException("Module not found: " + moduleName);
        }
        modules.remove(module);
        logger.log(Level.INFO, "Deleted module: " + moduleName);
    }

    /**
     * Helper method to find a module by its name.
     *
     * @param moduleName The name of the module to search for.
     * @return The {@code Module} object if found, or {@code null} if not found.
     */
    private Module findModule(String moduleName) {
        assert moduleName != null : "Search keyword for module name cannot be null";

        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(moduleName)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Retrieves a COPY of the entire list of modules.
     * This prevents Representation Exposure (Rep Exposure), ensuring external classes
     * cannot modify the internal list without using the proper methods.
     *
     * @return A shallow copy of the {@code ArrayList} containing all current modules.
     */
    public ArrayList<Module> getModules() {
        return new ArrayList<>(modules);
    }

    public boolean hasModule(String moduleName) {
        // Reuses findModule so we guarantee the exact same search logic!
        return findModule(moduleName) != null;
    }

    /**
     * Retrieves a module from the list by its name (case-insensitive).
     *
     * @param moduleName The name of the module to find.
     * @return The Module object if found, or null if it doesn't exist.
     */
    public Module getModule(String moduleName) {
        // Wraps your private helper method so external commands can use it
        return findModule(moduleName);
    }
}
