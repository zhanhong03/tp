package seedu.equipmentmaster;

import java.util.logging.LogManager;
import seedu.equipmentmaster.commands.Command;
import seedu.equipmentmaster.context.Context;
import seedu.equipmentmaster.equipmentlist.EquipmentList;
import seedu.equipmentmaster.exception.EquipmentMasterException;
import seedu.equipmentmaster.modulelist.ModuleList;
import seedu.equipmentmaster.parser.Parser;
import seedu.equipmentmaster.semester.AcademicSemester;
import seedu.equipmentmaster.storage.Storage;
import seedu.equipmentmaster.ui.Ui;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EquipmentMaster {
    private static final String DEFAULT_EQUIPMENT_PATH = "data/equipment.txt";
    private static final String DEFAULT_SETTING_PATH = "data/setting.txt";
    private static final String DEFAULT_MODULE_PATH = "data/module.txt";
    private static final String FALLBACK_SEMESTER = "AY2024/25 Sem1";

    private static final Logger logger = Logger.getLogger(EquipmentMaster.class.getName());

    private final Storage storage;
    private AcademicSemester currentSystemSemester;
    private final Ui ui;
    private final EquipmentList equipments;
    private final ModuleList moduleList;

    /**
     * Initializes the application, loads system settings, and populates the equipment list.
     *
     * @param equipmentFilePath The relative path to the data.txt storage file.
     * @param settingFilePath The relative path to the setting.txt storage file.
     * @param moduleFilePath The relative path to the module.txt storage file.
     */
    public EquipmentMaster(String equipmentFilePath, String settingFilePath, String moduleFilePath) {
        logger.log(Level.INFO, "Starting EquipmentMaster initialization...");

        this.ui = new Ui();
        this.storage = new Storage(equipmentFilePath, ui, settingFilePath, moduleFilePath);

        this.currentSystemSemester = initializeSemester();

        // Load equipment data
        this.equipments = new EquipmentList(storage.load());
        logger.log(Level.INFO, "System time loaded successfully.");

        this.moduleList = storage.loadModules();

        verifySystemReadiness();
    }

    /**
     * Attempts to load the saved semester or falls back to a default.
     */
    private AcademicSemester initializeSemester() {
        try {
            String savedSemStr = storage.loadSettings();
            return new AcademicSemester(savedSemStr);
        } catch (EquipmentMasterException e) {
            logger.log(Level.WARNING, "Corrupted or missing settings. Falling back to default semester.");
            try {
                return new AcademicSemester(FALLBACK_SEMESTER);
            } catch (EquipmentMasterException fatal) {
                // This is only triggered if the FALLBACK_SEMESTER constant itself is invalid
                throw new RuntimeException("Fatal Error: Default semester format is invalid.", fatal);
            }
        }
    }

    /**
     * Ensures all core components are correctly loaded before starting the loop.
     */
    private void verifySystemReadiness() {
        assert Parser.getCommandSpecs() != null : "Parser must be initialized";
        assert !Parser.getCommandSpecs().isEmpty() : "No commands loaded in Parser!";

        logger.log(Level.INFO, "System time loaded: " + currentSystemSemester);
        logger.log(Level.INFO, "Parser ready with " + Parser.getCommandSpecs().size() + " commands.");
    }

    public void run() {
        ui.showWelcomeMessage();

        Context context = new Context(equipments, moduleList, ui, storage, currentSystemSemester);
        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();

                if (fullCommand.trim().isEmpty()) {
                    continue;
                }

                ui.showLine();
                Command c = Parser.parse(fullCommand);
                c.execute(context);
                isExit = c.isExit();
            } catch (EquipmentMasterException e) {
                ui.showMessage(e.getMessage());
                logger.log(Level.FINE, "Execution error: " + e.getMessage());
            } finally {
                ui.showLine();
            }
        }
        logger.log(Level.INFO, "Application shutting down gracefully.");
    }

    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        new EquipmentMaster(DEFAULT_EQUIPMENT_PATH, DEFAULT_SETTING_PATH,
                DEFAULT_MODULE_PATH).run();
    }

    public EquipmentList getEquipmentList() {
        return this.equipments;
    }
}
